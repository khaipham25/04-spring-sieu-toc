package vn.hoidanit.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hoidanit.springsieutoc.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
