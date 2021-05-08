package com.app.em.controller.user;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RankRepository rankRepository;

    @MockBean
    private ClubRepository clubRepository;

    @MockBean
    private BranchChiefRepository branchChiefRepository;

    @MockBean
    private RoleRepository roleRepository;


    @Test
    @WithMockUser(username = "trainer", roles = {"TRAINER"})
    public void addUserIfExistsByEmail() throws Exception
    {
        User user = testUser();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        RequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("email_already_taken")))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "trainer", roles = {"TRAINER"})
    public void addUserIfNotExistByEmailAndHasRoleAdmin() throws Exception
    {
        Set<Role> roles = new HashSet<>();
        roles.add(testRoleAdmin());

        User user = testUser();
        user.setRoles(roles);

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        RequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(user));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Remove " + RoleEnum.ROLE_ADMIN.toString() + " from json.")))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "trainer", roles = {"TRAINER"})
    public void addUserIfNotExistByEmailAndHasNotRoleAdmin() throws Exception
    {
        Mockito.when(userRepository.findByEmail(testUser().getEmail())).thenReturn(Optional.empty());
        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.of(testRank()));
        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.of(testClub()));
        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.of(testBranchChief()));
        Mockito.when(userRepository.save(any())).thenReturn(testUser());

        RequestBuilder request = post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUser()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(testUser())))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getUserIfExists() throws Exception
    {
        User user = testUser();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        RequestBuilder request = get("/users/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(user)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getUserIfNotExist() throws Exception
    {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RequestBuilder request = get("/users/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllUsersIfExist() throws Exception
    {
        List<User> users = new ArrayList<>();
        users.add(testUser());

        Mockito.when(userRepository.findAll()).thenReturn(users);

        RequestBuilder request = get("/users");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(users)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getAllUsersIfNotExist() throws Exception
    {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<User>());

        RequestBuilder request = get("/users");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andReturn();
    }

    @Test
    public void getUsersForRoleIfRoleNotExist() throws Exception
    {
        Mockito.when(roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.empty());

        RequestBuilder request = get("/roles/ROLE_ADMIN/users?hasRole=YES");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    /*
        hasRole=yes - endpoint returns all users containing a given role
     */
    @Test
    public void getUsersForRoleIfRoleExistsAndHasRoleYes() throws Exception
    {
        List<User> users = new ArrayList<>();
        users.add(testUserAdmin());

        Mockito.when(roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(testRoleAdmin()));
        Mockito.when(userRepository.findByRoles(any())).thenReturn(users);

        RequestBuilder request = get("/roles/ROLE_ADMIN/users?hasRole=yes");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(users)))
                .andReturn();
    }

    /*
        hasRole=no - endpoint returns all users except of users with a given role
     */
    @Test
    public void getUsersForRoleIfRoleExistsAndHasRoleNo() throws Exception
    {
        List<User> users = new ArrayList<>();
        users.add(testUserUser());
        users.add(testUserTrainer());

        Mockito.when(roleRepository.findByRoleName(RoleEnum.ROLE_ADMIN)).thenReturn(Optional.of(testRoleAdmin()));
        Mockito.when(userRepository.findByRolesNotContaining(any())).thenReturn(users);

        RequestBuilder request = get("/roles/ROLE_ADMIN/users?hasRole=no");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(users)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void manageAdminPrivilegesIfUserFoundById() throws Exception
    {
        User userAdmin = testUserAdmin();

        Mockito.when(userRepository.findById(userAdmin.getId())).thenReturn(Optional.of(userAdmin));
        Mockito.when(userRepository.save(any())).thenReturn(userAdmin);

        RequestBuilder request = put("/administrators")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(userAdmin));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(userAdmin)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void manageAdminPrivilegesIfUserNotFoundById() throws Exception
    {
        Mockito.when(userRepository.findById(testUserAdmin().getId())).thenReturn(Optional.empty());

        RequestBuilder request = put("/administrators")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUserAdmin()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateUserIfNotFoundById() throws Exception
    {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RequestBuilder request = put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUser()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateUserIfFoundByIdAndEmailIsNull() throws Exception
    {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
        Mockito.when(userRepository.save(any())).thenReturn(testUser());

        RequestBuilder request = put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUser()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(testUser())))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateUserIfFoundByIdAndEmailAlreadyTaken() throws Exception
    {
        User foundUser = testUser();
        foundUser.setId(2);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
        Mockito.when(userRepository.findByEmail(testUser().getEmail())).thenReturn(Optional.of(foundUser));

        RequestBuilder request = put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUser()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("email_already_taken")))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateUserIfFoundByIdAndHasRoleAdminInserted() throws Exception
    {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
        Mockito.when(userRepository.findByEmail(testUser().getEmail())).thenReturn(Optional.of(testUser()));

        RequestBuilder request = put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUserAdmin()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("Remove " + RoleEnum.ROLE_ADMIN.toString() + " from json")))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateUserIfFoundByIdAndHasNotRoleAdminInserted() throws Exception
    {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser()));
        Mockito.when(userRepository.findByEmail(testUser().getEmail())).thenReturn(Optional.of(testUser()));
        Mockito.when(rankRepository.findById(1)).thenReturn(Optional.of(testRank()));
        Mockito.when(clubRepository.findById(1)).thenReturn(Optional.of(testClub()));
        Mockito.when(branchChiefRepository.findById(1)).thenReturn(Optional.of(testBranchChief()));
        Mockito.when(userRepository.save(any())).thenReturn(testUser());

        RequestBuilder request = put("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(testUser()));
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(testUser())))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUserIfExistsById() throws Exception
    {
        User user = testUser();

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userRepository).delete(user);

        RequestBuilder request = delete("/users/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Mockito.verify(userRepository, times(1)).delete(user);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUserIfNotExistById() throws Exception
    {
        Mockito.when(userRepository.findById(testUser().getId())).thenReturn(Optional.empty());

        RequestBuilder request = delete("/users/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
    }

    // - - - PRIVATE METHODS - - -

    private User testUser()
    {
        User user = new User();
        user.setId(1);
        user.setFullName("First User");
        user.setEmail("first.user@mail.com");
        user.setPassword("pwd");
        user.setCountry("Country");
        user.setRoles(Collections.emptySet());
        user.setRank(new Rank(1, "Rank", Collections.emptyList()));
        user.setClub(new Club(1, "Club", Collections.emptyList()));
        user.setBranchChief(new BranchChief(1, "Branch Chief", Collections.emptyList()));

        return user;
    }

    private Role testRoleUser()
    {
        return new Role(1, RoleEnum.ROLE_USER);
    }

    private Role testRoleTrainer()
    {
        return new Role(2, RoleEnum.ROLE_TRAINER);
    }

    private Role testRoleAdmin()
    {
        return new Role(3, RoleEnum.ROLE_ADMIN);
    }

    private Rank testRank()
    {
        return new Rank(1, "Rank", Collections.emptyList());
    }

    private Club testClub()
    {
        return new Club(1, "Club", Collections.emptyList());
    }

    private BranchChief testBranchChief()
    {
        return new BranchChief(1, "Branch Chief", Collections.emptyList());
    }

    private User testUserUser()
    {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(testRoleUser());

        User user = testUser();
        user.setRoles(roleSet);

        return user;
    }

    private User testUserTrainer()
    {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(testRoleUser());
        roleSet.add(testRoleTrainer());

        User user = testUser();
        user.setRoles(roleSet);

        return user;
    }

    private User testUserAdmin()
    {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(testRoleUser());
        roleSet.add(testRoleTrainer());
        roleSet.add(testRoleAdmin());

        User user = testUser();
        user.setRoles(roleSet);

        return user;
    }
}