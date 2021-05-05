package com.app.em.controller.club_document;

import com.app.em.persistence.entity.club_document.ClubDocument;
import com.app.em.persistence.repository.club_document.ClubDocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
class ClubDocumentControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClubDocumentRepository clubDocumentRepository;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void addClubDocument() throws Exception
    {
        ClubDocument newClubDocument = new ClubDocument();
        newClubDocument.setClubDocumentDescription("document description");
        newClubDocument.setClubDocumentPath("document_path");
        ClubDocument savedClubDocument = new ClubDocument(1, "document description", "document_path");

        Mockito.when( clubDocumentRepository.save(any()) ).thenReturn(savedClubDocument);

        RequestBuilder request = post("/club_documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(newClubDocument));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(savedClubDocument)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getClubDocument() throws Exception
    {
        ClubDocument clubDocument = new ClubDocument(1, "document description", "document_path");

        Mockito.when(clubDocumentRepository.findById(1)).thenReturn(Optional.of(clubDocument));

        RequestBuilder request = get("/club_documents/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(clubDocument)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void getAllClubDocuments() throws Exception
    {
        List<ClubDocument> clubDocuments = new ArrayList<>();
        clubDocuments.add(new ClubDocument(0, "first document description", "first_document_path"));
        clubDocuments.add(new ClubDocument(1, "second document description", "second_document_path"));
        clubDocuments.add(new ClubDocument(2, "third document description", "third_document_path"));

        Mockito.when(clubDocumentRepository.findAll()).thenReturn(clubDocuments);

        RequestBuilder request = get("/club_documents");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(clubDocuments)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateClubDocument() throws Exception
    {
        ClubDocument existingClubDocument = new ClubDocument(1, "document description", "document_path");
        ClubDocument updatedClubDocument = new ClubDocument(1, "updated document description", "updated_document_path");

        Mockito.when(clubDocumentRepository.findById(1)).thenReturn(Optional.of(existingClubDocument));
        Mockito.when(clubDocumentRepository.save(any())).thenReturn(updatedClubDocument);

        RequestBuilder request = put("/club_documents")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(existingClubDocument));

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(updatedClubDocument)))
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteClubDocument() throws Exception
    {
        ClubDocument existingClubDocument = new ClubDocument(1, "document description", "document_path");

        Mockito.when(clubDocumentRepository.findById(1)).thenReturn(Optional.of(existingClubDocument));
        Mockito.doNothing().when(clubDocumentRepository).delete(existingClubDocument);

        RequestBuilder request = delete("/club_documents/1");
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Mockito.verify(clubDocumentRepository, times(1)).delete(existingClubDocument);
    }
}