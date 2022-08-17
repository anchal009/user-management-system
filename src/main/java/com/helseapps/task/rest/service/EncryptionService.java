package com.helseapps.task.rest.service;

import java.util.Base64;

/**
 * Provides a set of methods to encrypt or decrypt a String information.
 *
 * See {@http://www.appsdeveloperblog.com/ http://www.appsdeveloperblog.com/encrypt-user-password-example-java/}
 *
 */
public class EncryptionService {

    public static String encrypt(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public static boolean isPasswordValid(String providedPassword,
                                          String securedPassword)
    {
        String newSecurePassword = encrypt(providedPassword);
        // Check if the passwords are equal
        return newSecurePassword.equalsIgnoreCase(securedPassword);
    }

}
