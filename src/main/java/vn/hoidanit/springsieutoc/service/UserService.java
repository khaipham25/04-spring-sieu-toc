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

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> fetchUsers() {
		// Logic fetch user/kết nối DB thực tế sẽ ở đây

		List<User> userList = this.userRepository.findAll();
		// select * from users

		return userList;
	}

	public void createUser(User user) {
		// save user to database
		// orm
		this.userRepository.save(user);

	}

	public User findUserById(int id) {
		Optional<User> userOpt = this.userRepository.findById(id);
		return userOpt.get();
	}

	public void updateUser(User inputUser) {
		User currentUserInDB = this.findUserById(inputUser.getId());
		if (currentUserInDB != null) {
			currentUserInDB.setName(inputUser.getName());
			currentUserInDB.setEmail(inputUser.getEmail());
			currentUserInDB.setAddress(inputUser.getAddress());

			this.userRepository.save(currentUserInDB);
		}
	}

	public void deleteUserById(int id) {
		this.userRepository.deleteById(id);
	}

	public void testJPA() {
		System.out.println("call jpa");
//		Optional<User> userOpt = this.userRepository.findByName("eric1");
		Optional<User> userOpt = this.userRepository.findByNameAndEmail("eric1", "hoidanit@example.com2");

		System.out.println(userOpt.get());
	}
}
