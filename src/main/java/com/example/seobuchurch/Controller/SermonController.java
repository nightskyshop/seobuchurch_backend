package com.example.seobuchurch.Controller;

import com.example.seobuchurch.Entity.SermonEntity;
import com.example.seobuchurch.JWT.TokenProvider;
import com.example.seobuchurch.Service.SermonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sermon")
public class SermonController {
    @Autowired
    private SermonService service;

    private final TokenProvider tokenProvider;

    @GetMapping("")
    public Page<SermonEntity> getAllSermon(@RequestParam int pageNo, @RequestParam String worshipTime) {
        return service.getAllSermon(pageNo, worshipTime);
    }

    @GetMapping("/current")
    public SermonEntity getSermon() {
        return service.getCurrentSermon();
    }

    @GetMapping("/{id}")
    public SermonEntity getSermon(@PathVariable int id) {
        return service.getSermon(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createSermon(@RequestBody SermonEntity sermon, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        if (sermon.getTitle().isBlank() || sermon.getVerse().isBlank() || sermon.getPastor().isBlank() || sermon.getWorshipTime().isBlank() || sermon.getUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모든 항목을 채워주세요.");
        }

        service.createSermon(sermon);
    }

    @PatchMapping("/{id}")
    public void updateSermon(@PathVariable int id, @RequestBody SermonEntity post, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        if (post.getTitle().isBlank() || post.getVerse().isBlank() || post.getPastor().isBlank() || post.getWorshipTime().isBlank() || post.getUrl().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모든 항목을 채워주세요.");
        }

        service.updateSermon(id, post);
    }

    @DeleteMapping("/{id}")
    public void deleteSermon(@PathVariable int id, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        service.deleteSermon(id);
    }
}
