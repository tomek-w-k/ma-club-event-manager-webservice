package com.app.em.controller.user;

import com.app.em.persistence.entity.user.Club;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.user.ClubRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClubController
{
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @PostMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addClub(@RequestBody Club club)
    {
        return clubRepository.findByClubName(club.getClubName())
                .map(this::clubAlreadyExists)
                .orElseGet(() -> ResponseEntity.ok(clubRepository.save(club)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/clubs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClub(@PathVariable Integer id)
    {
        return ResponseEntity.of( clubRepository.findById(id) );
    }

    @GetMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllClubs()
    {
        return Optional.ofNullable(clubRepository.findAll())
                .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateClub(@RequestBody Club club)
    {
        return clubRepository.findById(club.getId())
                .map(existingClub -> ResponseEntity.ok(clubRepository.save(club)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clubs/{id}")
    public ResponseEntity deleteClub(@PathVariable Integer id)
    {
        return clubRepository.findById(id)
                .map(club -> {
                    return Optional.ofNullable(userRepository.findByClub(club))
                            .map(this::clubHasUsersAssigned)
                            .orElseGet(() -> {
                                clubRepository.delete(club);
                                return ResponseEntity.ok().build();
                            });
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity clubAlreadyExists(Club club)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("A club " + club.getClubName() + " already exists."));
    }

    private ResponseEntity clubHasUsersAssigned(List<User> users)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("A club " +
                    users.stream().findAny().map(user -> user.getClub().getClubName()) +
                    " cannot be removed because it has one or more people assigned. Change their club and try again."));
    }
}
