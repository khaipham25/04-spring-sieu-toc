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

package vn.hoidanit.springsieutoc.controller;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.UserRequestDTO;
import vn.hoidanit.springsieutoc.model.dto.UserResponseDTO;
import vn.hoidanit.springsieutoc.service.UserService;

@RestController
public class UserController {

	// unit test
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/users")
	public ResponseEntity<ApiResponse<UserResponseDTO>> createUser(@Valid @RequestBody User inputUser) {
		UserResponseDTO userInDB = userService.createUser(inputUser);
		return ApiResponse.created(userInDB);
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers(
			@RequestParam(required = false) String role
	) {
		List<UserResponseDTO> users = null;
		if (role != null) {
			users = userService.fetchUsersWithRole(role);
		} else {
			users = userService.fetchUsers();
		}
		return ApiResponse.success(users);
	}



	@GetMapping("/users/{id}")
	public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
		UserResponseDTO user = userService.findUserById(id);
		return ApiResponse.success(user);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<ApiResponse<String>> updateUserById(@PathVariable Long id, @RequestBody UserRequestDTO inputUser) {
		userService.updateUser(id, inputUser);
		return ApiResponse.success("User updated successfully");
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<ApiResponse<String>> deleteUserById(@PathVariable Long id) {
		userService.deleteUserById(id);
		return ApiResponse.success("User deleted successfully");
	}
}
