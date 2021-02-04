package com.app.em.controller.user;

import com.app.em.persistence.entity.user.BranchChief;
import com.app.em.persistence.repository.user.BranchChiefRepository;
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
public class BranchChiefController
{
    @Autowired
    BranchChiefRepository branchChiefRepository;

    @Autowired
    ObjectMapper objectMapper;


    @GetMapping(value = "/branch_chiefs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllBranchChiefs() throws JsonProcessingException
    {
        Optional<List<BranchChief>> branchChiefsOptional = Optional.ofNullable(branchChiefRepository.findAll());
        if ( branchChiefsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(objectMapper.writeValueAsString(branchChiefsOptional.get()));
    }
}
