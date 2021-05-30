package com.app.em.persistence.repository.settings;

import com.app.em.persistence.entity.settings.Setting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SettingRepository extends CrudRepository<Setting, String>
{

}
