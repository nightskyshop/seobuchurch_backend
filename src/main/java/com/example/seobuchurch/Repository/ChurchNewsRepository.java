package com.example.seobuchurch.Repository;

import com.example.seobuchurch.Entity.ChurchNewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChurchNewsRepository extends JpaRepository<ChurchNewsEntity, Integer> {
    Page<ChurchNewsEntity> findAllByOrderByTitleDesc(PageRequest pageRequest);
    ChurchNewsEntity findById(int id);
    boolean existsById(int id);
}
