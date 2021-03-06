package com.app.em.persistence.repository.user;

import com.app.em.persistence.entity.user.Role;
import com.app.em.persistence.entity.user.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>
{
    Optional<Role> findByRoleName(RoleEnum roleEnum);
    List<Role> findAll();
}
