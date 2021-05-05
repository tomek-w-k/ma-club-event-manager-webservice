package com.app.em.controller.user;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.ClubRepository;
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
class ClubControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClubRepository clubRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addClubIfExists() throws Exception
    {
        saveClubIfExists(post("/clubs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addClubIfNotExist() throws Exception
    {
        saveClubIfNotExist(post("/clubs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getClubIfExists() throws Exception
    {
        Club club = new Club(1, "New Club", Collections.emptyList());

        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.of(club));

        RequestBuilder request = get("/clubs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(club)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getClubIfNotExist() throws Exception
    {
        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder request = get("/clubs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAllClubsIfExist() throws Exception
    {
        List<Club> clubs = new ArrayList<>();
        clubs.add(new Club(1, "First Club", new ArrayList<User>()));
        clubs.add(new Club(2, "Second Club", new ArrayList<User>()));
        clubs.add(new Club(3, "Third Club", new ArrayList<User>()));

        Mockito.when(clubRepository.findAll()).thenReturn(clubs);

        RequestBuilder request = get("/clubs");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(clubs)))
                .andReturn();
    }

    @Test
    public void getAllClubsIfNotExist() throws Exception
    {
        Mockito.when(clubRepository.findAll()).thenReturn(Collections.emptyList());

        RequestBuilder request = get("/clubs");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateClubIfExists() throws Exception
    {
        saveClubIfExists(put("/clubs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateClubIfNotExist() throws Exception
    {
        saveClubIfNotExist(put("/clubs"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteClubIfExistsAndHasNoUsersAssigned() throws Exception
    {
        Club club = new Club(1, "New Club", Collections.emptyList());

        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.of(club));
        Mockito.when(userRepository.findByClub(club)).thenReturn(Collections.emptyList());
        Mockito.doNothing().when(clubRepository).delete(club);

        RequestBuilder request = delete("/clubs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Mockito.verify(clubRepository, times(1)).delete(club);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteClubIfExistsAndHasUsersAssigned() throws Exception
    {
        Club club = new Club();

        User user = new User();
        user.setId(1);
        user.setFullName("First User");
        user.setEmail("first.user@mail.com");
        user.setPassword("pwd");
        user.setCountry("Country");
        user.setRoles(Collections.emptySet());
        user.setRank(new Rank(1, "Rank", Collections.emptyList()));
        user.setClub(new Club(1, "Club", Collections.emptyList()));
        user.setClub(club);

        List<User> users = new ArrayList<>();
        users.add(user);

        club.setId(1);
        club.setClubName("New Club");
        club.setUsers(users);

        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.of(club));
        Mockito.when(userRepository.findByClub(club)).thenReturn(users);

        RequestBuilder request = delete("/clubs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteClubIfNotExist() throws Exception
    {
        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder request = delete("/clubs/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    // - - - PRIVATE METHODS - - -

    private void saveClubIfExists(MockHttpServletRequestBuilder request) throws Exception
    {
        Club newClub = new Club();
        newClub.setClubName("New Club");
        newClub.setUsers(Collections.emptyList());
        Club savedClub = new Club(1, "New Club", Collections.emptyList());

        Mockito.when(clubRepository.findByClubName("New Club")).thenReturn(Optional.of(savedClub));

        request
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(newClub));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("club " + newClub.getClubName() + " already_exists")))
                .andReturn();
    }

    private void saveClubIfNotExist(MockHttpServletRequestBuilder request) throws Exception
    {
        Club newClub = new Club();
        newClub.setClubName("New Club");
        newClub.setUsers(Collections.emptyList());
        Club savedClub = new Club(1, "New Club", Collections.emptyList());

        Mockito.when(clubRepository.findByClubName("New Club")).thenReturn(Optional.empty());
        Mockito.when(clubRepository.save(any())).thenReturn(savedClub);

        request
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(newClub));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(savedClub)))
                .andReturn();
    }
}