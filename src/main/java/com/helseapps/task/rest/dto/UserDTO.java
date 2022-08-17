package com.helseapps.task.rest.dto;

import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    public UserDTO() {
        // empty constructor
        roles = new ArrayList<>();
    }

    public UserDTO(User user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.gender = user.getGender();
            this.address = user.getAddress();
            this.phoneNumber= user.getPhoneNumber();
            this.birthDay = user.getBirthDay();
            this.email = user.getEmail();

            this.enabled = user.isEnabled();
            this.note = user.getNote();
            this.created = user.getCreated();
            this.updated = user.getUpdated();
            this.loginDate = user.getLoginDate();

            roles = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
            }

        }
    }

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthDay;
    private String address;
    private String phoneNumber;
    private String email;

    private boolean enabled;

    private String note;

    private LocalDateTime created;
    private LocalDateTime updated;
    private LocalDateTime loginDate;

    // roles list
    private List<String> roles;

}
