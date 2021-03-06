package com.app.em.persistence.repository.event;

import com.app.em.persistence.entity.event.TournamentEvent;
import com.app.em.persistence.entity.registration.TournamentRegistration;
import com.app.em.persistence.entity.team.Team;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Transactional
public interface TournamentEventRepository extends EventBaseRepository<TournamentEvent>
{
    //Optional<TournamentEvent> findTournamentEventByTournamentRegistrations(TournamentRegistration tournamentRegistration);
    Optional<TournamentEvent> findTournamentEventByTeams(Team team);
}
