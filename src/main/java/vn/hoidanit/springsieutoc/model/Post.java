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

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.springsieutoc.helper.SecurityUtil;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "title không được để trống")
	private String title;

	@NotBlank(message = "content không được để trống")
	@Column(columnDefinition = "MEDIUMTEXT")
	private String content;

	private Instant createdAt;

	private Instant updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany( mappedBy = "post")
	private List<Comment> comments;

	@ManyToMany
	@JoinTable(
			name = "post_tag",
			joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private List<Tag> tags;

	public Post(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}

	// Trước khi post/insert Post thì chạy hàm nafy
	@PrePersist
	public void beforeCreate() {
		this.createdAt = Instant.now();
		this.updatedAt = Instant.now();

		// tức là thay vì nhận user từ post thì ta phải lấy user từ acccessToken lưu vào DB mới chuẩn nhất, chặn hacker gửi 1 request chứa user khác
		if (SecurityUtil.getCurrentIdLogin().isPresent()) {
			User u = new User();
			Long userId = SecurityUtil.getCurrentIdLogin().get();
			u.setId(userId);

			this.user = u;
		}
	}


}