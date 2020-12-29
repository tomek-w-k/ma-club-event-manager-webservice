package com.app.em.persistence.repository.registration;

import com.app.em.persistence.entity.registration.TournamentRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TournamentRegistrationRepository extends CrudRepository<TournamentRegistration, Long>
{
    Optional<TournamentRegistration> findById(Long id);
    List<TournamentRegistration> findByTournamentEventId(Long id);
    List<TournamentRegistration> findByUserId(Long id);
    Optional<TournamentRegistration> findByUserIdAndTournamentEventId(Long userId, Long tournamentEventId);
}
