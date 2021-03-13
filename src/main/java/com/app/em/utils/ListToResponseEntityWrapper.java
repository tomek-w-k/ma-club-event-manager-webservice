package com.app.em.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.List;


@Component
public class ListToResponseEntityWrapper
{
    @Autowired
    ObjectMapper objectMapper;


    public ResponseEntity wrapListInResponseEntity(List<?> items)
    {
        try {
            return ResponseEntity.ok(objectMapper.writeValueAsString(items));
        } catch (JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
