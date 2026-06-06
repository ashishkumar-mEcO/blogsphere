package com.blogapp.blogsphere.service;

import com.blogapp.blogsphere.exception.ResourceNotFoundException;
import com.blogapp.blogsphere.model.Comment;
import com.blogapp.blogsphere.model.Post;
import com.blogapp.blogsphere.model.User;
import com.blogapp.blogsphere.repository.CommentRepository;
import com.blogapp.blogsphere.repository.PostRepository;
import com.blogapp.blogsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor    //Lombok —final fields can't be initialized by Java's default constructor, so @RequiredArgsConstructor generates a parameterized constructor automatically
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;//for this we use @RequiredArgsConstructor to initialized  it properly!

//    public CommentService(CommentRepository commentRepository) {   //no need for this
//        this.commentRepository = commentRepository;
//    }

    // Get comments by post
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // Add comment
    public Comment addComment(Long postId, String content, String email) {

        // Find user from email (JWT token)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));

        // Find post by id
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Create comment
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    // Delete comment
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}