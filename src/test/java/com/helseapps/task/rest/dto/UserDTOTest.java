package com.helseapps.task.rest.dto;

import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    @Test
    public void userDTOTestConstructor1() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setFirstName("testName");
        user.setLastName("testSurname");
        user.setEnabled(true);
        user.setGender("Male");
        user.setEnabled(true);
        user.setEmail("email");
        user.setPhoneNumber("+4799887766");
        user.setNote("Test note");
        LocalDateTime creationDt = LocalDateTime.of(2020, 2, 1, 12, 30);
        user.setCreated(creationDt);

        LocalDateTime updatedDt = LocalDateTime.of(2020, 2, 1, 16, 45);
        user.setUpdated(updatedDt);

        Role roleUser = new Role(Role.USER, "USER");
        Role roleAdmin = new Role(Role.ADMINISTRATOR, "ADMINISTRATOR");

        user.getRoles().add(roleAdmin);
        user.getRoles().add(roleUser);

        UserDTO userDTO = new UserDTO(user);

        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getUsername(), user.getUsername());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());

        assertTrue(userDTO.isEnabled());

        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(userDTO.getNote(), user.getNote());

        assertEquals(userDTO.isEnabled(), user.isEnabled());

        assertEquals(creationDt, userDTO.getCreated());
        assertEquals(updatedDt, userDTO.getUpdated());
        assertNull(userDTO.getLoginDate());

        assertNotNull(user.getRoles());

        Set<Role> rolesTest = user.getRoles();

        assertTrue(rolesTest.contains(roleUser));
        assertTrue(rolesTest.contains(roleAdmin));
    }
}
