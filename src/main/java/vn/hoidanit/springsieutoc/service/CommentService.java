package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.dto.CommentResponseDTO;
import vn.hoidanit.springsieutoc.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentResponseDTO convertCommentToDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getId());
        dto.setPostId(comment.getPost().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        dto.setApproved(comment.isApproved());
        return dto;
    }

    public Comment createComment(Comment inputComment) {
        return commentRepository.save(inputComment);
    }

    public List<CommentResponseDTO> fetchComments() {
        return this.commentRepository.findAll().stream().map(comment -> {
            return convertCommentToDTO(comment);
        }).collect(Collectors.toList());
    }

    public CommentResponseDTO fetchCommentById(Long id) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return convertCommentToDTO(comment);
    }

    public CommentResponseDTO updateCommentById(Long id, Comment inputComment) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.setApproved(inputComment.isApproved());

        return convertCommentToDTO(this.commentRepository.save(comment));
    }

    public void deleteCommentById(Long id) {
        this.commentRepository.deleteById(id);
    }
}
