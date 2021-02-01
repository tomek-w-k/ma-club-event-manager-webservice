package com.app.em.controller.registration;

import com.app.em.persistence.entity.event.RoomType;
import com.app.em.persistence.entity.event.StayPeriod;
import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.event.WeightAgeCategory;
import com.app.em.persistence.entity.registration.TournamentRegistration;
import com.app.em.persistence.entity.team.Team;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.persistence.repository.registration.TeamRepository;
import com.app.em.persistence.repository.registration.TournamentRegistrationRepository;
import com.app.em.persistence.repository.room_type.RoomTypeRepository;
import com.app.em.persistence.repository.stay_period.StayPeriodRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.persistence.repository.weight_age_category.WeightAgeCategoryRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    TeamRepository teamRepository;

    @Autowired
    ObjectMapper objectMapper;


    // CREATE :     addTournamentRegistration()
    @PostMapping("/teams/{teamId}/tournament_registrations")
    public ResponseEntity addTournamentRegistration(@RequestBody TournamentRegistration tournamentRegistration, @PathVariable Long teamId)
    {
        User participantToRegister = tournamentRegistration.getUser();

        // Check if both team and its event exist
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        if (teamOptional.isPresent())
        {
            Optional<TournamentEvent> tournamentEventOptional = tournamentEventRepository.findTournamentEventByTeams(teamOptional.get());
            if (tournamentEventOptional.isPresent())
            {
                // Check if a user with the given email exists, if not - create and get it, if so - get it
                Optional<User> userOptional = userRepository.findByEmail(participantToRegister.getEmail());
                if (userOptional.isEmpty())
                {
                    participantToRegister.setPassword(UUID.randomUUID().toString());
                    participantToRegister = userRepository.save(participantToRegister);
                }
                else
                {
                    participantToRegister = userOptional.get();

                    // If the user exists, check if it is already registered for this team's tournament event
                    final User finalParticipantToRegister = participantToRegister;
                    List<Boolean> results = tournamentEventOptional.get().getTeams().stream().map(team -> {
                        return team.getTournamentRegistrations().stream().anyMatch(registration -> {
                            return registration.getUser().getEmail().equals(finalParticipantToRegister.getEmail());
                        });
                    }).collect(Collectors.toList());

                    if (results.contains(Boolean.TRUE))
                        return ResponseEntity.badRequest().body(new MessageResponse("Error - A given participant is already registered for this event"));
                }
            }
            else return ResponseEntity.badRequest().body(new MessageResponse("Error - A given event doesn't exist"));

            tournamentRegistration.setTeam(teamOptional.get());
        }
        else return ResponseEntity.badRequest().body(new MessageResponse("Error - A given team doesn't exist."));

        tournamentRegistration.setUser(participantToRegister);
        TournamentRegistration savedTournamentRegistration = tournamentRegistrationRepository.save(tournamentRegistration);
        return ResponseEntity.ok(savedTournamentRegistration);
    }

    // READ :       getTournamentRegistrationById()             v
    //              getTournamentRegistrationsForTournament()   v
    //              getTournamentRegistrationsForUser()         v
    //              getTournamentRegistrationsForTeam()         v
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
        Optional<TournamentEvent> tournamentEventOptional = tournamentEventRepository.findById(tournamentEventId);
        if ( tournamentEventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("A given event doesn't exist."));

        Optional<List<Team>> teamsOptional = Optional.ofNullable(teamRepository.findByTournamentEvent(tournamentEventOptional.get()));
        if ( teamsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        List<TournamentRegistration> registrations =
                teamsOptional.get().stream().map(team -> team.getTournamentRegistrations())
                                            .flatMap(regList -> regList.stream())
                                            .collect(Collectors.toList());

        return ResponseEntity.ok( objectMapper.writeValueAsString(registrations) );
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

    @GetMapping(value = "teams/{teamId}/tournament_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentRegistrationsForTeam(@PathVariable Long teamId) throws JsonProcessingException
    {
        Optional<List<TournamentRegistration>> tournamentRegistrationsOptional =
                Optional.ofNullable( tournamentRegistrationRepository.findByTeamId(teamId) );
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
    @PutMapping("/teams/{teamId}/tournament_registrations")
    public ResponseEntity updateTournamentRegistration(@RequestBody TournamentRegistration tournamentRegistration)
    {
        Optional<TournamentRegistration> tournamentRegistrationOptional = tournamentRegistrationRepository.findById(tournamentRegistration.getId());
        if ( tournamentRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

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

    @GetMapping(value = "/stay_periods/{stayPeriodId}/tournament_registrations")
    public ResponseEntity getTournamentRegistrationsCountForStayPeriod(@PathVariable Integer stayPeriodId)
    {
        Integer stayPeriodCount = tournamentRegistrationRepository.countTournamentRegistrationByStayPeriodId(stayPeriodId);
        return ResponseEntity.ok(Collections.singletonMap("stayPeriodCount", stayPeriodCount));
    }

    @GetMapping(value = "/room_types/{roomTypeId}/tournament_registrations")
    public ResponseEntity getTournamentRegistrationsCountForRoomType(@PathVariable Integer roomTypeId)
    {
        Integer roomTypeCount = tournamentRegistrationRepository.countTournamentRegistrationByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(Collections.singletonMap("roomTypeCount", roomTypeCount));
    }

    @GetMapping(value = "/weight_age_categories/{weightAgeCategoryId}/tournament_registrations")
    public ResponseEntity getTournamentRegistrationsCountForWeightAgeCategory(@PathVariable Integer weightAgeCategoryId)
    {
        Integer weightAgeCategoryCount = tournamentRegistrationRepository.countTournamentRegistrationByWeightAgeCategoryId(weightAgeCategoryId);
        return ResponseEntity.ok(Collections.singletonMap("weightAgeCategoryCount", weightAgeCategoryCount));
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity registrationAlreadyExists(String who, String forWhat)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }
}
