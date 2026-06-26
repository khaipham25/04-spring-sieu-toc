package vn.hoidanit.springsieutoc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;

    private String content;

    private boolean isApproved;

    private Instant createdAt;

    private Instant updatedAt;

    private Long userId;

    private Long postId;
}