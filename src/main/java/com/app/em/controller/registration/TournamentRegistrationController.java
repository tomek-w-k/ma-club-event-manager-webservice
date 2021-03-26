package com.app.em.controller.registration;

import com.app.em.persistence.entity.registration.TournamentRegistration;
import com.app.em.persistence.entity.team.Team;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.persistence.repository.registration.TeamRepository;
import com.app.em.persistence.repository.registration.TournamentRegistrationRepository;
import com.app.em.persistence.repository.room_type.RoomTypeRepository;
import com.app.em.persistence.repository.stay_period.StayPeriodRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.persistence.repository.weight_age_category.WeightAgeCategoryRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;


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

    @Autowired
    ListToResponseEntityWrapper listToResponseEntityWrapper;


    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping("/teams/{teamId}/tournament_registrations")
    public ResponseEntity addTournamentRegistration(@RequestBody TournamentRegistration tournamentRegistration, @PathVariable Long teamId)
    {
        return teamRepository.findById(teamId)
                .map(team -> {
                    tournamentRegistration.setTeam(team);
                    return tournamentEventRepository.findTournamentEventByTeams(team).get();
                })
                .map(tournamentEvent -> {
                    return userRepository.findByEmail(tournamentRegistration.getUser().getEmail()) // Check if a user with the given email exists, if so - get it, if not - create and get it
                            .map(user -> {
                                return tournamentEvent.getTeams().stream() // If the user exists, check if it is already registered for this team's tournament event
                                    .map(Team::getTournamentRegistrations)
                                    .flatMap(Collection::stream)
                                    .filter(existingTournamentRegistration -> existingTournamentRegistration.getUser().getEmail().equals(user.getEmail()))
                                    .findAny()
                                    .map(this::participantAlreadyRegisteredResponse)
                                    .orElseGet(() -> {
                                        tournamentRegistration.setUser(user);
                                        return ResponseEntity.ok( tournamentRegistrationRepository.save(tournamentRegistration) );
                                    });
                            })
                            .orElseGet(() -> {
                                tournamentRegistration.getUser().setPassword(UUID.randomUUID().toString());
                                tournamentRegistration.setUser( userRepository.save(tournamentRegistration.getUser()) );
                                return ResponseEntity.ok( tournamentRegistrationRepository.save(tournamentRegistration) );
                            });
                }).orElseGet(this::eventDoesNotExistResponse);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping("/tournament_registrations/{id}")
    public ResponseEntity getTournamentRegistrationById(@PathVariable Long id)
    {
        return tournamentRegistrationRepository.findById(id)
                .map(tournamentRegistration -> {
                    JSONObject registrationJson;
                    try
                    {
                        registrationJson = new JSONObject(objectMapper.writeValueAsString(tournamentRegistration));
                        JSONObject teamJson = new JSONObject();
                        teamJson.put("id", tournamentRegistration.getTeam().getId());
                        registrationJson.put("team", teamJson);
                    }
                    catch (JsonProcessingException e) { throw new RuntimeException(e); }
                    catch (JSONException e) { throw new RuntimeException(e); }

                    return ResponseEntity.ok( registrationJson.toString() );
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/tournament_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentRegistrationsForTournament(@PathVariable Long tournamentEventId)
    {
        return tournamentEventRepository.findById(tournamentEventId)
                .map(tournamentEvent -> teamRepository.findByTournamentEvent(tournamentEvent))
                .map(teams -> {
                    List<JSONObject> jsonObjects = teams.stream()
                        .map(team -> team.getTournamentRegistrations())
                        .flatMap(regList -> regList.stream())
                        .map(registration -> {
                            JSONObject registrationJson;
                            try
                            {
                                registrationJson = new JSONObject(objectMapper.writeValueAsString(registration));
                                registrationJson.put("teamId", registration.getTeam().getId());
                                registrationJson.put("trainerFullName",
                                        registration.getTeam().getTrainer() != null ?
                                                registration.getTeam().getTrainer().getFullName() : "none");
                                registrationJson.put("trainerClubName",
                                        registration.getTeam().getTrainer().getClub() != null ?
                                                registration.getTeam().getTrainer().getClub().getClubName() : "none");
                            }
                            catch (JsonProcessingException e) { throw new RuntimeException(e); }
                            catch (JSONException e) { throw new RuntimeException(e); }

                            return registrationJson;
                        }).collect(Collectors.toList());

                    return ResponseEntity.ok( new JSONArray(jsonObjects).toString() );
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/users/{userId}/tournament_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentRegistrationsForUser(@PathVariable Long userId)
    {
        return userRepository.findById(userId)
                .map(foundUser -> tournamentRegistrationRepository.findByUserId(foundUser.getId()))
                .map(tournamentRegistrations -> {
                    List<JSONObject> jsonObjects = tournamentRegistrations.stream()
                            .map(registration -> {
                                JSONObject registrationJson;
                                try
                                {
                                    registrationJson = new JSONObject(objectMapper.writeValueAsString(registration));
                                    registrationJson.put("eventId", registration.getTeam().getTournamentEvent().getId());
                                    registrationJson.put("eventName", registration.getTeam().getTournamentEvent().getEventName());
                                }
                                catch (JsonProcessingException e) { throw new RuntimeException((e)); }
                                catch (JSONException e) { throw new RuntimeException(e); }

                                return registrationJson;
                            }).collect(Collectors.toList());

                    return ResponseEntity.ok( new JSONArray(jsonObjects).toString() );
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "teams/{teamId}/tournament_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTournamentRegistrationsForTeam(@PathVariable Long teamId)
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(tournamentRegistrationRepository.findByTeamId(teamId));
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/room_types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRoomTypesForTournament(@PathVariable Long tournamentEventId)
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(roomTypeRepository.findByTournamentEventId(tournamentEventId));
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/stay_periods")
    public ResponseEntity getStayPeriodsForTournament(@PathVariable Long tournamentEventId)
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(stayPeriodRepository.findByTournamentEventId(tournamentEventId));
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/weight_age_categories")
    public ResponseEntity getWeightAgeCategoriesForTournament(@PathVariable Long tournamentEventId)
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(weightAgeCategoryRepository.findByTournamentEventId(tournamentEventId));
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping("/teams/{teamId}/tournament_registrations")
    public ResponseEntity updateTournamentRegistration(@RequestBody TournamentRegistration tournamentRegistration)
    {
        return tournamentRegistrationRepository.findById(tournamentRegistration.getId())
                .map(existingTournamentRegistration -> ResponseEntity.ok(tournamentRegistrationRepository.save(tournamentRegistration)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TRAINER')")
    @DeleteMapping("/tournament_registrations/{id}")
    public ResponseEntity deleteTournamentRegistration(@PathVariable Long id)
    {
        return tournamentRegistrationRepository.findById(id)
                .map(tournamentRegistration -> {
                    tournamentRegistrationRepository.delete(tournamentRegistration);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/stay_periods/{stayPeriodId}/tournament_registrations")
    public ResponseEntity getTournamentRegistrationsCountForStayPeriod(@PathVariable Integer stayPeriodId)
    {
        Integer stayPeriodCount = tournamentRegistrationRepository.countTournamentRegistrationByStayPeriodId(stayPeriodId);
        return ResponseEntity.ok(Collections.singletonMap("stayPeriodCount", stayPeriodCount));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/room_types/{roomTypeId}/tournament_registrations")
    public ResponseEntity getTournamentRegistrationsCountForRoomType(@PathVariable Integer roomTypeId)
    {
        Integer roomTypeCount = tournamentRegistrationRepository.countTournamentRegistrationByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(Collections.singletonMap("roomTypeCount", roomTypeCount));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/weight_age_categories/{weightAgeCategoryId}/tournament_registrations")
    public ResponseEntity getTournamentRegistrationsCountForWeightAgeCategory(@PathVariable Integer weightAgeCategoryId)
    {
        Integer weightAgeCategoryCount = tournamentRegistrationRepository.countTournamentRegistrationByWeightAgeCategoryId(weightAgeCategoryId);
        return ResponseEntity.ok(Collections.singletonMap("weightAgeCategoryCount", weightAgeCategoryCount));
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity registrationAlreadyExists(String who, String forWhat)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }

    private ResponseEntity teamDoesNotExistResponse()
    {
        return ResponseEntity.badRequest().body(new MessageResponse("Error - A given team doesn't exist."));
    }

    private ResponseEntity eventDoesNotExistResponse()
    {
        return ResponseEntity.badRequest().body(new MessageResponse("Error - A given event doesn't exist"));
    }

    private ResponseEntity participantAlreadyRegisteredResponse(TournamentRegistration tournamentRegistration)
    {
        return ResponseEntity.badRequest().body(new MessageResponse("Error - A given participant is already registered for this event"));
    }
}
