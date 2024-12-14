package com.example.seobuchurch.DTO;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
}
