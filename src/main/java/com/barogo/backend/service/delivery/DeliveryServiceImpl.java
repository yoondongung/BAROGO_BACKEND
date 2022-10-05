package com.barogo.backend.service.delivery;

import com.barogo.backend.dao.delivery.DeliveryDao;
import com.barogo.backend.domain.delivery.Delivery;
import com.barogo.backend.domain.delivery.DeliveryStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryDao deliveryDao;

    private static long MAX_PERIOD = 1000 * 60 * 60 * 24 * 3L;

    @Override
    @Transactional(readOnly = true)
    public List<Delivery> getDeliveryList(String userId, long startTime, long endTime) {
        if (!invalidPeriod(startTime, endTime)) {
            throw new IllegalArgumentException("Inquiries can be made up to 3 days.");
        }
        return deliveryDao.findDeliveryByUserIdAndStartTimeBetween(userId, startTime, endTime);
    }

    @Override
    @Transactional(readOnly = true)
    public Delivery getDeliveryById(Long id) {
        return deliveryDao.findDeliveryById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find Delivery : %s", id)));
    }

    @Override
    @Transactional
    public void changeEndAddr(Long id, String endAddr) {
        Delivery delivery = getDeliveryById(id);
        if(delivery.getStatus() != DeliveryStatus.WAIT){
            throw new IllegalArgumentException("It is not possible to change the address.");
        }
        deliveryDao.updateDeliveryEndAddr(id, endAddr);
    }

    private boolean invalidPeriod(long startTime, long endTime) {
        return (endTime - startTime) <= MAX_PERIOD;
    }
}
