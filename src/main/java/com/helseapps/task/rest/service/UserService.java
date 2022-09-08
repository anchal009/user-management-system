package com.helseapps.task.rest.service;

import com.helseapps.task.rest.dto.UserDTO;
import com.helseapps.task.rest.dto.request.CreateOrUpdateUserDTO;
import com.helseapps.task.rest.dto.request.RegisterUserAccountDTO;
import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.entity.User;
import com.helseapps.task.rest.exception.*;
import com.helseapps.task.rest.repository.RoleRepository;
import com.helseapps.task.rest.repository.UserRepository;
import com.helseapps.task.rest.service.validation.EmailValidator;
import com.helseapps.task.rest.service.validation.PasswordValidator;
import com.helseapps.task.rest.service.validation.PhoneValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;
    private final PhoneValidator phoneValidator;

    public UserService() {
        passwordValidator = new PasswordValidator();
        emailValidator = new EmailValidator();
        phoneValidator = new PhoneValidator();
    }

    public List<UserDTO> getUserPresentationList(String keyword) {
        ArrayList<UserDTO> listDto = new ArrayList<>();
        Iterable<User> list = getUserList(keyword);
        list.forEach(e -> listDto.add(new UserDTO(e)));
        return listDto;
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("User Id cannot be null");
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw new UserNotFoundException(String.format("User not found for Id = %s", id));
    }

    public User getUserByUsername(String username) {
        if (username == null) {
            throw new InvalidUsernameException("username cannot be null");
        }
        return userRepository.findByUsername(username);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            throw new InvalidUsernameException("username or password cannot be null");
        }
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User getUserByEmail(String email) {
        if (email == null) {
            throw new InvalidEmailException("email cannot be null");
        }
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User registerUserAccount(RegisterUserAccountDTO registerUserAccountDTO) {
        if (registerUserAccountDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        checkIfUsernameNotUsed(registerUserAccountDTO.getUsername());
        passwordValidator.checkPassword(registerUserAccountDTO.getPassword());
        emailValidator.checkEmail(registerUserAccountDTO.getEmail());

        checkIfEmailNotUsed(registerUserAccountDTO.getEmail());

        // create the new user account: not all the user information required
        User user = new User();
        user.setUsername(registerUserAccountDTO.getUsername());
        user.setPassword(EncryptionService.encrypt(registerUserAccountDTO.getPassword()));

        user.setFirstName(registerUserAccountDTO.getFirstName());
        user.setLastName(registerUserAccountDTO.getLastName());
        user.setEnabled(true);

        addUserRole(user, Role.ADMINISTRATOR);
        user.setCreated(LocalDateTime.now());

        User userCreated = userRepository.save(user);

        user.setEmail(registerUserAccountDTO.getEmail());
        userCreated = userRepository.save(userCreated);

        log.info(String.format("User %s has been created.", userCreated.getId()));
        return userCreated;
    }

    // check if the username has not been registered
    public void checkIfUsernameNotUsed(String username) {
        User userByUsername = getUserByUsername(username);
            if (userByUsername != null) {
                String msg = String.format("The username %s it's already in use from another user with ID = %s",
                        userByUsername.getUsername(), userByUsername.getId());
                log.error(msg);
            throw new InvalidUserDataException(msg);
        }
    }

    // check if the email has not been registered
    public void checkIfEmailNotUsed(String email) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail != null) {
            String msg = String.format("The email %s it's already in use from another user with ID = %s",
                    userByEmail.getEmail(), userByEmail.getId());
            log.error(msg);
            throw new InvalidUserDataException(String.format("This email %s it's already in use.",
                    userByEmail.getEmail()));
        }
    }

    @Transactional
    public User createUser(CreateOrUpdateUserDTO createUserDTO) {
        if (createUserDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        checkIfUsernameNotUsed(createUserDTO.getUsername());
        checkIfEmailNotUsed(createUserDTO.getEmail());
        passwordValidator.checkPassword(createUserDTO.getPassword());
        emailValidator.checkEmail(createUserDTO.getEmail());
        phoneValidator.checkPhone(createUserDTO.getPhoneNumber());

        // create the user
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setPassword(EncryptionService.encrypt(createUserDTO.getPassword()));

        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setGender(createUserDTO.getGender());
        user.setEmail(createUserDTO.getEmail());

        // date of birth
        user.setBirthDay(createUserDTO.getBirthDay());

        user.setEnabled(true);

        user.setNote(createUserDTO.getNote());
        user.setCreated(LocalDateTime.now());

        // set default user the role
        addUserRole(user, Role.ADMINISTRATOR);

        User userCreated = userRepository.save(user);
        log.info(String.format("User %s has been created.", userCreated.getId()));
        return userCreated;
    }

    public void addUserRole(User user, long roleId) {
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException("Role cannot be null");
        }
        user.getRoles().add(roleOpt.get());
    }

    @Transactional
    public User updateUser(Long id, CreateOrUpdateUserDTO updateUserDTO) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }
        if (updateUserDTO == null) {
            throw new InvalidUserDataException("User account data cannot be null");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("The user with Id = %s doesn't exists", id)));

        // check if the username has not been registered
        User userByUsername = getUserByUsername(updateUserDTO.getUsername());
        if (userByUsername != null) {
            // check if the user's id is different than the actual user
            if (!user.getId().equals(userByUsername.getId())) {
                String msg = String.format("The username %s it's already in use from another user with ID = %s",
                        updateUserDTO.getUsername(), userByUsername.getId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        passwordValidator.checkPassword(updateUserDTO.getPassword());
        emailValidator.checkEmail(updateUserDTO.getEmail());
        phoneValidator.checkPhone(updateUserDTO.getPhoneNumber());

        // check if the new email has not been registered yet
        User userEmail = getUserByEmail(updateUserDTO.getEmail());
        if (userEmail != null) {
            // check if the user's email is different than the actual user
            if (!user.getId().equals(userEmail.getId())) {
                String msg = String.format("The email %s it's already in use from another user with ID = %s",
                        updateUserDTO.getEmail(), userEmail.getId());
                log.error(msg);
                throw new InvalidUserDataException(msg);
            }
        }

        // update the user
        user.setUsername(updateUserDTO.getUsername());

        //  secure the new validated password
        user.setPassword(EncryptionService.encrypt(updateUserDTO.getPassword()));
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());

        // set gender
        user.setGender(updateUserDTO.getGender());

        // date of birth
        user.setBirthDay(updateUserDTO.getBirthDay());

        user.setEnabled(updateUserDTO.isEnabled());
        user.setNote(updateUserDTO.getNote());

        user.setEmail(updateUserDTO.getEmail());
        user.setPhoneNumber(updateUserDTO.getPhoneNumber());

        user.setUpdated(LocalDateTime.now());

        user.setAddress(updateUserDTO.getAddress());

        User userUpdated = userRepository.save(user);
        log.info(String.format("User %s has been updated.", user.getId()));

        return userUpdated;
    }

    public Iterable<User> getUserList(String keyword) {
        if (keyword != null) {
            return userRepository.search(keyword);
        }
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (id == null) {
            throw new InvalidUserIdentifierException("Id cannot be null");
        }

        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }
        userRepository.deleteById(id);
        log.info(String.format("User %s has been deleted.", id));
    }

    @Transactional
    public User login(String username, String password) {
        if ((Strings.isEmpty(username)) || (Strings.isEmpty(password))) {
            throw new InvalidLoginException("Username or Password cannot be null or empty");
        }

        User user = getUserByUsername(username);
        if (user == null) {
            // invalid username
            throw new InvalidLoginException("Invalid username or password");
        }

        log.info(String.format("Login request from %s", username));

        // check the password
        if (EncryptionService.isPasswordValid(password, user.getPassword())) {
            // check if the user is enabled
            if (!user.isEnabled()) {
                // not enabled
                throw new InvalidLoginException("User is not enabled");
            }

            // update the last login timestamp
            user.setLoginDate(LocalDateTime.now());
            userRepository.save(user);

            log.info(String.format("Valid login for %s", username));
        } else {
            throw new InvalidLoginException("Invalid username or password");
        }
        return user;
    }

    // add or remove a role on user

    @Transactional
    public User addRole(Long id, Long roleId) {
        // check user
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }
        User user = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        user.getRoles().add(role);
        user.setUpdated(LocalDateTime.now());

        userRepository.save(user);
        log.info(String.format("Added role %s on user id = %s", role.getRole(), user.getId()));

        return user;
    }

    @Transactional
    public User removeRole(Long id, Long roleId) {
        // check user
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent()) {
            throw new UserNotFoundException(String.format("User not found with Id = %s", id));
        }
        User user = userOpt.get();

        // check role
        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (!roleOpt.isPresent()) {
            throw new RoleNotFoundException(String.format("Role not found with Id = %s", roleId));
        }

        Role role = roleOpt.get();

        user.getRoles().remove(role);
        user.setUpdated(LocalDateTime.now());

        userRepository.save(user);
        log.info(String.format("Removed role %s on user id = %s", role.getRole(), user.getId()));

        return user;
    }

}
