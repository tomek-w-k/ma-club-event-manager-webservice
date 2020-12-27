package com.app.em.persistence.repository.event;

import com.app.em.persistence.entity.event.Event;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Transactional
public interface EventRepository extends EventBaseRepository<Event>
{

}
