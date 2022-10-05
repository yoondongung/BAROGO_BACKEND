package com.barogo.backend.service.delivery;

import com.barogo.backend.dao.delivery.DeliveryDao;
import com.barogo.backend.domain.delivery.Delivery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryDao deliveryDao;

    private static long MAX_PERIOD = 1000 * 60 * 60 * 24 * 3L;

    @Override
    public List<Delivery> getDeliveryList(String userId, long startTime, long endTime) {
        if (!invalidPeriod(startTime, endTime)) {
            throw new IllegalArgumentException("Inquiries can be made up to 3 days.");
        }
        return deliveryDao.findDeliveryByUserIdAndStartTimeBetween(userId, startTime, endTime);
    }

    @Override
    public Delivery getDeliveryById(Long id) {
        return deliveryDao.findDeliveryById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find Delivery : %s", id)));
    }

    private boolean invalidPeriod(long startTime, long endTime) {
        return (endTime - startTime) <= MAX_PERIOD;
    }
}
