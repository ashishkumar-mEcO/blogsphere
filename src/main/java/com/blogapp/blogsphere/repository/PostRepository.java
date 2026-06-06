package com.blogapp.blogsphere.repository;

import com.blogapp.blogsphere.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorEmail(String email);
    List<Post> findByTitleContaining(String keyword);
}