package com.app.em.controller.registration;

import com.app.em.persistence.entity.event.CampEvent;
import com.app.em.persistence.entity.event.ClothingSize;
import com.app.em.persistence.entity.registration.CampRegistration;
import com.app.em.persistence.entity.user.User;
import com.app.em.persistence.repository.clothing_size.ClothingSizeRepository;
import com.app.em.persistence.repository.event.CampEventRepository;
import com.app.em.persistence.repository.event.EventRepository;
import com.app.em.persistence.repository.registration.CampRegistrationRepository;
import com.app.em.persistence.repository.user.UserRepository;
import com.app.em.security.payload.response.MessageResponse;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CampRegistrationController
{
    @Autowired
    CampRegistrationRepository campRegistrationRepository;

    @Autowired
    CampEventRepository campEventRepository;

    @Autowired
    ClothingSizeRepository clothingSizeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/camp_registrations")
    public ResponseEntity addCampRegistration(@RequestBody CampRegistration campRegistration)
    {
        Optional<CampRegistration> campRegistrationOptional = campRegistrationRepository
                .findByUserIdAndCampEventId(campRegistration.getUser().getId(), campRegistration.getCampEvent().getId());
        if ( campRegistrationOptional.isPresent() )
        {
            Optional<User> userOptional = userRepository.findById(campRegistration.getUser().getId());
            if (userOptional.isPresent())
                campRegistration.setUser(userOptional.get()); // Because of "detached entity" error

            Optional<CampEvent> campEventOptional = campEventRepository.findById(campRegistration.getCampEvent().getId());
            if ( userOptional.isPresent() && campEventOptional.isPresent() )
                return registrationAlreadyExists(userOptional.get().getFullName(), campEventOptional.get().getEventName());
        }

        CampRegistration savedCampRegistration = campRegistrationRepository.save(campRegistration);
        return ResponseEntity.ok(savedCampRegistration);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/camp_registrations/{id}")
    public ResponseEntity getCampRegistrationById(@PathVariable Long id)
    {
        Optional<CampRegistration> campRegistrationOptional = campRegistrationRepository.findById(id);
        if ( campRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( campRegistrationOptional.get() );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/camp_events/{campEventId}/camp_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCampRegistrationsForCamp(@PathVariable Long campEventId) throws JsonProcessingException
    {
        Optional<List<CampRegistration>> campRegistrationsOptional = Optional.ofNullable(
                campRegistrationRepository.findByCampEventId(campEventId) );
        if ( campRegistrationsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(campRegistrationsOptional.get()) );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/users/{userId}/camp_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCampRegistrationsForUser(@PathVariable Long userId) throws JsonProcessingException
    {
        Optional<List<CampRegistration>> campRegistrationsOptional = Optional.ofNullable(campRegistrationRepository.findByUserId(userId) );
        if ( campRegistrationsOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        List<JSONObject> jsonObjects = campRegistrationsOptional.get().stream().map(registration -> {
            JSONObject registrationJson;
            try
            {
                registrationJson = new JSONObject(objectMapper.writeValueAsString(registration));
                registrationJson.put("eventId", registration.getCampEvent().getId());
                registrationJson.put("eventName", registration.getCampEvent().getEventName());
            }
            catch (JsonProcessingException e) { throw new RuntimeException((e)); }
            catch (JSONException e) { throw new RuntimeException(e); }

            return registrationJson;
        })
        .collect(Collectors.toList());

        String registrationsString = new JSONArray(jsonObjects).toString();

        return ResponseEntity.ok( registrationsString );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/camp_events/{campEventId}/clothing_sizes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClothingSizesForCampEvent(@PathVariable Long campEventId) throws JsonProcessingException
    {
        Optional<List<ClothingSize>> clothingSizesOptional = Optional.ofNullable(clothingSizeRepository.findByCampEventId(campEventId));
        if ( clothingSizesOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok( objectMapper.writeValueAsString(clothingSizesOptional.get()) );
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/camp_registrations")
    public ResponseEntity updateCampRegistration(@RequestBody CampRegistration campRegistration)
    {
        Optional<CampRegistration> campRegistrationOptional = campRegistrationRepository.findById( campRegistration.getId() );
        if ( campRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        Optional<CampEvent> campEventOptional = campEventRepository.findCampEventByCampRegistrations(campRegistration);
        if ( campEventOptional.isEmpty() )
            return ResponseEntity.badRequest().body(new MessageResponse("There are no event specified for this registration."));

        campRegistration.setCampEvent(campEventOptional.get());
        CampRegistration updatedCampRegistration = campRegistrationRepository.save(campRegistration);

        return ResponseEntity.ok(updatedCampRegistration);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/camp_registrations/{id}")
    public ResponseEntity deleteCampRegistration(@PathVariable Long id)
    {
        Optional<CampRegistration> campRegistrationOptional = campRegistrationRepository.findById(id);
        if ( campRegistrationOptional.isEmpty() )
            return ResponseEntity.notFound().build();

        campRegistrationRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/clothing_sizes/{clothingSizeId}/camp_registrations")
    public ResponseEntity getCampRegistrationsCountForClothingSize(@PathVariable Integer clothingSizeId)
    {
        Integer clothingSizeCount = campRegistrationRepository.countCampRegistrationByClothingSizeId(clothingSizeId);
        return ResponseEntity.ok(Collections.singletonMap("clothingSizeCount", clothingSizeCount));
    }

    // - - - PRIVATE METHODS - - -

    private ResponseEntity registrationAlreadyExists(String who, String forWhat)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }
}
