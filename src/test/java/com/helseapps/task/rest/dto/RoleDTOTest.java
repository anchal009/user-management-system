package com.helseapps.task.rest.dto;

import com.helseapps.task.rest.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleDTOTest {

    @Test
    public void testRoleDTOConstructor1() {
        Role role = new Role(1L, "USER");

        RoleDTO roleDTO = new RoleDTO(role);

        assertEquals(role.getId(), roleDTO.getId());
        assertEquals(role.getRole(), roleDTO.getRole());
    }

    @Test
    public void testRoleDTOConstructor2() {
        RoleDTO roleDTO = new RoleDTO(1L, "USER");

        assertEquals(Long.valueOf(1L), roleDTO.getId());
        assertEquals("USER", roleDTO.getRole());
    }

    @Test
    public void testEquals() {
        RoleDTO roleDTO = new RoleDTO(1L, "USER");
        RoleDTO roleDTO2 = new RoleDTO(1L, "USER");

        assertTrue(roleDTO.equals(roleDTO));
        assertFalse(roleDTO.equals("WRONG"));
        assertTrue(roleDTO.equals(roleDTO2));
    }

}
