package com.example.seobuchurch.Repository;

import com.example.seobuchurch.Entity.SermonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SermonRepository extends JpaRepository<SermonEntity, Integer> {
    Page<SermonEntity> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
    Page<SermonEntity> findAllByWorshipTimeOrderByCreatedAtDesc(PageRequest pageRequest, String worshipTime);
    SermonEntity findFirstByWorshipTimeOrderByCreatedAtDesc(String worshipTime);
    SermonEntity findById(int id);
}
