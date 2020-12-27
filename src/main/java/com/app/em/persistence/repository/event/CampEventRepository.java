package com.app.em.persistence.repository.event;

import com.app.em.persistence.entity.event.CampEvent;
import com.app.em.persistence.entity.registration.CampRegistration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface CampEventRepository extends EventBaseRepository<CampEvent>
{
    Optional<CampEvent> findCampEventByCampRegistrations(CampRegistration campRegistration);
}
