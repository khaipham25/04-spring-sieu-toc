package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.dto.CommentResponseDTO;
import vn.hoidanit.springsieutoc.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<Comment>> postComment(@Valid @RequestBody Comment comment) {
        Comment savedComment = this.commentService.createComment(comment);
        return ApiResponse.created(savedComment);
    }

    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getComments() {
        List<CommentResponseDTO> comments = this.commentService.fetchComments();
        return ApiResponse.success(comments);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> getComment(@PathVariable Long id) {
        CommentResponseDTO comment = this.commentService.fetchCommentById(id);
        return ApiResponse.success(comment);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> putComment(@PathVariable Long id,
                                                                      @Valid @RequestBody Comment inputComment) {
        CommentResponseDTO comment = this.commentService.updateCommentById(id, inputComment);
        return ApiResponse.success(comment);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long id) {
        this.commentService.deleteCommentById(id);
        return ApiResponse.success("ok");
    }
}
