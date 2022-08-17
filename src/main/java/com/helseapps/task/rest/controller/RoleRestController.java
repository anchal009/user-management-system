package com.helseapps.task.rest.controller;

import com.helseapps.task.rest.dto.RoleDTO;
import com.helseapps.task.rest.entity.Role;
import com.helseapps.task.rest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users/rbac")
public class RoleRestController {

    @Autowired
    private RoleService roleService;

    // roles
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getRolePresentationList() {
        Iterable<Role> roleList = roleService.getRoleList();
        ArrayList<RoleDTO> list = new ArrayList<>();
        roleList.forEach(e -> list.add(new RoleDTO(e)));
        return ResponseEntity.ok(list);
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> createRole(@RequestBody String role) {
        return new ResponseEntity(new RoleDTO(roleService.createRole(role)), null, HttpStatus.CREATED);
    }

    @GetMapping("/roles/{roleId}")
    public RoleDTO getRoleById(@PathVariable("roleId") Long roleId) {
        return new RoleDTO(roleService.getRoleById(roleId));
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

}
