package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.UserEntity;
import com.example.seobuchurch.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void updateUser(int id, UserEntity dto) {
        UserEntity entity = repository.findById(id);
        entity.setUsername(dto.getUsername());
        repository.save(entity);
    }
}
