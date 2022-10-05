package com.barogo.backend.dao.delivery;

import com.barogo.backend.domain.delivery.Delivery;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryDao extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findDeliveryById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Delivery d SET d.endAddr = :endAddr where d.id = :id")
    int updateDeliveryEndAddr(Long id, String endAddr);

    List<Delivery> findDeliveryByUserIdAndStartTimeBetween(String userId, long startTime, long endTime);
}
