package com.app.em.persistence.repository.club_document;

import com.app.em.persistence.entity.club_document.ClubDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClubDocumentRepository extends CrudRepository<ClubDocument, Integer>
{
    List<ClubDocument> findAll();
}
