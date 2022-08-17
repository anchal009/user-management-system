package com.helseapps.task.rest.service.validation;

import com.helseapps.task.rest.exception.InvalidUserDataException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorTest {

    @Autowired
    @InjectMocks
    private PasswordValidator passwordValidator;

    @Before("")
    public void initTest() {
        passwordValidator = new PasswordValidator();
    }

    @Test
    public void given_null_password_when_checkPassword_throw_InvalidUserDataException() {
        String password = null;
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_empty_password_when_checkPassword_throw_InvalidUserDataException() {
        String password = "";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_too_long_password_when_checkPassword_throw_InvalidUserDataException() {
        String password = "01234567890123456789012345678901234567890123456789012345678901234567890123456789";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_not_valid_password_when_checkPassword_throw_InvalidUserDataException() {
        String password = "aaaa asdasd1234";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    // 1 number, 1 upper case, 1 lower case letter, 1 special char
    @Test
    public void given_invalid_password_less_than_8_chars_when_checkPassword_then_OK() {
        String password = "Anchal";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_invalid_password_with_spaces_when_checkPassword_then_OK() {
        String password = "Anchal test";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_invalid_password_no_uppercase_letter_when_checkPassword_then_OK() {
        String password = "anchal!123";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_invalid_password_no_lowercase_letter_when_checkPassword_then_OK() {
        String password = "ANDREA!123";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_invalid_password_no_spacial_chars_when_checkPassword_then_OK() {
        String password = "ANDREA123";
        Assertions.assertThrows(InvalidUserDataException.class, () -> passwordValidator.checkPassword(password));
    }

    @Test
    public void given_valid_password_when_checkPassword_then_OK() {
        String password = "Anchal!123";
        passwordValidator.checkPassword(password);
    }

}
