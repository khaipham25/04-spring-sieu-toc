/*
 * Author: Hỏi Dân IT - @hoidanit 
 *
 * This source code is developed for the course
 * "Java Spring Siêu Tốc - Tự Học Java Spring Từ Số 0 Dành Cho Beginners từ A tới Z".
 * It is intended for educational purposes only.
 * Unauthorized distribution, reproduction, or modification is strictly prohibited.
 *
 * Copyright (c) 2025 Hỏi Dân IT. All Rights Reserved.
 */

package vn.hoidanit.springsieutoc.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.hoidanit.springsieutoc.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	Optional<User> findByName(String name);

	Optional<User> findByNameAndEmail(String name, String email);

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

    List<User> findByRole_Name(String roleName);

//	@Query("select u from User u")
//	List<User> findAllUsers();
//
//	@Query("""
//    		select u from User u
//    		where lower(u.username) like lower(concat('%', :kw, '%'))
//		""")
//	List<User> searchByUsername(@Param("kw") String keyword);
//
//	@NativeQuery(
//			value = "SELECT * FROM users WHERE status = :status"
//	)
//	List<User> findByStatus(String status);

//	@EntityGraph(attributePaths = "role")
//    Page<User> findAll(Specification<User> specs, Pageable pageable);


}
