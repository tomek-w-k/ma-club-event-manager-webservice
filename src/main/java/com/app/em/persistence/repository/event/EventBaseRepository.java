package com.app.em.persistence.repository.event;

import com.app.em.persistence.entity.event.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


@NoRepositoryBean
public interface EventBaseRepository<T extends Event> extends CrudRepository<T, Long>
{
    List<T> findAll();
    List<T> findAllByOrderByDateCreatedDesc();
}
