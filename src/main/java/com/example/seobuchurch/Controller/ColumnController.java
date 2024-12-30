package com.example.seobuchurch.Controller;

import com.example.seobuchurch.Entity.ColumnEntity;
import com.example.seobuchurch.JWT.TokenProvider;
import com.example.seobuchurch.Service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/column")
public class ColumnController {
    @Autowired
    private ColumnService service;

    private final TokenProvider tokenProvider;

    @GetMapping("")
    public Page<ColumnEntity> getAllColumn(@RequestParam int pageNo) {
        return service.getAllColumn(pageNo);
    }

    @GetMapping("/{id}")
    public ColumnEntity getColumn(@PathVariable int id) {
        return service.getColumn(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void createColumn(@RequestBody ColumnEntity column, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        if (column.getTitle().isBlank() || column.getContent().isBlank() || column.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모든 항목을 채워주세요.");
        }

        service.createColumn(column);
    }

    @PatchMapping("/{id}")
    public void updateColumn(@PathVariable int id, @RequestBody ColumnEntity column, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 전달되지 않았습니다.");
        }

        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다.");
        }

        if (column.getTitle().isBlank() || column.getContent().isBlank() || column.getAuthor().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "모든 항목을 채워주세요.");
        }

        service.updateColumn(id, column);
    }

}
