package com.app.em.controller.user;

import com.app.em.persistence.entity.user.Club;
import com.app.em.persistence.repository.user.ClubRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    ObjectMapper objectMapper;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/clubs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClub(@PathVariable Integer id)
    {
        return clubRepository.findById(id)
                .map(club -> ResponseEntity.ok(club))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/clubs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllClubs()
    {
        return Optional.ofNullable(clubRepository.findAll())
                .map(this::writeListToResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    private ResponseEntity writeListToResponseEntity(List<?> items)
    {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(items));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
