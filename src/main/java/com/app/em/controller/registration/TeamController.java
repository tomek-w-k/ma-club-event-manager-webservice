package com.app.em.controller.registration;

import com.app.em.persistence.entity.team.Team;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.persistence.repository.registration.TeamRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.app.em.utils.ListToResponseEntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeamController
{
    private final TeamRepository teamRepository;
    private final TournamentEventRepository tournamentEventRepository;
    private final ObjectMapper objectMapper;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public TeamController(  TeamRepository teamRepository,
                            TournamentEventRepository tournamentEventRepository,
                            ObjectMapper objectMapper,
                            ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.teamRepository = teamRepository;
        this.tournamentEventRepository = tournamentEventRepository;
        this.objectMapper = objectMapper;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTeam(@RequestBody Team team, @PathVariable Long tournamentEventId)
    {
        return tournamentEventRepository.findById(tournamentEventId)
                .map(tournamentEvent -> {
                    return tournamentEvent.getTeams().stream()
                            .filter(existingTeam -> existingTeam.getTrainer().getId() == team.getTrainer().getId())
                            .findAny()
                            .map(this::trainerAlreadyHasTeamResponse)
                            .orElseGet(() -> {
                                team.setTournamentEvent(tournamentEvent);
                                return ResponseEntity.ok(teamRepository.save(team));
                            });
                }).orElseGet(() -> ResponseEntity.badRequest().body(new MessageResponse("Event specified for a given team doesn't exist.")));
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTeam(@PathVariable Long id)
    {
        return teamRepository.findById(id)
                .map(team -> {
                    JSONObject teamJson;
                    try
                    {
                        teamJson = new JSONObject(objectMapper.writeValueAsString(team));
                        teamJson.put("eventId", team.getTournamentEvent().getId());
                        teamJson.put("eventName", team.getTournamentEvent().getEventName());
                    }
                    catch (JsonProcessingException e) { throw new RuntimeException(e); }
                    catch (JSONException e) { throw new RuntimeException(e); }

                    return ResponseEntity.ok( teamJson.toString() );
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTeamsForEvent(@PathVariable Long tournamentEventId)
    {
        return tournamentEventRepository.findById(tournamentEventId)
                .map(tournamentEvent -> teamRepository.findByTournamentEvent(tournamentEvent) )
                .map(listToResponseEntityWrapper::wrapListInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/user/{userId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTeamsForUser(@PathVariable Long userId)
    {
        List<JSONObject> allTeams = teamRepository.findByTrainerId(userId)
                .stream()
                .map(team -> {
                    JSONObject teamJson;
                    try
                    {
                        teamJson = new JSONObject(objectMapper.writeValueAsString(team));
                        teamJson.put("eventId", team.getTournamentEvent().getId());
                        teamJson.put("eventName", team.getTournamentEvent().getEventName());
                    }
                    catch (JsonProcessingException e) { throw new RuntimeException(e); }
                    catch (JSONException e) { throw new RuntimeException(e); }

                    return teamJson;
                }).collect(Collectors.toList());

        return ResponseEntity.ok( new JSONArray(allTeams).toString() );
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTeam(@RequestBody Team team)
    {
        return teamRepository.findById(team.getId())
                .map(existingTeam -> ResponseEntity.ok(teamRepository.save(team)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TRAINER')")
    @DeleteMapping("/user/{userId}/teams/{id}")
    public ResponseEntity deleteTeam(@PathVariable Long id)
    {
        return teamRepository.findById(id)
                .map(team -> {
                    teamRepository.delete(team);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // - - - - PRIVATE METHODS - - - -

    private ResponseEntity trainerAlreadyHasTeamResponse(Team team)
    {
        return ResponseEntity.badRequest().body(new MessageResponse("This trainer already has a team registered for this tournament."));
    }
}
