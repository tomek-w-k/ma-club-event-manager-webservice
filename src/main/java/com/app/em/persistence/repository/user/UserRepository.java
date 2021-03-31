package com.app.em.persistence.repository.user;

import com.app.em.persistence.entity.user.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Long>
{
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByFullName(String fullName);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByCountry(String country);
    List<User> findByRank(Rank rank);
    List<User> findByClub(Club club);
    List<User> findByBranchChief(BranchChief branchChief);
    List<User> findByRoles(Role role);
    List<User> findByRolesNotContaining(Role role);
}
