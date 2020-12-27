package com.app.em.controller.security;

import com.app.em.persistence.entity.user.*;
import com.app.em.persistence.repository.user.BranchChiefRepository;
import com.app.em.persistence.repository.user.ClubRepository;
import com.app.em.persistence.repository.user.RankRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    RankRepository rankRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    BranchChiefRepository branchChiefRepository;

    @Autowired
    ObjectMapper objectMapper;


    @PostMapping("/users")
    public ResponseEntity addUser(@RequestBody User user) throws JsonProcessingException
    {
        Optional<User> userFullNameOptional = userRepository.findByFullName(user.getFullName());
        if ( userFullNameOptional.isPresent() )
            return userAlreadyExists(user.getFullName());

        Optional<User> userEmailOptional = userRepository.findByEmail(user.getEmail());
        if ( userEmailOptional.isPresent() )
            return userAlreadyExists(user.getEmail());

        user.setRank( createRankIfNotExist(user.getRank()) );
        user.setClub( createClubIfNotExist(user.getClub()) );
        user.setBranchChief( createBranchChiefIfNotExist(user.getBranchChief()) );

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id)
    {
        Optional<User> userOptional = userRepository.findById(id);
        if ( userOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( userOptional.get() );
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUsers() throws JsonProcessingException
    {
        Optional<List<User>> usersOptional = Optional.ofNullable( userRepository.findAll() );
        if ( usersOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(usersOptional.get()) );
    }

    @PutMapping("/users")
    public ResponseEntity updateUser(@RequestBody User user)
    {
        Optional<User> userOptional = userRepository.findById( user.getId() );
        if ( userOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        user.setRank( createRankIfNotExist(user.getRank()) );
        user.setClub( createClubIfNotExist(user.getClub()) );
        user.setBranchChief( createBranchChiefIfNotExist(user.getBranchChief()) );

        User updatedUser = userRepository.save( user );

        return ResponseEntity.ok( updatedUser );
    }

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

    /*
        Returns a Rank if exist in a repository, if does not exist - creates a new one, saves it into a repository
        and returns it.
     */
    private Rank createRankIfNotExist(Rank rank)
    {
        Optional<Rank> rankIdOptional = rankRepository.findById(rank.getId());
        if ( rankIdOptional.isPresent() )
            return rankIdOptional.get();

        Optional<Rank> rankNameOptional = rankRepository.findByRankName(rank.getRankName());
        if ( rankNameOptional.isPresent() )
            return rankNameOptional.get();

        Rank newRank = rankRepository.save( rank );

        return newRank;
    }

    /*
        Returns a Club if exist in a repository, if does not exist - creates a new one, saves it into a repository
        and returns it.
     */
    private Club createClubIfNotExist(Club club)
    {
        Optional<Club> clubIdOptional = clubRepository.findById(club.getId());
        if ( clubIdOptional.isPresent() )
            return clubIdOptional.get();

        Optional<Club> clubNameOptional = clubRepository.findByClubName(club.getClubName());
        if ( clubNameOptional.isPresent() )
            return clubNameOptional.get();

        Club newClub = clubRepository.save( club );

        return newClub;
    }

    /*
        Returns a BranchChief if exist in a repository, if does not exist - creates a new one, saves it into a repository
        and returns it.
     */
    private BranchChief createBranchChiefIfNotExist(BranchChief branchChief)
    {
        Optional<BranchChief> branchChiefIdOptional = branchChiefRepository.findById(branchChief.getId());
        if ( branchChiefIdOptional.isPresent() )
            return branchChiefIdOptional.get();

        Optional<BranchChief> branchChiefNameOptional = branchChiefRepository.findByBranchChiefName(branchChief.getBranchChiefName());
        if ( branchChiefNameOptional.isPresent() )
            return branchChiefNameOptional.get();

        BranchChief newBranchChief = branchChiefRepository.save( branchChief );

        return newBranchChief;
    }
}
