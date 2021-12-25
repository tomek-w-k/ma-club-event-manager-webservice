package com.app.em.controller.user;

import com.app.em.persistence.entity.user.RoleEnum;
import com.app.em.persistence.repository.user.RoleRepository;
import com.app.em.utils.ListToResponseEntityWrapper;
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
    private final RoleRepository roleRepository;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public RoleController(  RoleRepository roleRepository,
                            ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.roleRepository = roleRepository;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }

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
