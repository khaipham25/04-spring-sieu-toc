package vn.hoidanit.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.springsieutoc.helper.ApiResponse;
import vn.hoidanit.springsieutoc.helper.PageResponse;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.service.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody Role inputRole) {
        Role role = roleService.createRole(inputRole);
        return ApiResponse.created(role);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAllRole(Pageable pageable) {
        Page<Role> roles = roleService.getAllRole(pageable);
        return ApiResponse.success(PageResponse.from(roles));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<Role>> getRoleById(@PathVariable Long id) {
        Role roleInDB = roleService.getRoleById(id);
        return ApiResponse.success(roleInDB);
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<String>> updateRoleById(@PathVariable Long id,@Valid @RequestBody Role inputRole) {
        roleService.updateRoleById(id , inputRole);
        return ApiResponse.success("Role updated successfully");
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<String>> deleteRoleById(@PathVariable Long id) {
        roleService.deleteRoleById(id);
        return ApiResponse.success("Role deleted successfully");
    }
}
