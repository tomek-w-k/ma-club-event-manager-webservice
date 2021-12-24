package com.app.em.controller.registration;

import com.app.em.persistence.entity.registration.ExamRegistration;
import com.app.em.persistence.repository.event.ExamEventRepository;
import com.app.em.persistence.repository.registration.ExamRegistrationRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExamRegistrationController
{
    private final ExamRegistrationRepository examRegistrationRepository;
    private final ExamEventRepository examEventRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public ExamRegistrationController(  ExamRegistrationRepository examRegistrationRepository,
                                        ExamEventRepository examEventRepository,
                                        UserRepository userRepository,
                                        ObjectMapper objectMapper,
                                        ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.examRegistrationRepository = examRegistrationRepository;
        this.examEventRepository = examEventRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/exam_registrations")
    public ResponseEntity addExamRegistration(@RequestBody ExamRegistration examRegistration)
    {
        return examRegistrationRepository.findByUserIdAndExamEventId(examRegistration.getUser().getId(), examRegistration.getExamEvent().getId())
                .map(existingExamRegistration -> {
                    return examEventRepository.findById(existingExamRegistration.getExamEvent().getId())
                            .map(examEvent -> registrationAlreadyExists(existingExamRegistration.getUser().getFullName(), examEvent.getEventName()))
                            .orElseGet(() -> ResponseEntity.badRequest().build());
                }).orElseGet(() -> ResponseEntity.ok( examRegistrationRepository.save(examRegistration) ));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/exam_registrations/{id}")
    public ResponseEntity getExamRegistrationById(@PathVariable Long id)
    {
        return ResponseEntity.of( examRegistrationRepository.findById(id) );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/exam_events/{examEventId}/exam_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamRegistrationsForExam(@PathVariable Long examEventId)
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(examRegistrationRepository.findByExamEventId(examEventId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/users/{userId}/exam_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamRegistrationsForUser(@PathVariable Long userId)
    {
        return userRepository.findById(userId)
                .map(foundUser -> examRegistrationRepository.findByUserId(foundUser.getId()))
                .map(examRegistrations -> {
                    List<JSONObject> jsonObjects = examRegistrations.stream()
                            .map(registration -> {
                                JSONObject registrationJson;
                                try
                                {
                                    registrationJson = new JSONObject(objectMapper.writeValueAsString(registration));
                                    registrationJson.put("eventId", registration.getExamEvent().getId());
                                    registrationJson.put("eventName", registration.getExamEvent().getEventName());
                                }
                                catch (JsonProcessingException e) { throw new RuntimeException((e)); }
                                catch (JSONException e) { throw new RuntimeException(e); }

                                return registrationJson;
                            }).collect(Collectors.toList());

                    return ResponseEntity.ok( new JSONArray(jsonObjects).toString() );
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/exam_registrations")
    public ResponseEntity updateExamRegistration(@RequestBody ExamRegistration examRegistration)
    {
        return examRegistrationRepository.findById(examRegistration.getId())
                .map(examRegistrationToUpdate -> {
                    return examEventRepository.findExamEventByExamRegistrations(examRegistration)
                            .map(examEvent -> {
                                examRegistration.setExamEvent(examEvent);
                                return ResponseEntity.ok(examRegistrationRepository.save(examRegistration));
                            }).orElseGet(() -> ResponseEntity.notFound().build());
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/exam_registrations/{id}")
    public ResponseEntity deleteExamRegistration(@PathVariable Long id)
    {
        return examRegistrationRepository.findById(id)
                .map(examRegistration -> {
                    examRegistrationRepository.delete(examRegistration);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // - - - PRIVATE METHODS - - -

    private ResponseEntity registrationAlreadyExists(String who, String forWhat)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }
}
