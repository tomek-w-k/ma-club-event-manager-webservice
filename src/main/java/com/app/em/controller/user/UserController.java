package com.app.em.controller.user;

import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.team.Team;
import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.persistence.repository.registration.CampRegistrationRepository;
import com.app.em.persistence.repository.registration.ExamRegistrationRepository;
import com.app.em.persistence.repository.registration.TeamRepository;
import com.app.em.persistence.repository.user.*;
import com.app.em.security.payload.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController
{
    private static final String SIMPLE_ENDPOINT_MODE = "simple";
    private static final String COMPLEMENT_ENDPOINT_MODE = "complement";

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
    ExamRegistrationRepository examRegistrationRepository;

    @Autowired
    CampRegistrationRepository campRegistrationRepository;

    @Autowired
    TournamentEventRepository tournamentEventRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    ObjectMapper objectMapper;


    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) throws JsonProcessingException
    {
        Optional<User> userEmailOptional = userRepository.findByEmail(user.getEmail());
        if ( userEmailOptional.isPresent() )
            return userAlreadyExists(user.getEmail());

        if ( user.getRank() != null )
            user.setRank( rankRepository.findById(user.getRank().getId()).orElseGet(() -> rankRepository.save(user.getRank())) );

        if ( user.getClub() != null )
            user.setClub( clubRepository.findById(user.getClub().getId()).orElseGet(() -> clubRepository.save(user.getClub())) );

        if ( user.getBranchChief() != null )
            user.setBranchChief( branchChiefRepository.findById(user.getBranchChief().getId()).orElseGet(() ->
                    branchChiefRepository.save(user.getBranchChief())) );

        Set<Role> rolesForUser = new HashSet<>();
        Role roleUser = roleRepository.findByRoleName(RoleEnum.ROLE_USER).orElseGet(() -> new Role(RoleEnum.ROLE_USER));
        rolesForUser.add(roleUser);
        user.setRoles(rolesForUser);

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id)
    {
        Optional<User> userOptional = userRepository.findById(id);
        if ( userOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( userOptional.get() );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUsers() throws JsonProcessingException
    {
        Optional<List<User>> usersOptional = Optional.ofNullable( userRepository.findAll() );
        if ( usersOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(usersOptional.get()) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/users/{mode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersForTournament(@PathVariable Long tournamentEventId, @PathVariable String mode) throws JsonProcessingException
    {
        Optional<TournamentEvent> tournamentEventOptional = tournamentEventRepository.findById(tournamentEventId);
        if ( tournamentEventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("A given tournament doesn't exist."));

        Optional<List<Team>> teamsOptional = Optional.ofNullable( teamRepository.findByTournamentEvent(tournamentEventOptional.get()) );
        if ( teamsOptional.isEmpty() )
        {
            switch (mode)
            {
                case SIMPLE_ENDPOINT_MODE: return ResponseEntity.notFound().build();
                case COMPLEMENT_ENDPOINT_MODE: return this.getAllUsers();
                default: return ResponseEntity.badRequest().body(new MessageResponse("Error - Wrong endpoint mode (simple | complement)"));
            }
        }

        List<User> usersInTournament = teamsOptional
                .get().stream().map(team -> team.getTournamentRegistrations())
                                .flatMap(regList -> regList.stream())
                                .map(registration -> registration.getUser())
                                .collect(Collectors.toList());

        switch (mode)
        {
            case SIMPLE_ENDPOINT_MODE:  return ResponseEntity.ok( objectMapper.writeValueAsString(usersInTournament) );
            case COMPLEMENT_ENDPOINT_MODE: {
                Optional<List<User>> usersOptional = Optional.ofNullable( userRepository.findAll() );
                if ( usersOptional.isEmpty() )
                    return ResponseEntity.notFound().build();

                List<User> users = usersOptional.get();
                users.removeAll(usersInTournament);
                return ResponseEntity.ok( objectMapper.writeValueAsString( users ) );
            }
            default: return ResponseEntity.badRequest().body(new MessageResponse("Error - Wrong endpoint mode (simple | complement)"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "roles/{roleName}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsersForRole(@PathVariable String roleName) throws JsonProcessingException
    {
        RoleEnum roleEnum;
        switch(roleName)
        {
            case "ROLE_USER": roleEnum = RoleEnum.ROLE_USER; break;
            case "ROLE_ADMIN": roleEnum = RoleEnum.ROLE_ADMIN; break;
            case "ROLE_TRAINER": roleEnum = RoleEnum.ROLE_TRAINER; break;
            default : roleEnum = RoleEnum.ROLE;
        }

        Optional<Role> roleOptional = roleRepository.findByRoleName(roleEnum);
        if ( roleOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Optional<List<User>> usersOptional = Optional.ofNullable( userRepository.findByRoles(roleOptional.get()) );
        if ( usersOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(usersOptional.get()) );
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody User user)
    {
        Optional<User> userByIdOptional = userRepository.findById(user.getId());
        if ( userByIdOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        // - - - soft deletion - - -
        if (user.getEmail() == null)
            return ResponseEntity.ok(userRepository.save(user));

        // - - - update - - -
        Optional<User> userByEmailOptional = userRepository.findByEmail(user.getEmail());
        // if such an email exists and if it's not the user's own email then return
        if ( userByEmailOptional.isPresent() && (!user.getEmail().equals(userByIdOptional.get().getEmail()) ) )
            return ResponseEntity.badRequest().body(new MessageResponse("email_already_taken"));

        if ( user.getRank() != null )
            user.setRank( rankRepository.findById(user.getRank().getId()).orElseGet(() -> rankRepository.save(user.getRank())) );

        if ( user.getClub() != null )
            user.setClub( clubRepository.findById(user.getClub().getId()).orElseGet(() -> clubRepository.save(user.getClub())) );

        if ( user.getBranchChief() != null )
            user.setBranchChief( branchChiefRepository.findById(user.getBranchChief().getId()).orElseGet(() ->
                    branchChiefRepository.save(user.getBranchChief())) );

        User updatedUser = userRepository.save( user );

        return ResponseEntity.ok( updatedUser );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id)
    {
        Optional<User> userOptional = userRepository.findById(id);
        if ( userOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        userRepository.deleteById( id );

        return ResponseEntity.ok().build();
    }

    private ResponseEntity userAlreadyExists(String who)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User " + who + " already exists");
    }
}
