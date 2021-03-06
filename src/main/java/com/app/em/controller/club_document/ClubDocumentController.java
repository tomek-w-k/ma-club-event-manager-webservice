package com.app.em.controller.club_document;

import com.app.em.persistence.entity.club_document.ClubDocument;
import com.app.em.persistence.repository.club_document.ClubDocumentRepository;
import com.app.em.utils.ListToResponseEntityWrapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClubDocumentController
{
    private final ClubDocumentRepository clubDocumentRepository;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public ClubDocumentController(  ClubDocumentRepository clubDocumentRepository,
                                    ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.clubDocumentRepository = clubDocumentRepository;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }


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
    public ResponseEntity getAllClubDocuments()
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(clubDocumentRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/club_documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateClubDocument(@RequestBody ClubDocument clubDocument)
    {
        return clubDocumentRepository.findById(clubDocument.getId())
                .map(existingClubDocument -> ResponseEntity.ok(clubDocumentRepository.save(clubDocument)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/club_documents/{id}")
    public ResponseEntity deleteClubDocument(@PathVariable Integer id)
    {
        return clubDocumentRepository.findById(id)
                .map(clubDocument -> {
                    clubDocumentRepository.delete(clubDocument);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
