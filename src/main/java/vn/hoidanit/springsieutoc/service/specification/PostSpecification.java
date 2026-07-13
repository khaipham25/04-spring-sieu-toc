package vn.hoidanit.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import vn.hoidanit.springsieutoc.model.Post;
import vn.hoidanit.springsieutoc.model.Tag;
import vn.hoidanit.springsieutoc.model.dto.PostFilterRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.UserFilterRequestDTO;

public class PostSpecification {

    public static Specification<Post> hasTitle(PostFilterRequestDTO postFilter) {
        return (root, query, cb) -> {
            if (postFilter.getTitle() == null)
                return cb.conjunction();

            return cb.like(cb.lower(root.get("title")),
                    "%" + postFilter.getTitle().toLowerCase() + "%");
        };
    }

    public static Specification<Post> hasUserId(PostFilterRequestDTO postFilter) {
        return (root, query, cb) -> {
            if (postFilter.getUserId() == null)
                return cb.conjunction();

            return cb.equal(root.get("user").get("id"), postFilter.getUserId());
        };
    }

    public static Specification<Post> hasTagsIn(PostFilterRequestDTO postFilter) {
        return (root, query, cb) -> {
            if (postFilter.getTags() == null || postFilter.getTags().isEmpty())
                return cb.conjunction();

            Join<Post, Tag> tagJoin = root.join("tags", JoinType.INNER);

            // truyen vao arraylist
            return cb.in(tagJoin.get("name")).value(postFilter.getTags());
        };
    }

    public static Specification<Post> createdAtBetween(PostFilterRequestDTO postFilter) {
        return (root, query, cb) -> {
            if (postFilter.getFromCreatedAt() == null
                    || postFilter.getToCreatedAt() == null)
                return cb.conjunction();

            return cb.between(root.get("createdAt"), postFilter.getFromCreatedAt(),
                    postFilter.getToCreatedAt());
        };
    }

    public static Specification<Post> updatedAtBetween(PostFilterRequestDTO postFilter) {
        return (root, query, cb) -> {
            if (postFilter.getFromUpdatedAt() == null
                    || postFilter.getToUpdatedAt() == null)
                return cb.conjunction();

            return cb.between(root.get("updatedAt"), postFilter.getFromUpdatedAt(),
                    postFilter.getToUpdatedAt());
        };
    }

}
