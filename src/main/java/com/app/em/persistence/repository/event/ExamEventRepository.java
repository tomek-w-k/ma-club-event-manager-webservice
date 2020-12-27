package com.app.em.persistence.repository.event;

import com.app.em.persistence.entity.event.ExamEvent;
import com.app.em.persistence.entity.registration.ExamRegistration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface ExamEventRepository extends EventBaseRepository<ExamEvent>
{
    Optional<ExamEvent> findExamEventByExamRegistrations(ExamRegistration examRegistration);
}
