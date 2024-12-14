package com.example.seobuchurch.Repository;

import com.example.seobuchurch.Entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    Page<PostEntity> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);
    PostEntity findById(int id);
}
