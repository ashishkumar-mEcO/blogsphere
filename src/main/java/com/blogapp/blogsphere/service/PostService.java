package com.blogapp.blogsphere.service;

import com.blogapp.blogsphere.exception.ResourceNotFoundException;
import com.blogapp.blogsphere.model.Post;
import com.blogapp.blogsphere.model.User;
import com.blogapp.blogsphere.repository.PostRepository;
import com.blogapp.blogsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Get post by id
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post not found with id: " + id));
    }

    // Get posts by author
    public List<Post> getPostsByAuthor(String email) {
        return postRepository.findByAuthorEmail(email);
    }

    // Search posts
    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }

    // Create post
    public Post createPost(Post post, String email) {
        User author = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found!"));
        post.setAuthor(author);
        return postRepository.save(post);
    }

    // Update post
    public Post updatePost(Long id, Post updatedPost, String email) {
        Post post = getPostById(id);

        // Check if user is the author
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("You can only update your own posts!");
        }

        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setThumbnailUrl(updatedPost.getThumbnailUrl());
        return postRepository.save(post);
    }

    // Delete post
    public void deletePost(Long id, String email) {
        Post post = getPostById(id);

        // Check if user is the author
        if (!post.getAuthor().getEmail().equals(email)) {
            throw new RuntimeException("You can only delete your own posts!");
        }

        postRepository.deleteById(id);
    }
}