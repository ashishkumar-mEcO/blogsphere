package com.blogapp.blogsphere.service;

import com.blogapp.blogsphere.exception.ResourceNotFoundException;
import com.blogapp.blogsphere.model.Like;
import com.blogapp.blogsphere.model.Post;
import com.blogapp.blogsphere.model.User;
import com.blogapp.blogsphere.repository.LikeRepository;
import com.blogapp.blogsphere.repository.PostRepository;
import com.blogapp.blogsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // Toggle like
    @Transactional
    public Map<String, Object> toggleLike(Long postId, String email) {

        // Find post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found!"));

        // Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        boolean liked;

        // If already liked → unlike
        if (likeRepository.existsByUserEmailAndPostId(email, postId)) {
            likeRepository.deleteByUserEmailAndPostId(email, postId);
            liked = false;
        } else {
            // If not liked → like
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            liked = true;
        }

        int count = likeRepository.countByPostId(postId);
        return Map.of("liked", liked, "count", count);
    }

    // Get like count
    public Map<String, Object> getLikeInfo(Long postId, String email) {
        boolean liked = email != null &&
                likeRepository.existsByUserEmailAndPostId(email, postId);
        int count = likeRepository.countByPostId(postId);
        return Map.of("liked", liked, "count", count);
    }
}