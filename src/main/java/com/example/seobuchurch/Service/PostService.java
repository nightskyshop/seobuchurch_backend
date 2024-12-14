package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.PostEntity;
import com.example.seobuchurch.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {
    @Autowired
    private PostRepository repository;

    public Page<PostEntity> getAllPost(int pageNo) {
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return repository.findAllByOrderByCreatedAtDesc(pageRequest);
    }

    public PostEntity getPost(int id) {
        if (repository.existsById(id)) {
            return repository.findById(id);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
    }

    public void createPost(PostEntity post) {
        repository.save(post);
    }

    public void updatePost(int id, PostEntity post) {
        if (repository.existsById(id)) {
            PostEntity entity = repository.findById(id);

            entity.setTitle(post.getTitle());
            entity.setVerse(post.getVerse());
            entity.setPastor(post.getPastor());
            entity.setWorshipTime(post.getWorshipTime());
            entity.setUrl(post.getUrl());

            repository.save(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
        }
    }

    public void deletePost(int id) {
        repository.deleteById(id);
    }
}
