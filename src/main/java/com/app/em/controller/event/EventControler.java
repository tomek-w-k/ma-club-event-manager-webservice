package com.app.em.controller.event;

import com.app.em.persistence.entity.event.CampEvent;
import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.event.ExamEvent;
import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.repository.event.CampEventRepository;
import com.app.em.persistence.repository.event.EventRepository;
import com.app.em.persistence.repository.event.ExamEventRepository;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class EventControler
{
    @Autowired
    EventRepository eventRepository;

    @Autowired
    ExamEventRepository examEventRepository;

    @Autowired
    CampEventRepository campEventRepository;

    @Autowired
    TournamentEventRepository tournamentEventRepository;

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    // - - - - EXAM EVENT - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/exam_events")
    public ResponseEntity addExamEvent(@RequestBody ExamEvent examEvent)
    {
        return addEvent(examEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/exam_events/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamEvent(@PathVariable Long id)
    {
        return getEvent(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/exam_events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllExamEvents()
    {
        return getAllEvents(ExamEvent.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/exam_events")
    public ResponseEntity updateExamEvent(@RequestBody ExamEvent examEvent)
    {
        return updateEvent(examEvent);
    }


    // - - - - CAMP EVENT - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/camp_events")
    public ResponseEntity addCampEvent(@RequestBody CampEvent campEvent)
    {
        return addEvent(campEvent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/camp_events/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCampEvent(@PathVariable Long id)
    {
        return getEvent(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/camp_events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllCampEvents()
    {
        return getAllEvents(CampEvent.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/camp_events")
    public ResponseEntity updateCampEvent(@RequestBody CampEvent campEvent)
    {
        return updateEvent(campEvent);
    }


    // - - - - TOURNAMENT EVENT - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tournament_events")
    public ResponseEntity addTournamentEvent(@RequestBody TournamentEvent tournamentEvent)
    {
        return addEvent(tournamentEvent);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/tournament_events/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentEvent(@PathVariable Long id)
    {
        return getEvent(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/tournament_events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTournamentEvents()
    {
        return getAllEvents(TournamentEvent.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/tournament_events")
    public ResponseEntity updateTournamentEvent(@RequestBody TournamentEvent tournamentEvent)
    {
        return updateEvent(tournamentEvent);
    }


    // - - - - MISC - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllEventsForWall() throws JsonProcessingException
    {
        return getAllEvents();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/events/{id}")
    public ResponseEntity removeEvent(@PathVariable Long id)
    {
        return deleteEvent(id);
    }


    // - - - - EXCEPTION HANDLERS - - - -

    @ExceptionHandler({ SQLIntegrityConstraintViolationException.class })
    public ResponseEntity handleException()
    {
        return ResponseEntity.badRequest().body(new MessageResponse("Items you want to remove are chosen by one or more users."));
    }

    // - - - - PRIVATE METHODS - - - -

    private <T extends Event> ResponseEntity addEvent(T event)
    {
        try {
            return ResponseEntity.ok( eventRepository.save(event) );
        }
        catch(ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("The submitted data is not valid. Please correct it and try again."));
        }
    }

    private ResponseEntity getEvent(Long id)
    {
        return ResponseEntity.of( eventRepository.findById(id) );
    }

    private ResponseEntity getAllEvents()
    {
        return Optional.ofNullable( eventRepository.findAllByOrderByDateCreatedDesc() )
            .map(listToResponseEntityWrapper::wrapListInResponseEntity)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity getAllEvents(Class<? extends Event> eventType)
    {
        if ( eventType.isAssignableFrom(ExamEvent.class) )
            return Optional.ofNullable( examEventRepository.findAllByOrderByDateCreatedDesc() )
                    .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        if ( eventType.isAssignableFrom(CampEvent.class) )
            return Optional.ofNullable( campEventRepository.findAllByOrderByDateCreatedDesc() )
                    .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        if ( eventType.isAssignableFrom(TournamentEvent.class) )
            return Optional.ofNullable( tournamentEventRepository.findAllByOrderByDateCreatedDesc() )
                    .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());

        return ResponseEntity.notFound().build();
    }

    private  ResponseEntity updateEvent(Event event)
    {
        return eventRepository.findById(event.getId())
                .map(eventToUpdate -> ResponseEntity.ok(eventRepository.save(event)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity deleteEvent(Long id)
    {
        return eventRepository.findById(id)
                .map(event -> {
                    eventRepository.delete(event);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
