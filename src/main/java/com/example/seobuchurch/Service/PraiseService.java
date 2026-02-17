package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.PraiseEntity;
import com.example.seobuchurch.Repository.PraiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PraiseService {
    @Autowired
    private PraiseRepository repository;

    public Page<PraiseEntity> getAllSermon(int pageNo, String worshipTime) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return repository.findAllByWorshipTimeOrderByCreatedAtDesc(pageRequest, worshipTime);
    }

    public PraiseEntity getSermon(int id) {
        if (repository.existsById(id)) {
            return repository.findById(id);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
    }

    public void createSermon(PraiseEntity sermon) {
        repository.save(sermon);
    }

    public void updateSermon(int id, PraiseEntity sermon) {
        if (repository.existsById(id)) {
            PraiseEntity entity = repository.findById(id);

            entity.setTitle(sermon.getTitle());
            entity.setWorshipTime(sermon.getWorshipTime());
            entity.setUrl(sermon.getUrl());
            entity.setCreatedAt(sermon.getCreatedAt());

            repository.save(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
        }
    }

    public void deleteSermon(int id) {
        repository.deleteById(id);
    }
}
