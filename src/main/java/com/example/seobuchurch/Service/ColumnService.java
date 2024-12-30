package com.example.seobuchurch.Service;

import com.example.seobuchurch.Entity.ColumnEntity;
import com.example.seobuchurch.Entity.SermonEntity;
import com.example.seobuchurch.Repository.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ColumnService {
    @Autowired
    private ColumnRepository repository;

    public Page<ColumnEntity> getAllColumn(int pageNo) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        return repository.findAllByOrderByCreatedAtDesc(pageRequest);
    }

    public ColumnEntity getColumn(int id) {
        if (repository.existsById(id)) {
            return repository.findById(id);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다.");
    }

    public void createColumn(ColumnEntity column) {
        repository.save(column);
    }



    public void updateColumn(int id, ColumnEntity column) {
        if (repository.existsById(id)) {
            ColumnEntity entity = repository.findById(id);
            entity.setTitle(column.getTitle());
            entity.setContent(column.getContent());
            entity.setAuthor(column.getAuthor());

            repository.save(entity);
        }
    }

}
