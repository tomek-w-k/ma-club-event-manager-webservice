package com.app.em.controller.user;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.BranchChiefRepository;
import com.app.em.persistence.repository.user.UserRepository;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;


@SpringBootTest
@AutoConfigureMockMvc
class BranchChiefControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BranchChiefRepository branchChiefRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addBranchChiefIfExists() throws Exception
    {
        saveBranchChiefIfExists(post("/branch_chiefs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addBranchChiefIfNotExist() throws Exception
    {
        saveBranchChiefIfNotExist(post("/branch_chiefs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getBranchChiefIfExists() throws Exception
    {
        BranchChief branchChief = new BranchChief(1, "New Branch Chief", new ArrayList<User>());

        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.of(branchChief));

        RequestBuilder request = get("/branch_chiefs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(branchChief)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getBranchChiefIfNotExist() throws Exception
    {
        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder request = get("/branch_chiefs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAllBranchChiefsIfExist() throws Exception
    {
        List<BranchChief> branchChiefs = new ArrayList<>();
        branchChiefs.add(new BranchChief(1, "First BC", new ArrayList<User>()));
        branchChiefs.add(new BranchChief(2, "Second BC", new ArrayList<User>()));
        branchChiefs.add(new BranchChief(3, "Third BC", new ArrayList<User>()));

        Mockito.when(branchChiefRepository.findAll()).thenReturn(branchChiefs);

        RequestBuilder request = get("/branch_chiefs");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(branchChiefs)))
                .andReturn();
    }

    @Test
    public void getAllBranchChiefsIfNotExist() throws Exception
    {
        Mockito.when(branchChiefRepository.findAll()).thenReturn(new ArrayList<BranchChief>());

        RequestBuilder request = get("/branch_chiefs");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateBranchChiefIfExists() throws Exception
    {
        saveBranchChiefIfExists(put("/branch_chiefs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateBranchChiefIfNotExist() throws Exception
    {
        saveBranchChiefIfNotExist(put("/branch_chiefs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBranchChiefIfExistsAndHasNoUsersAssigned() throws Exception
    {
        BranchChief branchChief = new BranchChief(1, "New Branch Chief", new ArrayList<User>());

        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.of(branchChief));
        Mockito.when(userRepository.findByBranchChief(branchChief)).thenReturn(new ArrayList<User>());
        Mockito.doNothing().when(branchChiefRepository).delete(branchChief);

        RequestBuilder request = delete("/branch_chiefs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Mockito.verify(branchChiefRepository, times(1)).delete(branchChief);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBranchChiefIfExistsAndHasUsersAssigned() throws Exception
    {
        BranchChief branchChief = new BranchChief();

        User user = new User();
        user.setId(1);
        user.setFullName("First User");
        user.setEmail("first.user@mail.com");
        user.setPassword("pwd");
        user.setCountry("Country");
        user.setRoles(Collections.emptySet());
        user.setRank(new Rank(1, "Rank", Collections.emptyList()));
        user.setClub(new Club(1, "Club", Collections.emptyList()));
        user.setBranchChief(branchChief);

        List<User> users = new ArrayList<>();
        users.add(user);

        branchChief.setId(1);
        branchChief.setBranchChiefName("New Branch Chief");
        branchChief.setUsers(users);

        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.of(branchChief));
        Mockito.when(userRepository.findByBranchChief(branchChief)).thenReturn(users);

        RequestBuilder request = delete("/branch_chiefs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBranchChiefIfNotExist() throws Exception
    {
        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder request = delete("/branch_chiefs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    // - - - PRIVATE METHODS - - -

    private void saveBranchChiefIfExists(MockHttpServletRequestBuilder request) throws Exception
    {
        BranchChief newBranchChief = new BranchChief();
        newBranchChief.setBranchChiefName("New Branch Chief");
        newBranchChief.setUsers(new ArrayList<User>());
        BranchChief savedBranchChief = new BranchChief(1, "New Branch Chief", new ArrayList<User>());

        Mockito.when(branchChiefRepository.findByBranchChiefName("New Branch Chief")).thenReturn(Optional.of(savedBranchChief));

        request
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(newBranchChief));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("branch_chief " + newBranchChief.getBranchChiefName() + " already_exists")))
                .andReturn();
    }

    private void saveBranchChiefIfNotExist(MockHttpServletRequestBuilder request) throws Exception
    {
        BranchChief newBranchChief = new BranchChief();
        newBranchChief.setBranchChiefName("New Branch Chief");
        newBranchChief.setUsers(new ArrayList<User>());
        BranchChief savedBranchChief = new BranchChief(1, "New Branch Chief", new ArrayList<User>());

        Mockito.when(branchChiefRepository.findByBranchChiefName("New Branch Chief")).thenReturn(Optional.empty());
        Mockito.when(branchChiefRepository.save(any())).thenReturn(savedBranchChief);

        request
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(newBranchChief));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(savedBranchChief)))
                .andReturn();
    }
}