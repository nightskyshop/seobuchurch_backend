package com.example.seobuchurch.DTO;

import com.example.seobuchurch.Entity.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@RequiredArgsConstructor
public class SignupDTO {
    private String username;
    private String password;
    private String adminPassword;

    public UserEntity toUser(PasswordEncoder passwordEncoder) {
        UserEntity new_user = new UserEntity();
        new_user.setUsername(username);
        new_user.setPassword(passwordEncoder.encode(password));
        return new_user;
    }
}
