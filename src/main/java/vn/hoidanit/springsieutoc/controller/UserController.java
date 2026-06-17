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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.service.UserService;

@Controller
public class UserController {

	// unit test
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/hoidanit")
	public String eric(Model model) {

		// gọi service, gọi database => lấy data
		model.addAttribute("name", "hoidanit with eric"); // x <= y

		return "hello";
	}

	@GetMapping("/user")
	public String showUser(Model model) {

		// 1. HARDCODE danh sách User (thay thế cho việc gọi Service/Database)
//		List<User> userList = Arrays.asList(new User("Nguyễn Văn A", "a.nguyen@example.com", "Hà Nội"),
//				new User("Trần Thị B", "b.tran@example.com", "TP.HCM"),
//				new User("Lê Văn C", "c.le@example.com", "Đà Nẵng"));
//		model.addAttribute("users", userList); // x <= y

		// cách làm sai 1
//		UserServiceWrong1 us1 = new UserServiceWrong1();
//		List<User> userListWrong1 = us1.fetchUsers();
//		model.addAttribute("users", userListWrong1); // x <= y

		// cách làm sai 2
//		List<User> userListWrong2 = UserServiceWrong2.fetchUsers();
//		model.addAttribute("users", userListWrong2); // x <= y

		// cách làm đúng, sử dụng DI

		List<User> userList = this.userService.fetchUsers();
		model.addAttribute("users", userList); // x <= y

		return "/user/show";
	}

	@GetMapping("/user/create")
	public String getCreatePage(Model model) {
		model.addAttribute("user", new User());
		return "/user/create";
	}

	@PostMapping("/user/create")
	public String postCreatePage(@Valid @ModelAttribute User createUser, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "/user/create";
		}

		this.userService.createUser(createUser);
		return "redirect:/user";
	}

	@GetMapping("/user/{id}")
	public String getUpdateUserPage(Model model, @PathVariable int id) {
		// select * from user where id = input
		User updateUser = this.userService.findUserById(id);

		model.addAttribute("user", updateUser);
		model.addAttribute("id", id);
		return "/user/update";
	}

	@PostMapping("/user/update")
	public String postUpdatePage(@Valid @ModelAttribute User updateUser, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("user", updateUser);
			model.addAttribute("id", updateUser.getId());
			return "/user/update";
		}

		this.userService.updateUser(updateUser);
		return "redirect:/user";
	}

	@PostMapping("/user/delete/{id}")
	public String postDeleteUser(Model model, @PathVariable int id) {
		this.userService.deleteUserById(id);
		return "redirect:/user";
	}

	@GetMapping("/admin")
	public String showAdmin() {

		return "/admin/show";
	}
}
