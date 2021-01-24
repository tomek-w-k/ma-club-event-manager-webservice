package com.app.em.persistence.entity.club_document;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "club_document")
public class ClubDocument
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Lob
    @NotBlank
    @Column(name = "club_document_description")
    private String clubDocumentDescription;

    @Column(name = "club_document_path")
    private String clubDocumentPath;


    public ClubDocument() {  }

    public ClubDocument(Integer id, String clubDocumentDescription, String clubDocumentPath)
    {
        this.id = id;
        this.clubDocumentDescription = clubDocumentDescription;
        this.clubDocumentPath = clubDocumentPath;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getClubDocumentDescription()
    {
        return clubDocumentDescription;
    }

    public void setClubDocumentDescription(String clubDocumentDescription)
    {
        this.clubDocumentDescription = clubDocumentDescription;
    }

    public String getClubDocumentPath()
    {
        return clubDocumentPath;
    }

    public void setClubDocumentPath(String clubDocumentPath)
    {
        this.clubDocumentPath = clubDocumentPath;
    }
}
