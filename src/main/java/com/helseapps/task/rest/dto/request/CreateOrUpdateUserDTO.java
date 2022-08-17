package com.helseapps.task.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Create or modify user data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateUserDTO implements Serializable {

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String gender;
    private String birthDay;
    private String address;
    private String phoneNumber;
    private String email;

    private boolean enabled;

    private String note;

}
