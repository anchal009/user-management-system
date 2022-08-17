package com.helseapps.task.rest.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserListDTOTest {

    @Test
    public void userListDTOTest() {
        UserListDTO userListDTO = new UserListDTO();

        assertEquals(0, userListDTO.getUserList().size());
    }

}
