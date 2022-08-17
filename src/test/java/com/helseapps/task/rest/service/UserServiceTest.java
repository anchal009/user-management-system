package com.helseapps.task.rest.service;

import com.helseapps.task.rest.dto.UserDTO;
import com.helseapps.task.rest.dto.request.CreateOrUpdateUserDTO;
import com.helseapps.task.rest.dto.request.RegisterUserAccountDTO;
import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.entity.User;
import com.helseapps.task.rest.exception.*;
import com.helseapps.task.rest.repository.RoleRepository;
import com.helseapps.task.rest.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.helseapps.task.rest.service.UserTestHelper.getUserTestData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Autowired
    @InjectMocks
    private UserService userService = new UserService();

    @Test
    public void given_existing_users_when_getUserPresentationList_return_validList() {
        User user1 = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");
        User user2= getUserTestData(2L, "kim", "Kim",
                "Mohn", "kim@gmail.com", "+4788997766");
        User user3 = getUserTestData(3L, "aparna", "Aparna",
                "Agrawal", "aparna@gmail.com", "+4793992233");

        List<User> list = Arrays.asList(user1, user2, user3);

        given(userService.getUserList(null)).willReturn(list);

        List<UserDTO> userDTOList = userService.getUserPresentationList(null);

        assertNotNull(userDTOList);
        assertEquals(3, userDTOList.size());

        // take the second element to test the DTO content
        UserDTO userDTO = userDTOList.get(1);

        assertEquals(Long.valueOf(2L) , userDTO.getId());
        assertEquals("kim" , userDTO.getUsername());
        assertEquals("Kim" , userDTO.getFirstName());
        assertEquals("Mohn" , userDTO.getLastName());

        assertEquals("kim@gmail.com" , userDTO.getEmail());
        assertEquals("+4788997766" , userDTO.getPhoneNumber());
    }

    @Test
    public void given_null_user_id_when_getUserById_throw_exception() {
        Assertions.assertThrows(InvalidUserIdentifierException.class, () -> userService.getUserById(null));
    }

    @Test
    public void given_null_username_when_getUserByUsername_return_user() {
        Assertions.assertThrows(InvalidUsernameException.class, () -> userService.getUserByUsername(null));
    }

    @Test
    public void given_existing_username_when_getUserByUsername_return_user() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByUsername("anchal")).willReturn(userDataForTest);

        User user = userService.getUserByUsername("anchal");

        assertNotNull(user);
        assertEquals(Long.valueOf(1L), user.getId());
        assertEquals("anchal", user.getUsername());
        assertEquals("Anchal", user.getFirstName());
        assertEquals("Agrawal", user.getLastName());
        assertEquals("anchal@gmail.com", user.getEmail());
        assertEquals("+4799887766", user.getPhoneNumber());
    }

    @Test
    public void given_existing_email_when_getUserByEmail_return_user() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByEmail("anchal@gmail.com")).willReturn(userDataForTest);

        User user = userService.getUserByEmail("anchal@gmail.com");

        assertNotNull(user);
        assertEquals(Long.valueOf(1L), user.getId());
        assertEquals("anchal", user.getUsername());
        assertEquals("Anchal", user.getFirstName());
        assertEquals("Agrawal", user.getLastName());
        assertEquals("anchal@gmail.com", user.getEmail());
        assertEquals("+4799887766", user.getPhoneNumber());
    }

    @Test
    public void given_invalid_email_getUserByEmail_throw_InvalidUserEmailException() {
        Assertions.assertThrows(InvalidEmailException.class, () -> userService.getUserByEmail(null));
    }

    @Test
    public void given_null_CreateUserAccountDTO_when_createNewUserAccount_throw_InvalidUserDataException() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.registerUserAccount(null));
    }

    @Test
    public void given_already_existing_username_when_createNewUserAccount_throw_InvalidUserDataException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByUsername("anchal")).willReturn(userDataForTest);

        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .firstName("Anchal")
                .lastName("Agrawal")
                .email("anchal@gmail.com")
                .username("anchal")
                .password(UserTestHelper.TEST_PASSWORD_DECRYPTED)
                .build();

        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.registerUserAccount(registerUserAccountDTO));
    }

    @Test
    public void given_existing_email_when_createNewUserAccount_throw_InvalidUserDataException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByEmail("anchal@gmail.com")).willReturn(userDataForTest);

        // existing email
        RegisterUserAccountDTO registerUserAccountDTO = RegisterUserAccountDTO.builder()
                .firstName("Anchal")
                .password("Anchal!123")
                .lastName("Agrawal")
                .email("anchal@gmail.com")
                .username("anchal")
                .password(UserTestHelper.TEST_PASSWORD_DECRYPTED)
                .build();

        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.registerUserAccount(registerUserAccountDTO));
    }

    @Test
    public void given_invalidRole_when_setUserRole_throw_RoleNotFoundException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        // role doesn't exists
        Assertions.assertThrows(RoleNotFoundException.class, () ->userService.addUserRole(userDataForTest, 1));
    }

    @Test
    public void given_valid_role_id_when_setUserRole_returnUser() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(roleRepository.findById(Role.USER)).willReturn(Optional.of(new Role(Role.USER, "USER")));

        userService.addUserRole(userDataForTest, Role.USER);

        assertNotNull(userDataForTest);

        Role roleUser = new Role(Role.USER, "USER");
        assertTrue(userDataForTest.getRoles().contains(roleUser));

        assertEquals("anchal", userDataForTest.getUsername());
        assertEquals("Anchal", userDataForTest.getFirstName());
        assertEquals("Agrawal", userDataForTest.getLastName());
        assertTrue(userDataForTest.isEnabled());

        assertEquals("anchal@gmail.com", userDataForTest.getEmail());
        assertEquals("+4799887766", userDataForTest.getPhoneNumber());
    }

    @Test
    public void given_invalid_CreateOrUpdateUserDTO_when_createUser_throw_InvalidUserDataException() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.createUser(null));
    }

    @Test
    public void given_already_registered_username_when_createUser_throw_InvalidUserDataException() {
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder().username("anchal").build();

        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByUsername("anchal")).willReturn(userDataForTest);

        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.createUser(createOrUpdateUserDTO));
    }

    @Test
    public void given_already_registered_email_when_createUser_throw_InvalidUserDataException() {
        // existing email
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .firstName("Anchal")
                .lastName("Agrawal")
                .email("anchal@gmail.com")
                .gender("MALE")
                .username("anchal")
                .phoneNumber("+4799887766")
                .enabled(true).build();

        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByEmail("anchal@gmail.com")).willReturn(userDataForTest);

        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.createUser(createOrUpdateUserDTO));
    }

    @Test
    public void given_invalid_userId_when_updateUser_throw_InvalidUserIdentifierException() {
        Assertions.assertThrows(InvalidUserIdentifierException.class, () -> userService.updateUser(null, new CreateOrUpdateUserDTO()));
    }

    @Test
    public void given_invalid_createOrUpdateUserDTO_when_updateUser_throw_InvalidUserDataException() {
        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.updateUser(1L, null));
    }

    @Test
    public void given_not_existing_userId_when_updateUser_throw_UserNotFoundException() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, new CreateOrUpdateUserDTO()));
    }

    @Test
    public void given_existing_username_when_updateUser_throw_InvalidUserDataException() {
        // setting an existing username
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .firstName("Kim")
                .lastName("Mohn")
                .email("kim@gmail.com")
                .gender("MALE")
                .username("kim")
                .phoneNumber("+4788997766")
                .enabled(true)
                .build();

        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");
        User userDataForTest2 = getUserTestData(2L, "kim", "Kim",
                "Rossi", "marco.test@gmail.com", "+4788997766");

        given(userRepository.findById(2L)).willReturn(Optional.of(userDataForTest2));
        given(userRepository.findByUsername("kim")).willReturn(userDataForTest);

        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.updateUser(2L, createOrUpdateUserDTO));
    }

    @Test
    public void given_existing_email_when_updateUser_throw_InvalidUserDataException() {
        // setting an existing email
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .firstName("Kim")
                .lastName("Mohn")
                .email("kim@gmail.com")
                .gender("MALE")
                .username("kim")
                .password("Test!123")
                .phoneNumber("+4788997766")
                .enabled(true)
                .build();

        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");
        User userDataForTest2 = getUserTestData(2L, "kim", "Kim",
                "Rossi", "marco.test@gmail.com", "+4788997766");

        given(userRepository.findById(2L)).willReturn(Optional.of(userDataForTest2));
        given(userRepository.findByEmail("kim@gmail.com")).willReturn(userDataForTest);

        Assertions.assertThrows(InvalidUserDataException.class, () -> userService.updateUser(2L, createOrUpdateUserDTO));
    }

    @Test
    public void given_existing_user_when_updatedUser_return_userUpdated() {
        // correct user data, update the phone number
        CreateOrUpdateUserDTO createOrUpdateUserDTO = CreateOrUpdateUserDTO.builder()
                .firstName("Anchal")
                .lastName("Agrawal")
                .email("anchal@gmail.com")
                .gender("MALE")
                .username("anchal")
                .password("Test!123")
                .phoneNumber("+3539988776655")
                .enabled(true)
                .address("via roma 3")
                .build();

        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        userService.updateUser(1L, createOrUpdateUserDTO);
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void given_null_userId_when_deleteUserById_throw_InvalidUserIdentifierException() {
        Assertions.assertThrows(InvalidUserIdentifierException.class, () -> userService.deleteUserById(null));
    }

    @Test
    public void given_not_existing_userId_when_deleteUserById_throw_UserNotFoundException() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));
    }

    @Test
    public void given_null_username_and_null_password_when_login_throw_InvalidLoginException() {
        Assertions.assertThrows(InvalidLoginException.class, () -> userService.login(null, null));
    }

    @Test
    public void given_null_username_login_when_login_throw_InvalidLoginException() {
        Assertions.assertThrows(InvalidLoginException.class, () -> userService.login(null, "WRONG_PWD"));
    }

    @Test
    public void given_null_password_login_when_login_throw_InvalidLoginException() {
        Assertions.assertThrows(InvalidLoginException.class, () -> userService.login("WRONG", null));
    }

    @Test
    public void given_invalid_login_when_login_throw_InvalidLoginException() {
        Assertions.assertThrows(InvalidLoginException.class, () -> userService.login("WRONG", "WRONG_PWD"));
    }

    @Test
    public void given_valid_login_when_login_return_User() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByUsername("anchal")).willReturn(userDataForTest);

        User user = userService.login("anchal", UserTestHelper.TEST_PASSWORD_DECRYPTED);

        assertNotNull(user);
        assertEquals("anchal", user.getUsername());
    }

    @Test
    public void given_invalid_login2_when_login_throw_InvalidLoginException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findByUsername("anchal")).willReturn(userDataForTest);

        Assertions.assertThrows(InvalidLoginException.class, () -> userService.login("anchal", "WRONG_PWD"));
    }

    @Test
    public void given_not_enabled_login_when_login_throw_InvalidLoginException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        userDataForTest.setEnabled(false);

        given(userRepository.findByUsername("anchal")).willReturn(userDataForTest);

        Assertions.assertThrows(InvalidLoginException.class, () -> userService.login("anchal", UserTestHelper.TEST_PASSWORD_DECRYPTED));
    }

    // tests add role on User
    @Test
    public void given_notExistingUserId_when_addRole_throw_UserNotFoundException() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.addRole(99L, 2L));
    }

    @Test
    public void given_existingUserId_notExistingRoleId_when_addRole_throw_RoleNotFoundException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        Assertions.assertThrows(RoleNotFoundException.class, () -> userService.addRole(1L, 99L));
    }

    @Test
    public void given_validUserAndRoleIds_when_addRole_returnUser() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        Role roleAdmin = new Role(Role.ADMINISTRATOR, "Administrator");

        given(roleRepository.findById(2L)).willReturn(Optional.of(roleAdmin));

        User user = userService.addRole(1L, 2L);

        assertNotNull(user);

        // check the new added role
        Set<Role> roleSet = user.getRoles();

        assertNotNull(roleSet);
        assertEquals(1, roleSet.size());
        assertTrue(roleSet.contains(roleAdmin));
    }

    // test remove role from User
    @Test
    public void given_notExistingUserId_when_removeRole_throw_UserNotFoundException() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.removeRole(99L, 2L));
    }

    @Test
    public void given_existingUserId_notExistingRoleId_when_removeRole_throw_RoleNotFoundException() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));

        Assertions.assertThrows(RoleNotFoundException.class, () -> userService.removeRole(1L, 99L));
    }

    @Test
    public void given_validUserAndRoleIds_when_removeRole_returnUser() {
        User userDataForTest = getUserTestData(1L, "anchal", "Anchal",
                "Agrawal", "anchal@gmail.com", "+4799887766");

        Role roleAdmin = new Role(Role.ADMINISTRATOR, "Administrator");
        userDataForTest.getRoles().add(roleAdmin);

        given(userRepository.findById(1L)).willReturn(Optional.of(userDataForTest));
        given(roleRepository.findById(2L)).willReturn(Optional.of(roleAdmin));

        User user = userService.removeRole(1L, 2L);

        assertNotNull(user);

        // check the remove role
        Set<Role> roleSet = user.getRoles();

        assertNotNull(roleSet);
        assertEquals(0, roleSet.size());
        assertFalse(roleSet.contains(roleAdmin));
    }
}
