package com.app.em.persistence.repository;

import com.app.em.persistence.entity.user.Club;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClubRepository extends CrudRepository<Club, Integer>
{
    Optional<Club> findByClubName(String clubName);
}
