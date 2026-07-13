package vn.hoidanit.springsieutoc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeTokenResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private LoginResponseDTO.UserLogin user;
}
