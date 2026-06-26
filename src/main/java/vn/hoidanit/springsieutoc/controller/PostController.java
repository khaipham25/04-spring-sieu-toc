package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.dto.PostRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.PostResponseDTO;
import vn.hoidanit.springsieutoc.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(@Valid @RequestBody PostRequestDTO inputPost) {

        PostResponseDTO post = this.postService.createPost(inputPost);
        return ApiResponse.created(post);
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getPosts() {
        List<PostResponseDTO> posts = this.postService.fetchPosts();
        return ApiResponse.success(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPost(@PathVariable Long id) {
        PostResponseDTO post = this.postService.fetchPostById(id);
        return ApiResponse.success(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> putPost(@PathVariable Long id,
                                                                @Valid @RequestBody PostRequestDTO inputPost) {
        PostResponseDTO post = this.postService.updatePostById(id, inputPost);
        return ApiResponse.success(post);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long id) {
        this.postService.deletePostById(id);
        return ApiResponse.success("ok");
    }

}
