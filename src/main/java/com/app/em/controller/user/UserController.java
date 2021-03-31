package com.app.em.controller.user;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.*;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController
{
    private final static String YES = "yes";
    private final static String NO = "no";

    @Autowired
    UserRepository userRepository;

    @Autowired
    RankRepository rankRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BranchChiefRepository branchChiefRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user)
    {
        return userRepository.findByEmail(user.getEmail())
                .map(this::emailAlreadyTaken)
                .orElseGet(() -> {
                    return user.getRoles()
                            .stream()
                            .filter(role -> role.getRoleName().toString().equals("ROLE_ADMIN"))
                            .findAny()
                            .map(this::removeRole)
                            .orElseGet(() -> setUserSelectableOptionsAndSaveUser(user));
                });
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id)
    {
        return ResponseEntity.of( userRepository.findById(id) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUsers()
    {
         return listToResponseEntityWrapper.wrapListInResponseEntity(userRepository.findAll());
    }

    @GetMapping(value = "/roles/{roleName}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersForRole(@PathVariable String roleName, @RequestParam String hasRole)
    {
        return roleRepository.findByRoleName(RoleEnum.valueOf(roleName))
                .map(role -> {
                    switch ( hasRole )
                    {
                        case YES: return listToResponseEntityWrapper.wrapListInResponseEntity(userRepository.findByRoles(role));
                        case NO: return listToResponseEntityWrapper.wrapListInResponseEntity(userRepository.findByRolesNotContaining(role));
                        default: return ResponseEntity.notFound().build();
                    }
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/administrators")
    public ResponseEntity manageAdminPrivileges(@RequestBody User adminUser)
    {
        return userRepository.findById(adminUser.getId())
                .map(foundUser -> ResponseEntity.ok(userRepository.save(adminUser)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody User user)
    {
        return userRepository.findById(user.getId())
                .map(existingUser -> {
                    if (user.getEmail() == null)
                        return ResponseEntity.ok(userRepository.save(user));

                    return userRepository.findByEmail(user.getEmail())
                            .filter(foundUser -> foundUser.getId() != user.getId())
                            .map(this::emailAlreadyTaken)
                            .orElseGet(() ->{
                                return userRepository.findByEmail(user.getEmail())
                                        .filter(foundUser -> !foundUser.getRoles().stream().anyMatch(role -> role.getRoleName().toString().equals(RoleEnum.ROLE_ADMIN.toString())) &&
                                                            Optional.ofNullable(user.getRoles()).map(roles -> roles.stream()
                                                                    .anyMatch(role -> role.getRoleName().toString().equals(RoleEnum.ROLE_ADMIN.toString()))).orElseGet(() -> false))
                                        .map(foundUser -> ResponseEntity.badRequest().body(new MessageResponse("Remove ROLE_ADMIN from json")))
                                        .orElseGet(() -> setUserSelectableOptionsAndSaveUser(user));
                            });
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id)
    {
        return userRepository.findById(id)
                .map(existingUser -> {
                    userRepository.delete(existingUser);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity emailAlreadyTaken(User existingUser)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("email_already_taken"));
    }

    private ResponseEntity removeRole(Role role)
    {
        return ResponseEntity.badRequest().body(new MessageResponse("Remove " + role.getRoleName() + " from json."));
    }

    private ResponseEntity setUserSelectableOptionsAndSaveUser(User user)
    {
        Optional.ofNullable(user.getRank())
                .ifPresent(rank -> user.setRank( rankRepository.findById(rank.getId()).orElseGet(() -> rankRepository.save(rank))) );
        Optional.ofNullable(user.getClub())
                .ifPresent(club -> user.setClub( clubRepository.findById(club.getId()).orElseGet(() -> clubRepository.save(club))) );
        Optional.ofNullable(user.getBranchChief())
                .ifPresent(branchChief -> user.setBranchChief( branchChiefRepository.findById(branchChief.getId())
                        .orElseGet(() -> branchChiefRepository.save(branchChief))) );

        return ResponseEntity.ok( userRepository.save(user) );
    }
}
