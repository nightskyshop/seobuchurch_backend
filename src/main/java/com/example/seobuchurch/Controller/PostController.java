package com.example.seobuchurch.Controller;

import com.example.seobuchurch.Entity.PostEntity;
import com.example.seobuchurch.JWT.TokenProvider;
import com.example.seobuchurch.Service.PostService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService service;

    private final TokenProvider tokenProvider;

    @GetMapping("")
    public Page<PostEntity> getAllPost(@RequestParam int pageNo) {
        return service.getAllPost(pageNo);
    }

    @GetMapping("/{id}")
    public PostEntity getPost(@PathVariable int id) {
        return service.getPost(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createPost(@RequestBody PostEntity post, @RequestHeader("Authorization") String accessToken) {
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

        service.createPost(post);
    }

    @PatchMapping("/{id}")
    public void updatePost(@PathVariable int id, @RequestBody PostEntity post, @RequestHeader("Authorization") String accessToken) {
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

        service.updatePost(id, post);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        service.deletePost(id);
    }
}
