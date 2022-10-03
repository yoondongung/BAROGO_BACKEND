package com.barogo.backend.dao;

import com.barogo.backend.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryDao extends JpaRepository<Delivery, Long> {

    List<Delivery> findDeliveryByUserIdAndStartTimeBetween(String userId, long startTime, long endTime);
}
