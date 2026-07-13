package vn.hoidanit.springsieutoc.model.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;

    private String title;

    private String content;

    private List<OutputTag> tags;

    private Instant createdAt;
    private Instant updatedAt;
    private String authorName;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OutputTag {
        private Long id;

        private String name;
    }
}
