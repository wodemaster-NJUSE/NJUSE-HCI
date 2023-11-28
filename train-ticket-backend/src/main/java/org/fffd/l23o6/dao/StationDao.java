package org.fffd.l23o6.dao;

import org.fffd.l23o6.pojo.entity.StationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationDao extends JpaRepository<StationEntity, Long>{
    StationEntity findByName(String name);
}
