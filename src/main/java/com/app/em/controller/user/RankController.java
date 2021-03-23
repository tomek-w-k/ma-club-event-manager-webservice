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
import java.util.Optional;


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
        return rankRepository.findByRankName(rank.getRankName())
                .map(this::rankAlreadyExists)
                .orElseGet(() -> ResponseEntity.ok(rankRepository.save(rank)));
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
        List<Rank> ranks = rankRepository.findAll();

        if ( ranks.isEmpty() )
            return ResponseEntity.notFound().build();
        else return listToResponseEntityWrapper.wrapListInResponseEntity(ranks);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRank(@RequestBody Rank rank)
    {
        return rankRepository.findById(rank.getId())
                .map(rankToUpdate -> ResponseEntity.ok(rankRepository.save(rank)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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

    private ResponseEntity rankAlreadyExists(Rank rank)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("A rank " + rank.getRankName() + " already exists."));
    }

    private ResponseEntity rankHasUsersAssigned(User user)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("A rank " + user.getRank().getRankName() +
                    " cannot be removed because it has one or more people assigned. Change their rank and try again."));
    }
}
