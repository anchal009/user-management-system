package com.helseapps.task.rest.service;

import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.entity.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserTestHelper {

    public static final String TEST_PASSWORD_DECRYPTED = "Test!123";

    // create a test user data
    public static User getUserTestData(Long id, String username, String firstName, String lastName, String email, String phone) {
        User user = new User();
        user.setId(id);

        user.setUsername(username);
        user.setPassword(EncryptionService.encrypt(TEST_PASSWORD_DECRYPTED));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setGender("Male");
        user.setEmail(email);
        user.setPhoneNumber(phone);

        user.setEnabled(true);

        user.setCreated(LocalDateTime.of(2020, 2, 1, 12, 30));
        user.setUpdated(LocalDateTime.of(2020, 2, 1, 16, 45));
        user.setLoginDate(null);

        // add the ADMINISTRATOR role
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(new Role(Role.ADMINISTRATOR, "ADMINISTRATOR"));

        user.setRoles(roleSet);
        return user;
    }

}
