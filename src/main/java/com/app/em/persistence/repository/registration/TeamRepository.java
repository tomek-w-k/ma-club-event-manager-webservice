package com.app.em.persistence.repository.registration;

import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.team.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeamRepository extends CrudRepository<Team, Long>
{
    List<Team> findAll();
    List<Team> findByTrainerId(Long id);
    List<Team> findByTournamentEvent(TournamentEvent tournamentEvent);
}
