package com.app.em.persistence.repository.room_type;

import com.app.em.persistence.entity.event.RoomType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomTypeRepository extends CrudRepository<RoomType, Integer>
{
    @Query(value = "select * from room_type where tournament_event_id = ?1", nativeQuery = true)
    List<RoomType> findByTournamentEventId(Long tournamentEventId);
}
