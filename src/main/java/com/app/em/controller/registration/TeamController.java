package com.app.em.controller.registration;

import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.team.Team;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.event.EventRepository;
import com.app.em.persistence.repository.event.TournamentEventRepository;
import com.app.em.persistence.repository.registration.TeamRepository;
import com.app.em.security.payload.response.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeamController
{
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TournamentEventRepository tournamentEventRepository;

    @Autowired
    ObjectMapper objectMapper;


    @PreAuthorize("hasRole('TRAINER')")
    @PostMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTeam(@RequestBody Team team, @PathVariable Long tournamentEventId)
    {
        Optional<TournamentEvent> eventOptional = tournamentEventRepository.findById(tournamentEventId);
        if ( eventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("Event specified for a given team doesn't exist."));

        // Check if a given trainer already has a team for this tournament
        final User trainer = team.getTrainer();
        Boolean trainerAlreadyHasTeam = eventOptional.get().getTeams().stream().anyMatch(existingTeam -> {
            return existingTeam.getTrainer().getId() == trainer.getId();
        });

        if ( trainerAlreadyHasTeam )
            return ResponseEntity.badRequest().body(new MessageResponse("This trainer already has a team registered for this tournament."));

        team.setTournamentEvent( eventOptional.get() );
        Team savedTeam = teamRepository.save(team);

        return ResponseEntity.ok(savedTeam);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/teams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTeam(@PathVariable Long id)
    {
        Optional<Team> teamOptional = teamRepository.findById(id);
        if ( teamOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Team team = teamOptional.get();
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
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTeamsForEvent(@PathVariable Long tournamentEventId) throws JsonProcessingException
    {
        Optional<TournamentEvent> tournamentEventOptional = tournamentEventRepository.findById(tournamentEventId);
        if ( tournamentEventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("Error - A given tournament doesn't exist."));

        Optional<List<Team>> teamsOptional = Optional.ofNullable( teamRepository.findByTournamentEvent(tournamentEventOptional.get()) );
        if ( teamsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(teamsOptional.get()) );
    }

    @PreAuthorize("hasRole('TRAINER')")
    @GetMapping(value = "/user/{userId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTeamsForUser(@PathVariable Long userId) throws JsonProcessingException
    {
        Optional<List<Team>> teamsOptional = Optional.ofNullable( teamRepository.findByTrainerId(userId) );
        if ( teamsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        List<JSONObject> jsonObjects = teamsOptional.get().stream().map(team -> {
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
        })
        .collect(Collectors.toList());

        String teamsString =  new JSONArray(jsonObjects).toString();

        return ResponseEntity.ok( teamsString );
    }

    @PreAuthorize("hasRole('TRAINER')")
    @PutMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTeam(@RequestBody Team team)
    {
        Optional<Team> teamOptional = teamRepository.findById(team.getId());
        if ( teamOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Team updatedTeam = teamRepository.save(team);

        return ResponseEntity.ok(updatedTeam);
    }

    @PreAuthorize("hasRole('TRAINER')")
    @DeleteMapping("/user/{userId}/teams/{id}")
    public ResponseEntity deleteTeam(@PathVariable Long id)
    {
        Optional<Team> teamOptional = teamRepository.findById(id);
        if ( teamOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        teamRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
