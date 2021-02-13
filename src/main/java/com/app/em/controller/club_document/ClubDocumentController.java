package com.app.em.controller.club_document;

import com.app.em.persistence.entity.club_document.ClubDocument;
import com.app.em.persistence.repository.club_document.ClubDocumentRepository;
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
public class ClubDocumentController
{
    @Autowired
    ClubDocumentRepository clubDocumentRepository;

    @Autowired
    ObjectMapper objectMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addClubDocument(@RequestBody ClubDocument clubDocument)
    {
        ClubDocument savedClubDocument = clubDocumentRepository.save(clubDocument);
        return ResponseEntity.ok( savedClubDocument );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/club_documents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClubDocument(@PathVariable Integer id)
    {
        Optional<ClubDocument> clubDocumentOptional = clubDocumentRepository.findById(id);
        if ( clubDocumentOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(clubDocumentOptional.get());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllClubDocuments() throws JsonProcessingException
    {
        Optional<List<ClubDocument>> clubDocumentsOptional = Optional.ofNullable( clubDocumentRepository.findAll() );
        if ( clubDocumentsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString( clubDocumentsOptional.get() ) );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateClubDocument(@RequestBody ClubDocument clubDocument)
    {
        Optional<ClubDocument> clubDocumentOptional = clubDocumentRepository.findById( clubDocument.getId() );
        if ( clubDocumentOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        ClubDocument updatedClubDocument = clubDocumentRepository.save(clubDocument);

        return ResponseEntity.ok( updatedClubDocument );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/club_documents/{id}")
    public ResponseEntity deleteClubDocument(@PathVariable Integer id)
    {
        Optional<ClubDocument> clubDocumentOptional = clubDocumentRepository.findById(id);
        if ( clubDocumentOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        clubDocumentRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
