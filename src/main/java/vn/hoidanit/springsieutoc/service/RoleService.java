package vn.hoidanit.springsieutoc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.hoidanit.springsieutoc.helper.exception.ResourceAlreadyExistsException;
import vn.hoidanit.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.hoidanit.springsieutoc.model.Role;
import vn.hoidanit.springsieutoc.repository.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;


    public Role createRole(Role inputRole) {
        if (roleRepository.existsByName(inputRole.getName())) {
            throw new ResourceAlreadyExistsException("Role with name " + inputRole.getName() + " already exists");
        }
        return roleRepository.save(inputRole);
    }

    public Page<Role> getAllRole(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(role -> {
                    return new Role(role.getId(), role.getName(), role.getDescription(), null);
                });
    }

    public Role getRoleById(Long id){
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));
    }

    public Role updateRoleById(Long id, Role inputRole) {
        Role roleInDB = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role with id " + id + " not found"));

        if (roleInDB.getName().equals(inputRole.getName())) {
            roleInDB.setDescription(inputRole.getDescription());
        } else {
            if (roleRepository.existsByNameAndIdNot(inputRole.getName(), id)) {
                throw new ResourceAlreadyExistsException("Role with name " + inputRole.getName() + " already exists");
            }
            roleInDB.setName(inputRole.getName());
        }

        return roleRepository.save(roleInDB);
    }

    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }
}
