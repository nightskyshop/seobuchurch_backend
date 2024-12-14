package com.example.seobuchurch.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
public class LoginDTO {
    private String username;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthenticaiton() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
