package com.helseapps.task.rest.service;

import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.exception.InvalidRoleDataException;
import com.helseapps.task.rest.exception.InvalidRoleIdentifierException;
import com.helseapps.task.rest.exception.RoleInUseException;
import com.helseapps.task.rest.exception.RoleNotFoundException;
import com.helseapps.task.rest.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Autowired
    @InjectMocks
    private RoleService roleService = new RoleService();

    @Test
    public void given_wrong_roleId_when_getRoleById_throw_InvalidRoleIdentifierException() {
        Assertions.assertThrows(InvalidRoleIdentifierException.class, () -> roleService.getRoleById(null));
    }
    // validateRoleName

    @Test
    public void given_invalid_role_name_when_validateRoleName_throw_InvalidRoleDataException() {
        Assertions.assertThrows(InvalidRoleDataException.class, () -> RoleService.validateRoleName(null));
    }

    @Test
    public void given_empty_role_name_when_validateRoleName_throw_InvalidRoleDataException() {
        Assertions.assertThrows(InvalidRoleDataException.class, () -> RoleService.validateRoleName(""));
    }

    @Test
    public void given_empty_role_name_when_validateRoleName_no_exception_occurs() {
        RoleService.validateRoleName("VALID_ROLE_TEST");
    }

    // createRole

    @Test
    public void given_invalid_role_name_when_createRole_throw_InvalidRoleDataException() {
        Assertions.assertThrows(InvalidRoleDataException.class, () -> roleService.createRole(null));
    }

    // deleteRole

    @Test
    public void given_not_existing_role_when_deleteRole_throw_RoleNotFoundException() {
        Assertions.assertThrows(RoleNotFoundException.class, () -> roleService.deleteRole(1L));
    }

    @Test
    public void given_existing_role_in_use_when_deleteRole_throw_RoleInUseException() {
        given(roleRepository.findById(1L)).willReturn(Optional.of(new Role(1L, "TEST")));
        given(roleRepository.countRoleUsage(1L)).willReturn(10L);

        Assertions.assertThrows(RoleInUseException.class, () -> roleService.deleteRole(1L));
    }

    @Test
    public void given_existing_role_not_in_use_when_deleteRole_Ok() {
        given(roleRepository.findById(1L)).willReturn(Optional.of(new Role(1L, "TEST")));
        given(roleRepository.countRoleUsage(1L)).willReturn(0L);

        roleService.deleteRole(1L);
    }

    // getRoleList

    @Test
    public void calling_getRoleList_then_return_list_of_roles() {
        ArrayList<Role> roleArrayList = new ArrayList<>();
        roleArrayList.add(new Role(1L, "FIRST_ROLE"));
        roleArrayList.add(new Role(2L, "SECOND_ROLE"));

        given(roleRepository.findAll()).willReturn(roleArrayList);

        Iterable<Role> roleIterable = roleService.getRoleList();

        assertNotNull(roleIterable);
    }

}
