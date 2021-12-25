package com.app.em.controller.user;

import com.app.em.persistence.entity.user.Club;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.user.ClubRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClubController
{
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public ClubController(  ClubRepository clubRepository,
                            UserRepository userRepository,
                            ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addClub(@RequestBody Club club)
    {
        return saveIfNotExist(club);
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
        return listToResponseEntityWrapper.wrapListInResponseEntity(clubRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateClub(@RequestBody Club club)
    {
        return saveIfNotExist(club);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clubs/{id}")
    public ResponseEntity deleteClub(@PathVariable Integer id)
    {
        return clubRepository.findById(id)
                .map(club -> {
                    return userRepository.findByClub(club)
                            .stream()
                            .findAny()
                            .map(this::clubHasUsersAssigned)
                            .orElseGet(() -> {
                                clubRepository.delete(club);
                                return ResponseEntity.ok().build();
                            });
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity saveIfNotExist(Club club)
    {
        return clubRepository.findByClubName(club.getClubName())
                .map(this::clubAlreadyExists)
                .orElseGet(() -> ResponseEntity.ok(clubRepository.save(club)));
    }

    private ResponseEntity clubAlreadyExists(Club club)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("club " + club.getClubName() + " already_exists"));
    }

    private ResponseEntity clubHasUsersAssigned(User user)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("club " + user.getClub().getClubName() +
                    " club_cannot_be_removed_because"));
    }
}
