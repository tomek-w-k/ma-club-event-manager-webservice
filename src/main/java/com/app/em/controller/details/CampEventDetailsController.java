package com.app.em.controller.details;

import com.app.em.details.CampEventDetails;
import com.app.em.persistence.repository.event.CampEventRepository;
import com.app.em.persistence.repository.registration.CampRegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CampEventDetailsController
{
    private final CampEventRepository campEventRepository;
    private final CampRegistrationRepository campRegistrationRepository;


    public CampEventDetailsController(CampEventRepository campEventRepository, CampRegistrationRepository campRegistrationRepository)
    {
        this.campEventRepository = campEventRepository;
        this.campRegistrationRepository = campRegistrationRepository;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/camp_events/{campEventId}/details")
    public ResponseEntity<CampEventDetails> getCampEventDetails(@PathVariable Long campEventId)
    {
        return ResponseEntity.ok(new CampEventDetails(
                campEventId,
                campRegistrationRepository.countCampRegistrationByCampEventId(campEventId),
                campEventRepository.findNumberOfPlacesById(campEventId)
        ));
    }
}
