package com.app.em.controller.registration;

import com.app.em.persistence.entity.event.RoomType;
import com.app.em.persistence.entity.event.StayPeriod;
import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.event.WeightAgeCategory;
import com.app.em.persistence.entity.registration.TournamentRegistration;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.persistence.repository.registration.TournamentRegistrationRepository;
import com.app.em.persistence.repository.room_type.RoomTypeRepository;
import com.app.em.persistence.repository.stay_period.StayPeriodRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.persistence.repository.weight_age_category.WeightAgeCategoryRepository;
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
public class TournamentRegistrationController
{
    @Autowired
    TournamentRegistrationRepository tournamentRegistrationRepository;

    @Autowired
    TournamentEventRepository tournamentEventRepository;

    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    StayPeriodRepository stayPeriodRepository;

    @Autowired
    WeightAgeCategoryRepository weightAgeCategoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;


    // CREATE :     addTournamentRegistration()
    @PostMapping("/tournament_registrations")
    public ResponseEntity addTournamentRegistration(@RequestBody TournamentRegistration tournamentRegistration)
    {
        Optional<TournamentRegistration> tournamentRegistrationOptional = tournamentRegistrationRepository
                .findByUserIdAndTournamentEventId(tournamentRegistration.getUser().getId(), tournamentRegistration.getTournamentEvent().getId());
        if ( tournamentRegistrationOptional.isPresent() )
        {
            Optional<User> userOptional = userRepository.findById(tournamentRegistration.getUser().getId());
            Optional<TournamentEvent> tournamentEventOptional = tournamentEventRepository.findById(tournamentRegistration.getTournamentEvent().getId());
            if ( userOptional.isPresent() && tournamentEventOptional.isPresent() )
                return registrationAlreadyExists(userOptional.get().getFullName(), tournamentEventOptional.get().getEventName());
        }

        TournamentRegistration savedTournamentRegistration = tournamentRegistrationRepository.save(tournamentRegistration);
        return ResponseEntity.ok(savedTournamentRegistration);
    }

    // READ :       getTournamentRegistrationById()             v
    //              getTournamentRegistrationsForTournament()   v
    //              getTournamentRegistrationsForUser()         v
    //              getRoomTypesForTournament()                 v
    //              getStayPeriodsForTournament()               v
    //              getWeightAgeCategoriesForTournament()       v
    @GetMapping("/tournament_registrations/{id}")
    public ResponseEntity getTournamentRegistrationById(@PathVariable Long id)
    {
        Optional<TournamentRegistration> tournamentRegistrationOptional = tournamentRegistrationRepository.findById(id);
        if ( tournamentRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( tournamentRegistrationOptional.get() );
    }

    @GetMapping(value = "/tournament_events/{tournamentEventId}/tournament_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentRegistrationsForTournament(@PathVariable Long tournamentEventId) throws JsonProcessingException
    {
        Optional<List<TournamentRegistration>> tournamentRegistrationsOptional = Optional.ofNullable(
                tournamentRegistrationRepository.findByTournamentEventId(tournamentEventId) );
        if ( tournamentRegistrationsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(tournamentRegistrationsOptional.get()) );
    }

    @GetMapping(value = "/users/{userId}/tournament_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentRegistrationsForUser(@PathVariable Long userId) throws JsonProcessingException
    {
        Optional<List<TournamentRegistration>> tournamentRegistrationsOptional = Optional.ofNullable(
                tournamentRegistrationRepository.findByUserId(userId) );
        if ( tournamentRegistrationsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(tournamentRegistrationsOptional.get()) );
    }

    @GetMapping(value = "/tournament_events/{tournamentEventId}/room_types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRoomTypesForTournament(@PathVariable Long tournamentEventId) throws JsonProcessingException
    {
        Optional<List<RoomType>> roomTypesOptional = Optional.ofNullable(roomTypeRepository.findByTournamentEventId(tournamentEventId));
        if ( roomTypesOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(roomTypesOptional.get()) );
    }

    @GetMapping(value = "/tournament_events/{tournamentEventId}/stay_periods")
    public ResponseEntity getStayPeriodsForTournament(@PathVariable Long tournamentEventId) throws JsonProcessingException
    {
        Optional<List<StayPeriod>> stayPeriodsOptional = Optional.ofNullable(stayPeriodRepository.findByTournamentEventId(tournamentEventId));
        if ( stayPeriodsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(stayPeriodsOptional.get()) );
    }

    @GetMapping(value = "/tournament_events/{tournamentEventId}/weight_age_categories")
    public ResponseEntity getWeightAgeCategoriesForTournament(@PathVariable Long tournamentEventId) throws JsonProcessingException
    {
        Optional<List<WeightAgeCategory>> weightAgeCategoriesOptional = Optional.ofNullable(weightAgeCategoryRepository.findByTournamentEventId(tournamentEventId));
        if ( weightAgeCategoriesOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(weightAgeCategoriesOptional.get()) );
    }

    // UPDATE :     updateTournamentRegistration()
    @PutMapping("/tournament_registrations")
    public ResponseEntity updateTournamentRegistration(@RequestBody TournamentRegistration tournamentRegistration)
    {
        Optional<TournamentRegistration> tournamentRegistrationOptional = tournamentRegistrationRepository.findById(tournamentRegistration.getId());
        if ( tournamentRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Optional<TournamentEvent> tournamentEventOptional = tournamentEventRepository.findTournamentEventByTournamentRegistrations(tournamentRegistration);
        if ( tournamentEventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("There are no event specified for this registration."));

        tournamentRegistration.setTournamentEvent(tournamentEventOptional.get());
        TournamentRegistration updatedTournamentRegistration = tournamentRegistrationRepository.save(tournamentRegistration);

        return ResponseEntity.ok(updatedTournamentRegistration);
    }

    // DELETE :     deleteTournamentRegistration()
    @DeleteMapping("/tournament_registrations/{id}")
    public ResponseEntity deleteTournamentRegistration(@PathVariable Long id)
    {
        Optional<TournamentRegistration> tournamentRegistrationOptional = tournamentRegistrationRepository.findById(id);
        if ( tournamentRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        tournamentRegistrationRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }


    // - - - PRIVATE METHODS - - -

    private ResponseEntity registrationAlreadyExists(String who, String forWhat)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }
}
