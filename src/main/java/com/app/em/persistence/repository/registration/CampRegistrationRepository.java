package com.app.em.persistence.repository.registration;

import com.app.em.persistence.entity.event.ClothingSize;
import com.app.em.persistence.entity.registration.CampRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface CampRegistrationRepository extends CrudRepository<CampRegistration, Long>
{
    Optional<CampRegistration> findById(Long id);
    List<CampRegistration> findByCampEventId(Long id);
    List<CampRegistration> findByUserId(Long id);
    Optional<CampRegistration> findByUserIdAndCampEventId(Long userId, Long campEventId);
}
