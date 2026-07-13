package vn.hoidanit.springsieutoc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentFilterRequestDTO {

    private String content;

    private Long postId;

    private Integer userId;
}