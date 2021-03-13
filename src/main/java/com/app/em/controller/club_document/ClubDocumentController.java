package com.app.em.controller.club_document;

import com.app.em.persistence.entity.club_document.ClubDocument;
import com.app.em.persistence.repository.club_document.ClubDocumentRepository;
import com.app.em.utils.ListToResponseEntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClubDocumentController
{
    @Autowired
    ClubDocumentRepository clubDocumentRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addClubDocument(@RequestBody ClubDocument clubDocument)
    {
        return ResponseEntity.ok( clubDocumentRepository.save(clubDocument) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/club_documents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClubDocument(@PathVariable Integer id)
    {
        return ResponseEntity.of( clubDocumentRepository.findById(id) );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllClubDocuments() throws JsonProcessingException
    {
        return Optional.ofNullable(clubDocumentRepository.findAll())
                .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateClubDocument(@RequestBody ClubDocument clubDocument)
    {
        return clubDocumentRepository.findById(clubDocument.getId())
                .map(clubDocumentToUpdate -> ResponseEntity.ok(clubDocumentRepository.save(clubDocument)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/club_documents/{id}")
    public ResponseEntity deleteClubDocument(@PathVariable Integer id)
    {
        return clubDocumentRepository.findById(id)
                .map(clubDocument -> {
                    clubDocumentRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
