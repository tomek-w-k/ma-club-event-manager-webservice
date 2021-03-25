package com.app.em.controller.user;

import com.app.em.persistence.entity.user.RoleEnum;
import com.app.em.persistence.repository.user.RoleRepository;
import com.app.em.utils.ListToResponseEntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController
{
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @GetMapping(value = "roles/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRoleByRoleName(@PathVariable String roleName)
    {
        return ResponseEntity.of( roleRepository.findByRoleName(RoleEnum.valueOf(roleName)) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRoles()
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(roleRepository.findAll());
    }
}
