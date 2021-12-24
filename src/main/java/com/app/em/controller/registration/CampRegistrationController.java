package com.app.em.controller.registration;

import com.app.em.persistence.entity.registration.CampRegistration;
import com.app.em.persistence.repository.clothing_size.ClothingSizeRepository;
import com.app.em.persistence.repository.event.CampEventRepository;
import com.app.em.persistence.repository.registration.CampRegistrationRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CampRegistrationController
{
    private final CampRegistrationRepository campRegistrationRepository;
    private final CampEventRepository campEventRepository;
    private final ClothingSizeRepository clothingSizeRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final ListToResponseEntityWrapper listToResponseEntityWrapper;


    public CampRegistrationController(  CampRegistrationRepository campRegistrationRepository,
                                        CampEventRepository campEventRepository,
                                        ClothingSizeRepository clothingSizeRepository,
                                        UserRepository userRepository,
                                        ObjectMapper objectMapper,
                                        ListToResponseEntityWrapper listToResponseEntityWrapper
    ) {
        this.campRegistrationRepository = campRegistrationRepository;
        this.campEventRepository = campEventRepository;
        this.clothingSizeRepository = clothingSizeRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.listToResponseEntityWrapper = listToResponseEntityWrapper;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/camp_registrations")
    public ResponseEntity addCampRegistration(@RequestBody CampRegistration campRegistration)
    {
        return campRegistrationRepository.findByUserIdAndCampEventId(campRegistration.getUser().getId(), campRegistration.getCampEvent().getId())
                .map(existingCampRegistration -> {
                    return campEventRepository.findById(existingCampRegistration.getCampEvent().getId())
                            .map(campEvent -> registrationAlreadyExists(existingCampRegistration.getUser().getFullName(), campEvent.getEventName()) )
                            .orElseGet(() -> ResponseEntity.badRequest().build());
                }).orElseGet(() -> ResponseEntity.ok( campRegistrationRepository.save(campRegistration) ));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/camp_registrations/{id}")
    public ResponseEntity getCampRegistrationById(@PathVariable Long id)
    {
        return ResponseEntity.of( campRegistrationRepository.findById(id) );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/camp_events/{campEventId}/camp_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCampRegistrationsForCamp(@PathVariable Long campEventId)
    {
        return listToResponseEntityWrapper.wrapListInResponseEntity(campRegistrationRepository.findByCampEventId(campEventId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/users/{userId}/camp_registrations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCampRegistrationsForUser(@PathVariable Long userId)
    {
        return userRepository.findById(userId)
                .map(foundUser -> campRegistrationRepository.findByUserId(foundUser.getId()))
                .map(campRegistrations -> {
                    List<JSONObject> jsonObjects = campRegistrations.stream()
                        .map(registration -> {
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
                        }).collect(Collectors.toList());

                    return ResponseEntity.ok( new JSONArray(jsonObjects).toString() );
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/camp_events/{campEventId}/clothing_sizes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getClothingSizesForCampEvent(@PathVariable Long campEventId)
    {
        return ResponseEntity.of( Optional.ofNullable(clothingSizeRepository.findByCampEventId(campEventId)) );
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/camp_registrations")
    public ResponseEntity updateCampRegistration(@RequestBody CampRegistration campRegistration)
    {
        return campRegistrationRepository.findById(campRegistration.getId())
                .map(campRegistrationToUpdate -> {
                    return campEventRepository.findCampEventByCampRegistrations(campRegistration)
                            .map(campEvent -> {
                                campRegistration.setCampEvent(campEvent);
                                return ResponseEntity.ok(campRegistrationRepository.save(campRegistration));
                            }).orElseGet(() -> ResponseEntity.notFound().build());
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/camp_registrations/{id}")
    public ResponseEntity deleteCampRegistration(@PathVariable Long id)
    {
        return campRegistrationRepository.findById(id)
                .map(campRegistration -> {
                    campRegistrationRepository.delete(campRegistration);
                    return ResponseEntity.ok().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
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
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("User " + who + " is already registered for " + forWhat));
    }
}
