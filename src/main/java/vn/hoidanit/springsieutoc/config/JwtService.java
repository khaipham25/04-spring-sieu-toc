package vn.hoidanit.springsieutoc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.ExchangeTokenResponseDTO;
import vn.hoidanit.springsieutoc.model.dto.LoginResponseDTO;
import vn.hoidanit.springsieutoc.service.RefreshTokenService;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    private final JwtEncoder jwtEncoder;
    private final RefreshTokenService refreshTokenService;

    @Value("${hoidanit.jwt.access-token-validity-in-seconds}")
    private Long accessTokenExpiration;

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private Long refreshTokenExpiration;

    public String getScope(Authentication authentication) {
        if (authentication != null) {
            // ghép các quyền thành 1 string: "ROLE_USER ROLE_ADMIN"
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            return scope;
        }
        return "UNKNOWN";
    }

    public String generateSecureToken() {
        byte[] randomBytes = new byte[64]; // 512 bits
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public String createRefreshToken(User user) {
        // todo: lưu RFTK vào databsase
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);
        String token = generateSecureToken();

        RefreshToken refreshToken = new RefreshToken();
        // mục đích đoạn này để lưu xuống database còn return cái refreshToken
        refreshToken.setCreatedAt(now);
        refreshToken.setExpiredAt(validity);
        refreshToken.setToken(token);
        refreshToken.setUser(user);

        // Lưu token xuống DB
        refreshTokenService.createRefreshToken(refreshToken);

        return token;
    }


    public String createAccessToken(Authentication authentication, Long userId) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        // ghép các quyền thành 1 string: "ROLE_USER ROLE_ADMIN"
        String scope = getScope(authentication);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                // username đó
                .subject(authentication.getName())
                .claim("id", userId)
                .claim("scope", scope)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public ExchangeTokenResponseDTO handleExchangeToken(String inputToken) {
        // check refresh token in db
        RefreshToken currentRefreshToken = this.refreshTokenService
                .findByToken(inputToken);

        // validate token
        // check xem so với thời gian bây giờ xem nằm bên trái (còn hạn) không
        Instant now = Instant.now();
        if (now.isAfter(currentRefreshToken.getExpiredAt())) {
            throw new ResourceNotFoundException("Refresh token đã hết hạn");
        }

        // create new token
        // 1 user có nhiều token nên khi query token nó mặc định query luôn đối tuong User
        User currentUser = currentRefreshToken.getUser();
        String newRefreshToken = this.createRefreshToken(currentUser);

        Instant validity = now.plus(accessTokenExpiration, ChronoUnit.SECONDS);

        String scope = "ROLE_" + currentUser.getRole().getName();

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(currentUser.getEmail())
                .claim("id", currentUser.getId())
                .claim("scope", scope)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        String accessToken = this.jwtEncoder.encode( JwtEncoderParameters.from(jwsHeader, claims) ).getTokenValue();

        // @formatter:on
        ExchangeTokenResponseDTO exToken = new ExchangeTokenResponseDTO();
        exToken.setAccessToken(accessToken);
        exToken.setRefreshToken(newRefreshToken);
        exToken.setUser(new LoginResponseDTO.UserLogin(currentUser.getId(),
                currentUser.getEmail(), scope));

        // delete old refreshToken
        this.refreshTokenService.deleteById(currentRefreshToken.getId());

        return exToken;
    }
}
