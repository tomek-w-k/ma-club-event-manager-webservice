package com.app.em.persistence.repository.clothing_size;

import com.app.em.persistence.entity.event.ClothingSize;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ClothingSizeRepository extends CrudRepository<ClothingSize, Integer>
{
    @Query(value = "select * from clothing_size where camp_event_id = ?1", nativeQuery = true)
    List<ClothingSize> findByCampEventId(Long campEventId);
}
