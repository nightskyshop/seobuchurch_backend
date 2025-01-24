package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.SermonEntity;
import com.example.seobuchurch.Repository.SermonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SermonService {
    @Autowired
    private SermonRepository repository;

    public Page<SermonEntity> getAllSermon(int pageNo, String worshipTime) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return repository.findAllByWorshipTimeOrderByCreatedAtDesc(pageRequest, worshipTime);
    }

    public SermonEntity getCurrentSermon() {
        return repository.findFirstByWorshipTimeOrderByCreatedAtDesc("주일예배");
    }

    public SermonEntity getSermon(int id) {
        if (repository.existsById(id)) {
            return repository.findById(id);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
    }

    public void createSermon(SermonEntity sermon) {
        repository.save(sermon);
    }

    public void updateSermon(int id, SermonEntity sermon) {
        if (repository.existsById(id)) {
            SermonEntity entity = repository.findById(id);

            entity.setTitle(sermon.getTitle());
            entity.setVerse(sermon.getVerse());
            entity.setPastor(sermon.getPastor());
            entity.setWorshipTime(sermon.getWorshipTime());
            entity.setUrl(sermon.getUrl());

            repository.save(entity);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
        }
    }

    public void deleteSermon(int id) {
        repository.deleteById(id);
    }
}
