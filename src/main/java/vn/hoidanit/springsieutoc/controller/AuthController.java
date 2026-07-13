/*
 * Author: Hỏi Dân IT - @hoidanit 
 *
 * This source code is developed for the course
 * "Java Spring Siêu Tốc - Tự Học Java Spring Từ Số 0 Dành Cho Beginners từ A tới Z".
 * It is intended for educational purposes only.
 * Unauthorized distribution, reproduction, or modification is strictly prohibited.
 *
 * Copyright (c) 2025 Hỏi Dân IT. All Rights Reserved.
 */

package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import vn.hoidanit.springsieutoc.config.JwtService;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.RefreshToken;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.ExchangeTokenResponseDTO;
import vn.hoidanit.springsieutoc.model.dto.LoginRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.LoginResponseDTO;
import vn.hoidanit.springsieutoc.model.dto.RegisterRequestDTO;
import vn.hoidanit.springsieutoc.service.RefreshTokenService;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final RefreshTokenService refreshTokenService;

	@Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
	private Long refreshTokenExpiration;

	@PostMapping("/auth/login")
	public ResponseEntity<?> postLogin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

		log.info("hello world with username ={} and password ={}", loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

		// Nếu đăng nhập thành công nó sẽ trả ra biến authentication, chính là hàm loadUserByUsername trong file CustomUserDetailsService
		// authencation chính là ng dùng sau đăng nhập

		// thực ra chỗ này DaoAuthenticationProvider.authenticate(authToken), mình đổi tên khâc r :v
		Authentication authentication = authenticationManager.authenticate(authToken);

		User currentUser = userService.findUserByEmail(authentication.getName());
		// tạo accessToken
		String accessToken = jwtService.createAccessToken(authentication, currentUser.getId());
		String refreshToken = jwtService.createRefreshToken(currentUser);
		// login xong tạo 1 cái refreshToken lưu xuống DB và trả ra LoginResponseDTO

		LoginResponseDTO res = new LoginResponseDTO();
		res.setAccessToken(accessToken);
		res.setRefreshToken(refreshToken);
		res.setUser(new LoginResponseDTO.UserLogin(currentUser.getId(), authentication.getName(), jwtService.getScope(authentication)));

		// set cookies
		// @formatter:off
		ResponseCookie resCookies = ResponseCookie
				.from("refreshToken", refreshToken)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(refreshTokenExpiration)
				.build();

		ApiResponse<LoginResponseDTO> finalData = new ApiResponse<>(
				HttpStatus.OK, "", res, "");
		// @formatter:on

		// body là json trả về cho client
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString())
				.body(finalData);
	}

	@PostMapping("/auth/refresh")
	public ResponseEntity<?> postRefreshToken(@RequestParam("token") String refreshToken) {

		ExchangeTokenResponseDTO res = jwtService.handleExchangeToken(refreshToken);

		return ApiResponse.success(res);
	}

	@PostMapping("/auth/refresh-with-cookie")
	public ResponseEntity<?> postRefreshTokenWithCookie(
			@CookieValue(required = false) String refreshToken) {

		ExchangeTokenResponseDTO res = this.jwtService.handleExchangeToken(refreshToken);

		// set cookies
		// @formatter:off
		ResponseCookie resCookies = ResponseCookie
				.from("refreshToken", res.getRefreshToken())
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(refreshTokenExpiration)
				.build();

		ApiResponse<ExchangeTokenResponseDTO> finalData = new ApiResponse<>(
				HttpStatus.OK, "", res, "");
		// @formatter:on

		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString())
				.body(finalData);
	}

	@GetMapping("/auth/account")
	public ResponseEntity<?> getAccount() {
		// lay ra thông tin người dùng bằng SecurityContextHolder
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Jwt jwt = (Jwt) auth.getPrincipal();

		String userId = jwt.getClaimAsString("id");
		String username = jwt.getSubject();
		String role = jwt.getClaimAsString("scope");

		LoginResponseDTO.UserLogin user = new LoginResponseDTO.UserLogin();
		user.setId(Long.valueOf(userId));
		user.setUsername(username);
		user.setRole(role);

		return ApiResponse.success(user);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<?> postLogout(@AuthenticationPrincipal Jwt jwt,
	                                    @CookieValue(required = false) String refreshToken) {
		// lấy ra accessToken để lấy thông tin ng dùng và lấy refreshToken từ cookie
		String userId = jwt.getClaimAsString("id");
		String username = jwt.getSubject();

		// remove refresh token in database
		RefreshToken currentTokenInDB = this.refreshTokenService.findByToken(refreshToken);
		this.refreshTokenService.deleteById(currentTokenInDB.getId());

		// remove refresh token cookie
		// @formatter:off
		ResponseCookie deleteSpringCookie = ResponseCookie
				.from("refreshToken", null)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();
		ApiResponse<String> finalData = new ApiResponse<>(HttpStatus.OK, "", "ok", "");

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
				.body(finalData);
	}

	@PostMapping("/auth/register")
	public ResponseEntity<?> postRegister(@Valid @RequestBody RegisterRequestDTO inputUser){
		userService.registerNewUser(inputUser);
		return ApiResponse.success("ok");
	}
}
