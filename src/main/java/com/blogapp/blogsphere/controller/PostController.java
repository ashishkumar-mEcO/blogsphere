//package com.blogapp.blogsphere.controller;
//
//import com.blogapp.blogsphere.model.Post;
//import com.blogapp.blogsphere.service.PostService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/posts")
//@RequiredArgsConstructor
//public class PostController {
//
//    private final PostService postService;
//
//    // Get all posts
//    @GetMapping
//    public ResponseEntity<List<Post>> getAllPosts() {
//        return ResponseEntity.ok(postService.getAllPosts());
//    }
//
//    // Get post by id
//    @GetMapping("/{id}")
//    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
//        return postService.getPostById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // Search posts
//    @GetMapping("/search")
//    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
//        return ResponseEntity.ok(postService.searchPosts(keyword));
//    }
//
//    // Create post
//    @PostMapping
//    public ResponseEntity<Post> createPost(@RequestBody Post post) {
//        return ResponseEntity.ok(postService.createPost(post));
//    }
//
//    // Update post
//    @PutMapping("/{id}")
//    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post post) {
//        post.setId(id);
//        return ResponseEntity.ok(postService.updatePost(post));
//    }
//
//    // Delete post
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
//        postService.deletePost(id);
//        return ResponseEntity.ok().build();
//    }
//}









package com.blogapp.blogsphere.controller;

import com.blogapp.blogsphere.model.Post;
import com.blogapp.blogsphere.service.CloudinaryService;
import com.blogapp.blogsphere.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CloudinaryService cloudinaryService;

    // Get all posts — public
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // Get post by id — public
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    // Search posts — public
    @GetMapping("/search")
    public ResponseEntity<List<Post>> searchPosts(@RequestParam String keyword) {
        return ResponseEntity.ok(postService.searchPosts(keyword));
    }

    // Get my posts — authenticated
    @GetMapping("/my-posts")
    public ResponseEntity<List<Post>> getMyPosts(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(postService.getPostsByAuthor(userDetails.getUsername()));
    }

    // Create post — authenticated
    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody Post post,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                postService.createPost(post, userDetails.getUsername()));
    }

    // Update post — authenticated
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id,
            @RequestBody Post post,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                postService.updatePost(id, post, userDetails.getUsername()));
    }

    // Delete post — authenticated
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.ok("Post deleted successfully!");
    }

    @PostMapping("/{id}/thumbnail")
    public ResponseEntity<String> uploadThumbnail(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Upload image to Cloudinary
        String imageUrl = cloudinaryService.uploadFile(file);

        // Get post and update thumbnail
        Post post = postService.getPostById(id);
        post.setThumbnailUrl(imageUrl);
        postService.updatePost(id, post, userDetails.getUsername());

        return ResponseEntity.ok(imageUrl);
    }
}