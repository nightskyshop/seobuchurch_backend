package com.example.seobuchurch.Controller;

import com.example.seobuchurch.Entity.ChurchNewsEntity;
import com.example.seobuchurch.Entity.ColumnEntity;
import com.example.seobuchurch.JWT.TokenProvider;
import com.example.seobuchurch.Service.ChurchNewsService;
import com.example.seobuchurch.Service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/churchNews")
public class ChurchNewsController {
    @Autowired
    private ChurchNewsService service;

    private final TokenProvider tokenProvider;

    @GetMapping("")
    public Page<ChurchNewsEntity> getAllChurchNews(@RequestParam int pageNo) {
        return service.getAllChurchNews(pageNo);
    }

    @GetMapping("/{id}")
    public ChurchNewsEntity getChurchNews(@PathVariable int id) {
        return service.getChurchNews(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createChurchNews(@RequestBody ChurchNewsEntity churchNews, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        if (churchNews.getTitle().isBlank() || churchNews.getUrl().isBlank() || churchNews.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모든 항목을 채워주세요.");
        }

        service.createChurchNews(churchNews);
    }

    @PatchMapping("/{id}")
    public void updateChurchNews(@PathVariable int id, @RequestBody ChurchNewsEntity churchNews, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        if (churchNews.getTitle().isBlank() || churchNews.getUrl().isBlank() || churchNews.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모든 항목을 채워주세요.");
        }

        service.updateChurchNews(id, churchNews);
    }

}
