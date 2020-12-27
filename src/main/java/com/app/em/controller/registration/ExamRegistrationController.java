package com.app.em.controller.registration;

import com.app.em.persistence.entity.event.ExamEvent;
import com.app.em.persistence.entity.registration.ExamRegistration;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.event.EventRepository;
import com.app.em.persistence.repository.event.ExamEventRepository;
import com.app.em.persistence.repository.registration.ExamRegistrationRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExamRegistrationController
{
    @Autowired
    ExamRegistrationRepository examRegistrationRepository;

    @Autowired
    ExamEventRepository examEventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;


    // CREATE : addExamRegistration()
    @PostMapping("/exam_registrations")
    public ResponseEntity addExamRegistration(@RequestBody ExamRegistration examRegistration)
    {
        Optional<ExamRegistration> examRegistrationOptional = examRegistrationRepository
                .findByUserIdAndExamEventId(examRegistration.getUser().getId(), examRegistration.getExamEvent().getId());
        if ( examRegistrationOptional.isPresent() )
        {
            Optional<User> userOptional = userRepository.findById(examRegistration.getUser().getId());
            Optional<ExamEvent> examEventOptional = examEventRepository.findById(examRegistration.getExamEvent().getId());
            if ( userOptional.isPresent() && examEventOptional.isPresent() )
                return registrationAlreadyExists(userOptional.get().getFullName(), examEventOptional.get().getEventName());
        }

        ExamRegistration savedExamRegistration = examRegistrationRepository.save(examRegistration);
        return ResponseEntity.ok(savedExamRegistration);
    }

    // READ :   getExamRegistrationById(),
    //          getExamRegistrationsForExam()
    //          getExamRegistrationsForUser()
    @GetMapping("/exam_registrations/{id}")
    public ResponseEntity getExamRegistrationById(@PathVariable Long id)
    {
        Optional<ExamRegistration> examRegistrationOptional = examRegistrationRepository.findById(id);
        if ( examRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( examRegistrationOptional.get() );
    }

    @GetMapping(value = "/exam_events/{examEventId}/exam_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamRegistrationsForExam(@PathVariable Long examEventId) throws JsonProcessingException
    {
        Optional<List<ExamRegistration>> examRegistrationsOptional = Optional.ofNullable(
                examRegistrationRepository.findByExamEventId(examEventId) );
        if ( examRegistrationsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(examRegistrationsOptional.get()) );
    }

    @GetMapping(value = "/users/{userId}/exam_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamRegistrationsForUser(@PathVariable Long userId) throws JsonProcessingException
    {
        Optional<List<ExamRegistration>> examRegistrationsOptional = Optional.ofNullable(
                examRegistrationRepository.findByUserId(userId) );
        if ( examRegistrationsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(examRegistrationsOptional.get()) );
    }

    // UPDATE :
    @PutMapping("/exam_registrations")
    public ResponseEntity updateExamRegistration(@RequestBody ExamRegistration examRegistration)
    {
        Optional<ExamRegistration> examRegistrationOptional = examRegistrationRepository.findById( examRegistration.getId() );
        if ( examRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Optional<ExamEvent> examEventOptional = examEventRepository.findExamEventByExamRegistrations(examRegistration);
        if ( examEventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("There are no event specified for this registration."));

        examRegistration.setExamEvent(examEventOptional.get());
        ExamRegistration updatedExamRegistration = examRegistrationRepository.save(examRegistration);

        return ResponseEntity.ok(updatedExamRegistration);
    }

    // DELETE : deleteExamRegistration()
    @DeleteMapping("/exam_registrations/{id}")
    public ResponseEntity deleteExamRegistration(@PathVariable Long id)
    {
        Optional<ExamRegistration> examRegistrationOptional = examRegistrationRepository.findById(id);
        if ( examRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        examRegistrationRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


    // - - - PRIVATE METHODS - - -

    private ResponseEntity registrationAlreadyExists(String who, String forWhat)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }
}
