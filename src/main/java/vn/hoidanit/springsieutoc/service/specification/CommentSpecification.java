package vn.hoidanit.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.springsieutoc.model.Comment;
import vn.hoidanit.springsieutoc.model.dto.CommentFilterRequestDTO;

public class CommentSpecification {

    public static Specification<Comment> hasContent(
            CommentFilterRequestDTO commentFilter) {
        return (root, query, cb) -> {
            if (commentFilter.getContent() == null)
                return cb.conjunction();

            return cb.like(cb.lower(root.get("content")),
                    "%" + commentFilter.getContent().toLowerCase() + "%");
        };
    }

    public static Specification<Comment> hasUserId(
            CommentFilterRequestDTO commentFilter) {
        return (root, query, cb) -> {
            if (commentFilter.getUserId() == null)
                return cb.conjunction();

            return cb.equal(root.get("user").get("id"), commentFilter.getUserId());
        };
    }

    public static Specification<Comment> hasPostId(
            CommentFilterRequestDTO commentFilter) {
        return (root, query, cb) -> {
            if (commentFilter.getPostId() == null)
                return cb.conjunction();

            return cb.equal(root.get("post").get("id"), commentFilter.getPostId());
        };
    }

}