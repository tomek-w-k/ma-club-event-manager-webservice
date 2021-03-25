package com.app.em.controller.user;

import com.app.em.persistence.entity.user.Rank;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.user.RankRepository;
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


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RankController
{
    @Autowired
    RankRepository rankRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addRank(@RequestBody Rank rank)
    {
        return saveIfNotExist(rank);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/ranks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRank(@PathVariable Integer id)
    {
        return ResponseEntity.of( rankRepository.findById(id) );
    }

    @GetMapping(value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllRanks()
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(rankRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRank(@RequestBody Rank rank)
    {
        return saveIfNotExist(rank);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/ranks/{id}")
    public ResponseEntity deleteRank(@PathVariable Integer id)
    {
        return rankRepository.findById(id)
                .map(rank -> {
                    return userRepository.findByRank(rank)
                            .stream()
                            .findAny()
                            .map(this::rankHasUsersAssigned)
                            .orElseGet(() -> {
                                rankRepository.delete(rank);
                                return ResponseEntity.ok().build();
                            });
                }).orElseGet(() ->  ResponseEntity.notFound().build());
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity saveIfNotExist(Rank rank)
    {
        return rankRepository.findByRankName(rank.getRankName())
                .map(this::rankAlreadyExists)
                .orElseGet(() -> ResponseEntity.ok(rankRepository.save(rank)));
    }

    private ResponseEntity rankAlreadyExists(Rank rank)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("rank " + rank.getRankName() + " already_exists"));
    }

    private ResponseEntity rankHasUsersAssigned(User user)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("rank " + user.getRank().getRankName() +
                    " rank_cannot_be_removed_because"));
    }
}
