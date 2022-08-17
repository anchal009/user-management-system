package com.helseapps.task.rest.service.validation;

import com.helseapps.task.rest.exception.InvalidUserDataException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {
    @Autowired
    @InjectMocks
    private EmailValidator emailValidator;

    @Before("")
    public void initTest() {
        emailValidator = new EmailValidator();
    }

    @Test
    public void given_null_email_when_checkEmail_throw_InvalidUserDataException() {
        String email = null;
        Assertions.assertThrows(InvalidUserDataException.class, () -> emailValidator.checkEmail(email));
    }

    @Test
    public void given_empty_email_when_checkEmail_throw_InvalidUserDataException() {
        String email = "";
        Assertions.assertThrows(InvalidUserDataException.class, () -> emailValidator.checkEmail(email));
    }

    @Test
    public void given_invalid_email_when_checkEmail_throw_InvalidUserDataException() {
        String email = "@gmail.com";
        Assertions.assertThrows(InvalidUserDataException.class, () -> emailValidator.checkEmail(email));
    }

    @Test
    public void given_valid_email_when_checkEmail_OK() {
        String email = "testEmail@gmail.com";
        emailValidator.checkEmail(email);
    }

}
