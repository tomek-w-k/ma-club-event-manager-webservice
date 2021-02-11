package com.app.em.controller.user;

import com.app.em.persistence.entity.user.Club;
import com.app.em.persistence.repository.user.ClubRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    ObjectMapper objectMapper;


    @GetMapping(value = "/clubs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClub(@PathVariable Integer id)
    {
        return clubRepository.findById(id)
                .map(club -> ResponseEntity.ok(club))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllClubs() throws JsonProcessingException
    {
        return Optional.ofNullable(clubRepository.findAll())
                .map(this::writeToResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());


//        Optional<List<Club>> clubsOptional = Optional.ofNullable(clubRepository.findAll());
//        if ( clubsOptional.isEmpty() )
//            return ResponseEntity.notFound().build();
//
//        return ResponseEntity.ok(objectMapper.writeValueAsString(clubsOptional.get()));
    }

    @DeleteMapping("/clubs/{id}")
    public ResponseEntity deleteClub(@PathVariable Integer id)
    {
        return clubRepository.findById(id)
                .map(club -> {
                    clubRepository.delete(club);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.ok().build());

    }

    private ResponseEntity writeToResponseEntity(List<?> items)
    {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(items));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}