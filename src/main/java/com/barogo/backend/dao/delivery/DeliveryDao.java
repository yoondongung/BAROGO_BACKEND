package com.barogo.backend.dao.delivery;

import com.barogo.backend.domain.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryDao extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findDeliveryById(Long id);
    List<Delivery> findDeliveryByUserIdAndStartTimeBetween(String userId, long startTime, long endTime);
}
