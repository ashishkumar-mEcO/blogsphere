package com.blogapp.blogsphere.repository;

import com.blogapp.blogsphere.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserEmailAndPostId(String email, Long postId);
    void deleteByUserEmailAndPostId(String email, Long postId);
    int countByPostId(Long postId);
}