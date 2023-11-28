package org.fffd.l23o6.dao;

import java.util.List;

import org.fffd.l23o6.pojo.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<OrderEntity, Long>{
    List<OrderEntity> findByUserId(Long userId);
}
