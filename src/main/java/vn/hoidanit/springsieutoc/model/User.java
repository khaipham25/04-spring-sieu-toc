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

package vn.hoidanit.springsieutoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "name không được để trống")
	private String name;

	@NotBlank(message = "email không được để trống")
	private String email;

	@NotBlank(message = "password không được để trống")
//	@JsonIgnore
	private String password;

	@NotBlank(message = "address không được để trống")
	private String address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "role_id")
	private Role role;

	@OneToMany( mappedBy = "user")
	private List<Post> posts;

	@OneToMany( mappedBy = "user")
	private List<Comment> comments;

	@OneToMany(mappedBy = "user")
	private List<RefreshToken> refreshTokens;
}
