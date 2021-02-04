package com.app.em.controller.user;

import com.app.em.persistence.entity.user.Rank;
import com.app.em.persistence.repository.user.RankRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class RankController
{
    @Autowired
    RankRepository rankRepository;

    @Autowired
    ObjectMapper objectMapper;


    @GetMapping(value = "/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllRanks() throws JsonProcessingException
    {
        Optional<List<Rank>> ranksOptional = Optional.ofNullable(rankRepository.findAll());
        if ( ranksOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(objectMapper.writeValueAsString(ranksOptional.get()));
    }
}
