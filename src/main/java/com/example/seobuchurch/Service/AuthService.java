package com.example.seobuchurch.Service;

import com.example.seobuchurch.Config.Authority;
import com.example.seobuchurch.DTO.LoginDTO;
import com.example.seobuchurch.DTO.TokenDTO;
import com.example.seobuchurch.DTO.SignupDTO;
import com.example.seobuchurch.Entity.UserEntity;
import com.example.seobuchurch.JWT.TokenProvider;
import com.example.seobuchurch.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Value("${jwt.adminPassword}")
    private String adminPassword;

    public UserEntity signup(SignupDTO userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "이미 가입되어 있는 유저입니다.");
        }

        String inputedAdminPassword = userDto.getAdminPassword();
        System.out.println(adminPassword);
        System.out.println(inputedAdminPassword);
        if (!inputedAdminPassword.equals(adminPassword)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin 암호가 일치하지 않습니다.");
        }

        String password = userDto.getPassword();
        String passwordRegex = "^(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

        if (!Pattern.matches(passwordRegex, password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "비밀번호는 최소 8자 이상이며, 숫자와 특수문자(@$!%*?&)를 각각 하나 이상 포함해야 합니다.");
        }

        UserEntity user = userDto.toUser(passwordEncoder);
        user.setAuthority(Authority.ROLE_USER);
        return userRepository.save(user);
    }

    public TokenDTO login(LoginDTO requestDto) {
        System.out.println("login");
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthenticaiton();
        System.out.println("login1");
        System.out.println(authenticationToken);
        Authentication authentication;

        try {
            authentication = managerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception err) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀버호가 틀립니다.");
        }

        UserEntity user = userRepository.findByUsername(requestDto.getUsername()).get();

        System.out.println(authentication);
        System.out.println("login2");
        return tokenProvider.generateTokenDto(authentication);
    }
}
