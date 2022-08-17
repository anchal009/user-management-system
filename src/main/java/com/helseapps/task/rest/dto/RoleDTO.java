package com.helseapps.task.rest.dto;

import com.helseapps.task.rest.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class RoleDTO implements Serializable {

    private Long id;
    private String role;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.role = role.getRole();
    }

    public RoleDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleDTO)) return false;
        return id != null && id.equals(((RoleDTO) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
