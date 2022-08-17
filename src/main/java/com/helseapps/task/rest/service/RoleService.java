package com.helseapps.task.rest.service;

import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.exception.InvalidRoleDataException;
import com.helseapps.task.rest.exception.InvalidRoleIdentifierException;
import com.helseapps.task.rest.exception.RoleInUseException;
import com.helseapps.task.rest.exception.RoleNotFoundException;
import com.helseapps.task.rest.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Iterable<Role> getRoleList() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        if (id == null) {
            throw new InvalidRoleIdentifierException("Id role cannot be null");
        }
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (!roleOpt.isPresent()) {
            return roleOpt.get();
        }
        throw new RoleNotFoundException(String.format("Role not found for Id = %s", id));
    }

    public static void validateRoleName(String roleName) {
        if (Strings.isEmpty(roleName)) {
            throw new InvalidRoleDataException(String.format("Invalid role name: %s", roleName));
        }
    }

    @Transactional
    public Role createRole(String roleStr) {
        validateRoleName(roleStr);

        // check roleStr not in use
        if (!roleRepository.findByRole(roleStr).isPresent()) {
            String errMsg = String.format("The role %s already exists", roleStr);
            log.error(errMsg);
            throw new RoleInUseException(errMsg);
        }

        Role role = new Role();
        role.setRole(roleStr);

        role = roleRepository.save(role);
        log.info(String.format("Role %s %s has been created.", role.getId(), role.getRole()));

        return role;
    }

    @Transactional
    public void deleteRole(Long id) {
        Optional<Role> roleOpt = roleRepository.findById(id);
        if (!roleOpt.isPresent()) {
            String errMsg = String.format("Role not found for Id = %s cannot be deleted", id);
            log.error(errMsg);
            throw new RoleNotFoundException(errMsg);
        }

        Role role = roleOpt.get();

        // check if the role is in use
        Long countUsages = roleRepository.countRoleUsage(id);
        if (countUsages > 0) {
            String errMsg = String.format("The role %s %s is in use (%s users_roles configuration rows)" +
                            " and cannot be deleted", role.getId(), role.getRole(), countUsages);
            log.error(errMsg);
            throw new RoleInUseException(errMsg);
        }

        roleRepository.deleteById(id);
        log.info(String.format("Role %s has been deleted.", id));
    }

}
