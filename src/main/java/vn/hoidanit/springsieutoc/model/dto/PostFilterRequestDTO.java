package vn.hoidanit.springsieutoc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostFilterRequestDTO {

    private String title;

    private Integer userId;

    private List<String> tags;

    private Instant fromCreatedAt;

    private Instant toCreatedAt;

    private Instant fromUpdatedAt;

    private Instant toUpdatedAt;
}