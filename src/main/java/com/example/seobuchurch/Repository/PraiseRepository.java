package com.example.seobuchurch.Repository;

import com.example.seobuchurch.Entity.PraiseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PraiseRepository extends JpaRepository<PraiseEntity, Integer> {
    Page<PraiseEntity> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
    Page<PraiseEntity> findAllByWorshipTimeOrderByCreatedAtDesc(PageRequest pageRequest, String worshipTime);
    PraiseEntity findFirstByWorshipTimeOrderByCreatedAtDesc(String worshipTime);
    PraiseEntity findById(int id);
}
