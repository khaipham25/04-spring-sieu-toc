package vn.hoidanit.springsieutoc.service.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.UserFilterRequestDTO;

public class UserSpecification {

    public static Specification<User> hasName(UserFilterRequestDTO userFilterRequestDTO) {
        return (root, query, cb) -> {
            if (userFilterRequestDTO.getName() == null ) return cb.conjunction();
            // ko lafm thế này thì nó query where name = null luôn đó
            return cb.equal(root.get("name"), userFilterRequestDTO.getName());
        };
    }

    public static Specification<User> hasEmail(UserFilterRequestDTO userFilterRequestDTO) {
        return (root, query, cb) -> {
            if (userFilterRequestDTO.getEmail() == null ) return cb.conjunction();
            // ko lafm thế này thì nó query where name = null luôn đó
            return cb.equal(root.get("email"), userFilterRequestDTO.getEmail());
        };
    }

    public static Specification<User> hasAddress(UserFilterRequestDTO userFilterRequestDTO) {
        return (root, query, cb) -> {
            if (userFilterRequestDTO.getAddress() == null ) return cb.conjunction();
            // ko lafm thế này thì nó query where name = null luôn đó
            return cb.equal(root.get("address"), userFilterRequestDTO.getAddress());
        };
    }

    public static Specification<User> hasRoleName(UserFilterRequestDTO userFilterRequestDTO) {
        return (root, query, cb) -> {
            if (userFilterRequestDTO.getRoleName() == null ) return cb.conjunction();
            // ko lafm thế này thì nó query where name = null luôn đó
            Join<User, Role> roleJoin = root.join("role", JoinType.INNER);
            return cb.equal(cb.lower(roleJoin.get("name")), userFilterRequestDTO.getRoleName().toUpperCase());

        };
    }
}
