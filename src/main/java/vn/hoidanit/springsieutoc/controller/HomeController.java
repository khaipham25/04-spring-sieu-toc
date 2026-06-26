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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.springsieutoc.service.UserService;

@RestController
public class HomeController {

	private final UserService userService;

	public HomeController(UserService userService) {
		this.userService = userService;
	}

	@Value("${hoidanit.secret:default-value}")
	private String name;

	@GetMapping("/")
	public String index() {
		return "Hello World from Spring Boot - @hoidanit devtool: " + name;
	}

	@GetMapping("/eric")
	public ResponseEntity<String> demoAPI() {
//		return new ResponseEntity<>("hello eric", HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body("Hello World from Spring Boot");
	}
}
