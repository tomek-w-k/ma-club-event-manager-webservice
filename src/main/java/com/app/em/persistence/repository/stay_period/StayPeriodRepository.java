package com.app.em.persistence.repository.stay_period;

import com.app.em.persistence.entity.event.StayPeriod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StayPeriodRepository extends CrudRepository<StayPeriod, Integer>
{
    @Query(value = "select * from stay_period where tournament_event_id = ?1", nativeQuery = true)
    List<StayPeriod> findByTournamentEventId(Long tournamentEventId);
}
