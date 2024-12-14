package com.example.seobuchurch.Controller;

import com.example.seobuchurch.DTO.LoginDTO;
import com.example.seobuchurch.DTO.TokenDTO;
import com.example.seobuchurch.DTO.SignupDTO;
import com.example.seobuchurch.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(@RequestBody SignupDTO user) { service.signup(user); }

    @PostMapping("/login")
    public TokenDTO login(@RequestBody LoginDTO loginDTO) {
        System.out.println(loginDTO.getUsername());
        System.out.println(loginDTO.getPassword());
        return service.login(loginDTO);
    }

    @GetMapping("/refreshToken")
    public TokenDTO accessTokenFromRefreshToken(@RequestHeader("RefreshToken") String refreshToken) {
        return new TokenDTO();
    }
}
