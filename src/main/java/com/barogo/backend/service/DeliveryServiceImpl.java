package com.barogo.backend.service;

import com.barogo.backend.dao.DeliveryDao;
import com.barogo.backend.domain.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryDao deliveryDao;

    @Override
    public List<Delivery> deliveryList(String userId, long startTime, long endTime) {
        return deliveryDao.findDeliveryByUserIdAndStartTimeBetween(userId, startTime, endTime);
    }
}
