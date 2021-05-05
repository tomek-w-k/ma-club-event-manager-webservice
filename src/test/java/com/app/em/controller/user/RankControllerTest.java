package com.app.em.controller.user;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.RankRepository;
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
class RankControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RankRepository rankRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addRankIfExists() throws Exception
    {
        saveRankIfExists(post("/ranks"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addRankIfNotExist() throws Exception
    {
        saveRankIfNotExist(post("/ranks"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getRankIfExists() throws Exception
    {
        Rank rank = new Rank(1, "New Rank", Collections.emptyList());

        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.of(rank));

        RequestBuilder request = get("/ranks/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(rank)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getRankIfNotExist() throws Exception
    {
        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder request = get("/ranks/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    public void getAllRanksIfExist() throws Exception
    {
        List<Rank> ranks = new ArrayList<>();
        ranks.add(new Rank(1, "First BC", Collections.emptyList()));
        ranks.add(new Rank(2, "Second BC", Collections.emptyList()));
        ranks.add(new Rank(3, "Third BC", Collections.emptyList()));

        Mockito.when(rankRepository.findAll()).thenReturn(ranks);

        RequestBuilder request = get("/ranks");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(ranks)))
                .andReturn();
    }

    @Test
    public void getAllRanksIfNotExist() throws Exception
    {
        Mockito.when(rankRepository.findAll()).thenReturn(Collections.emptyList());

        RequestBuilder request = get("/ranks");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateRankIfExists() throws Exception
    {
        saveRankIfExists(put("/ranks"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateRankIfNotExist() throws Exception
    {
        saveRankIfNotExist(put("/ranks"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteRankIfExistsAndHasNoUsersAssigned() throws Exception
    {
        Rank rank = new Rank(1, "New Rank", Collections.emptyList());

        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.of(rank));
        Mockito.when(userRepository.findByRank(rank)).thenReturn(Collections.emptyList());
        Mockito.doNothing().when(rankRepository).delete(rank);

        RequestBuilder request = delete("/ranks/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Mockito.verify(rankRepository, times(1)).delete(rank);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteRankIfExistsAndHasUsersAssigned() throws Exception
    {
        Rank rank = new Rank();

        User user = new User();
        user.setId(1);
        user.setFullName("First User");
        user.setEmail("first.user@mail.com");
        user.setPassword("pwd");
        user.setCountry("Country");
        user.setRoles(Collections.emptySet());
        user.setRank(new Rank(1, "Rank", Collections.emptyList()));
        user.setClub(new Club(1, "Club", Collections.emptyList()));
        user.setRank(rank);

        List<User> users = new ArrayList<>();
        users.add(user);

        rank.setId(1);
        rank.setRankName("New Rank");
        rank.setUsers(users);

        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.of(rank));
        Mockito.when(userRepository.findByRank(rank)).thenReturn(users);

        RequestBuilder request = delete("/ranks/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteRankIfNotExist() throws Exception
    {
        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.empty());

        RequestBuilder request = delete("/ranks/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    // - - - PRIVATE METHODS - - -

    private void saveRankIfExists(MockHttpServletRequestBuilder request) throws Exception
    {
        Rank newRank = new Rank();
        newRank.setRankName("New Rank");
        newRank.setUsers(Collections.emptyList());
        Rank savedRank = new Rank(1, "New Rank", Collections.emptyList());

        Mockito.when(rankRepository.findByRankName("New Rank")).thenReturn(Optional.of(savedRank));

        request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newRank));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("rank " + newRank.getRankName() + " already_exists")))
                .andReturn();
    }

    private void saveRankIfNotExist(MockHttpServletRequestBuilder request) throws Exception
    {
        Rank newRank = new Rank();
        newRank.setRankName("New Rank");
        newRank.setUsers(Collections.emptyList());
        Rank savedRank = new Rank(1, "New Rank", Collections.emptyList());

        Mockito.when(rankRepository.findByRankName("New Rank")).thenReturn(Optional.empty());
        Mockito.when(rankRepository.save(any())).thenReturn(savedRank);

        request
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newRank));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(savedRank)))
                .andReturn();
    }
}