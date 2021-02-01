package com.app.em.controller.registration;

import com.app.em.persistence.entity.event.Event;
import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.team.Team;
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


    // CREATE ::    addTeam()
    @PostMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTeam(@RequestBody Team team, @PathVariable Long tournamentEventId)
    {
        Optional<TournamentEvent> eventOptional = tournamentEventRepository.findById(tournamentEventId);
        if ( eventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("Event specified for a given team doesn't exist."));

        team.setTournamentEvent( eventOptional.get() );
        Team savedTeam = teamRepository.save(team);

        return ResponseEntity.ok(savedTeam);
    }

    // READ ::      getTeam(), getAllTeams()
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

    @GetMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTeamsForEvent() throws JsonProcessingException
    {
        Optional<List<Team>> teamsOptional = Optional.ofNullable( teamRepository.findAll() );
        if ( teamsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(teamsOptional.get()) );
    }

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

    // UPDATE ::    updateTeam()
    @PutMapping(value = "/tournament_events/{tournamentEventId}/teams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTeam(@RequestBody Team team)
    {
        Optional<Team> teamOptional = teamRepository.findById(team.getId());
        if ( teamOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Team updatedTeam = teamRepository.save(team);

        return ResponseEntity.ok(updatedTeam);
    }

    // DELETE ::    deleteTeam()
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
