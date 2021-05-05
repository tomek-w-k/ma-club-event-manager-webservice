package com.app.em.controller.user;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoleRepository roleRepository;


    @Test
    public void getRoleByRoleNameIfExists() throws Exception
    {
        Role role = new Role(1, RoleEnum.ROLE_ADMIN);

        Mockito.when(roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(role));

        RequestBuilder request = get("/roles/ROLE_ADMIN");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(role)))
                .andReturn();
    }

    @Test
    public void getRoleByRoleNameIfNotExist() throws Exception
    {
        Mockito.when(roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.empty());

        RequestBuilder request = get("/roles/ROLE_ADMIN");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getRolesIfExist() throws Exception
    {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1, RoleEnum.ROLE_USER));
        roles.add(new Role(2, RoleEnum.ROLE_TRAINER));
        roles.add(new Role(3, RoleEnum.ROLE_ADMIN));

        Mockito.when(roleRepository.findAll()).thenReturn(roles);

        RequestBuilder request = get("/roles");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(roles)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getRolesIfNotExist() throws Exception
    {
        Mockito.when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        RequestBuilder request = get("/roles");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }
}