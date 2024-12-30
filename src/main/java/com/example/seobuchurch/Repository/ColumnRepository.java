package com.example.seobuchurch.Repository;

import com.example.seobuchurch.Entity.ColumnEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColumnRepository extends JpaRepository<ColumnEntity, Integer> {
    Page<ColumnEntity> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
    ColumnEntity findById(int id);
    boolean existsById(int id);
}
