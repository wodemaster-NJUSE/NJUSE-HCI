package org.fffd.l23o6.dao;

import org.fffd.l23o6.pojo.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainDao extends JpaRepository<TrainEntity, Long>{
    TrainEntity findByName(String name);
    // TODO: 2023/5/26
    
}
