package com.app.em.persistence.repository.event;

import com.app.em.persistence.entity.event.CampEvent;
import com.app.em.persistence.entity.registration.CampRegistration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface CampEventRepository extends EventBaseRepository<CampEvent>
{
    Optional<CampEvent> findCampEventByCampRegistrations(CampRegistration campRegistration);

    @Query("select c.numberOfPlaces from CampEvent c where c.id = :id")
    Integer findNumberOfPlacesById(@Param("id") Long id);
}
