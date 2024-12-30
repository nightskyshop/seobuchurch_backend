package com.example.seobuchurch.Controller;

import com.example.seobuchurch.Config.Authority;
import com.example.seobuchurch.Entity.UserEntity;
import com.example.seobuchurch.JWT.TokenProvider;
import com.example.seobuchurch.Repository.UserRepository;
import com.example.seobuchurch.Service.UserService;
import com.sun.net.httpserver.HttpsServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private UserRepository repository;
    private final TokenProvider tokenProvider;

    @GetMapping("/my")
    public UserEntity getMyUser(@RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("userId: " + userId);

        int tokenId;

        try {
            tokenId = Integer.parseInt(userId);
        } catch (Exception err) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자의 ID를 확인할 수 없습니다.");
        }

        if (repository.existsById(tokenId)) {
            return repository.findById(tokenId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public void updateUser(@PathVariable int id, @RequestHeader("Authorization") String accessToken, @RequestBody UserEntity dto) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        String userId = tokenProvider.getUserIdFromToken(token);
        int tokenId;

        try {
            tokenId = Integer.parseInt(userId);
        } catch (Exception err) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자의 ID를 확인할 수 없습니다.");
        }

        if (repository.existsById(tokenId)) {
            UserEntity user = repository.findById(tokenId);
            if (user.getAuthority() != Authority.ROLE_ADMIN && tokenId != id) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저가 없습니다.");
        }

        service.updateUser(tokenId, dto);
    }
}
