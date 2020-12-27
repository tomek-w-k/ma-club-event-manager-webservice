package com.app.em.persistence.repository.registration;

import com.app.em.persistence.entity.registration.ExamRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ExamRegistrationRepository extends CrudRepository<ExamRegistration, Long>
{
    Optional<ExamRegistration> findById(Long id);
    List<ExamRegistration> findByExamEventId(Long id);
    List<ExamRegistration> findByUserId(Long id);
    Optional<ExamRegistration> findByUserIdAndExamEventId(Long userId, Long examEventId);
}
