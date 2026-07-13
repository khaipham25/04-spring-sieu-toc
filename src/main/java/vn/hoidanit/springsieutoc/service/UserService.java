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
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistsException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.model.User;
import vn.hoidanit.springsieutoc.model.dto.*;
import vn.hoidanit.springsieutoc.repository.RoleRepository;
import vn.hoidanit.springsieutoc.repository.UserRepository;
import vn.hoidanit.springsieutoc.service.specification.UserSpecification;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public List<UserResponseDTO> fetchUsersWithRole(String roleName) {
		List<UserResponseDTO> userList = this.userRepository.findByRole_Name(roleName).stream()
				.map(user -> UserResponseDTO.builder()
						.id(user.getId())
						.email(user.getEmail())
						.name(user.getName())
						.address(user.getAddress())
						.role(new RoleResponseDTO(
								user.getRole().getId(),
								user.getRole().getName()))
						.build())
				.collect(Collectors.toList());

		return userList;
	}

	public Page<UserResponseDTO> fetchUsers(Pageable pageable, UserFilterRequestDTO userFilterRequestDTO) {
		// Logic fetch user/kết nối DB thực tế sẽ ở đây

		// build filter
		Specification<User> specs = Specification
                .allOf(
                        // dieu kien tim kiem
                        UserSpecification.hasName(userFilterRequestDTO),
                        UserSpecification.hasEmail(userFilterRequestDTO),
                        UserSpecification.hasAddress(userFilterRequestDTO),
                        UserSpecification.hasRoleName(userFilterRequestDTO)

                );

		Page<UserResponseDTO> userList = this.userRepository.findAll(specs, pageable).map(user ->
				UserResponseDTO.builder()
						.id(user.getId())
						.name(user.getName())
						.email(user.getEmail())
						.address(user.getAddress())
						.role(new RoleResponseDTO(user.getRole().getId(), user.getRole().getName()))
						.build());
		// select * from users

		return userList;
	}

	public UserResponseDTO convertUserToUserDTO(User user) {
//		UserResponseDTO userDTO = new UserResponseDTO();
//		userDTO.setId(user.getId());
//		userDTO.setEmail(user.getEmail());
//		return userDTO;

		RoleResponseDTO userRole = new RoleResponseDTO(user.getRole().getId(), user.getRole().getName());

		return UserResponseDTO.builder()
				.id(user.getId())
				.name(user.getName())
				.email(user.getEmail())
				.address(user.getAddress())
				.role(userRole)
				.build();
	}

	public UserResponseDTO createUser(User user) {
		// save user to database
		// check email
		// existBy tối ưu bộ nhớ hơn cách dùng findBy
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new ResourceAlreadyExistsException("Email already exists "+ user.getEmail());
		}

		// check role
		// to do
		Long roleId = user.getRole().getId();
		String roleName = user.getRole().getName();
		Role roleInDB = roleRepository.findByIdOrName(roleId, roleName)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		// hash password
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		user.setRole(roleInDB);

		userRepository.save(user);

		return convertUserToUserDTO(user);

	}

	public UserResponseDTO findUserById(Long id) {
		User userInDB = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		 return convertUserToUserDTO(userInDB);
	}

	public void updateUser(Long id, UserRequestDTO inputUser) {
		User userInDB = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (inputUser.getRole() != null) {
			// update role
			userInDB.setRole(inputUser.getRole());
		}
		userInDB.setName(inputUser.getName());
		userInDB.setAddress(inputUser.getAddress());

		userRepository.save(userInDB);

	}

	public void deleteUserById(Long id) {
		User userInDB = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found."));

		if (userInDB.getEmail().equals("user@example.com")
				|| userInDB.getEmail().equals("admin@example.com")) {

			throw new ResourceNotFoundException(
					"Xóa rồi, lấy gì mà test @.@ :" + userInDB.getEmail());
		}

		this.userRepository.deleteById(id);
	}


	public User findUserByEmail(String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email not found from service"));

	}

	public void registerNewUser(RegisterRequestDTO inputUser) {
		// check email
		if (this.userRepository.existsByEmail(inputUser.getEmail())) {
			throw new ResourceAlreadyExistsException(
					"Email đã tồn tại: " + inputUser.getEmail());
		}

		// hardcode role = USER
		Role userRole = this.roleRepository.findByIdOrName(null, "USER")
				.orElseThrow(() -> new ResourceNotFoundException("USER Role not found"));

		String hashPassword = this.passwordEncoder.encode(inputUser.getPassword());

		User user = new User();
		user.setPassword(hashPassword);
		user.setRole(userRole);
		user.setAddress(inputUser.getAddress());
		user.setEmail(inputUser.getEmail());
		user.setName(inputUser.getName());

		userRepository.save(user);
	}
}
