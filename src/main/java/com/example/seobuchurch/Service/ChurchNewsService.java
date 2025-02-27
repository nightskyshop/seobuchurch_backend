package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.ChurchNewsEntity;
import com.example.seobuchurch.Repository.ChurchNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChurchNewsService {
    @Autowired
    private ChurchNewsRepository repository;

    public Page<ChurchNewsEntity> getAllChurchNews(int pageNo) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return repository.findAllByOrderByCreatedAtDesc(pageRequest);
    }

    public ChurchNewsEntity getChurchNews(int id) {
        if (repository.existsById(id)) {
            return repository.findById(id);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
    }

    public void createChurchNews(ChurchNewsEntity churchNews) {
        repository.save(churchNews);
    }



    public void updateChurchNews(int id, ChurchNewsEntity churchNews) {
        if (repository.existsById(id)) {
            ChurchNewsEntity entity = repository.findById(id);
            entity.setTitle(churchNews.getTitle());
            entity.setAuthor(churchNews.getAuthor());
            entity.setUrl(churchNews.getUrl());

            repository.save(entity);
        }
    }

}
