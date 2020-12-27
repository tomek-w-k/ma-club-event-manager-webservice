package com.app.em.persistence.repository.user;

import com.app.em.persistence.entity.user.BranchChief;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BranchChiefRepository extends CrudRepository<BranchChief, Integer>
{
    Optional<BranchChief> findByBranchChiefName(String branchChiefName);
}
