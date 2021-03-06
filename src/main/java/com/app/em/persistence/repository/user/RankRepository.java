package com.app.em.persistence.repository.user;

import com.app.em.persistence.entity.user.Rank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RankRepository extends CrudRepository<Rank, Integer>
{
    Optional<Rank> findByRankName(String rankName);
    List<Rank> findAll();
}
