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

package vn.hoidanit.springsieutoc.service;

import java.util.Arrays;
import java.util.List;

import vn.hoidanit.springsieutoc.model.User;

public class UserServiceWrong2 {

	public static List<User> fetchUsers() {
		// Logic fetch user/kết nối DB thực tế sẽ ở đây
		List<User> userList = Arrays.asList(new User(1, "wrong Nguyễn Văn A", "a.nguyen@example.com", "Hà Nội"),
				new User(2, "Trần Thị B", "b.tran@example.com", "TP.HCM"),
				new User(3, "Lê Văn C", "c.le@example.com", "Đà Nẵng"));

		return userList;
	}
}
