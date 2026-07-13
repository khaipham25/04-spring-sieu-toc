package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.helper.PageResponse;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.service.TagService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/tags")
    public ResponseEntity<ApiResponse<Tag>> createTag(@Valid @RequestBody Tag inputTag) {
        Tag tagInDB = tagService.createTag(inputTag);
        return ApiResponse.created(tagInDB);
    }

    @GetMapping("/tags")
    public ResponseEntity<?> getAllTag(Pageable pageable) {
        Page<Tag> tags = tagService.getAllTag(pageable);
        return ApiResponse.success(PageResponse.from(tags));
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<ApiResponse<Tag>> getTagById(@PathVariable Long id) {
        Tag tag = tagService.findTagById(id);
        return ApiResponse.success(tag);
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<ApiResponse<String>> updateTagById( @RequestBody Tag updatedTag, @PathVariable Long id) {
        updatedTag.setId(id);
        tagService.updateTag(updatedTag);
        return ApiResponse.success("Update Tag successfully");
    }

    @DeleteMapping("/tags/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTagById(@PathVariable Long id) {
        tagService.deleteTagById(id);
        return ApiResponse.success("Tag deleted successfully");
    }
}
