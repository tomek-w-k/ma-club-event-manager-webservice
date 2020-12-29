package com.app.em.persistence.repository.weight_age_category;

import com.app.em.persistence.entity.event.WeightAgeCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WeightAgeCategoryRepository extends CrudRepository<WeightAgeCategory, Integer>
{
    @Query(value = "select * from weight_age_category where tournament_event_id = ?1", nativeQuery = true)
    List<WeightAgeCategory> findByTournamentEventId(Long tournamentEventId);
}
