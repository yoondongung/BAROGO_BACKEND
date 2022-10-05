package com.barogo.backend.service;

import com.barogo.backend.dao.delivery.DeliveryDao;
import com.barogo.backend.domain.delivery.Delivery;
import com.barogo.backend.domain.delivery.DeliveryStatus;
import com.barogo.backend.service.delivery.DeliveryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class DeliveryServiceTest {

    @Mock
    DeliveryDao deliveryDao;

    @InjectMocks
    DeliveryServiceImpl deliveryService;

    @Test
    @DisplayName("배송 조회")
    void testDeliveryList() {
        String userId = "barogoUser1";
        Long endTime = System.currentTimeMillis();
        Long startTime = endTime - (1000 * 60 * 60 * 2L);

        when(deliveryDao.findDeliveryByUserIdAndStartTimeBetween(userId, startTime, endTime))
                .thenReturn(getDeliveryList());
        List<Delivery> deliveryList = deliveryService.getDeliveryList(userId, startTime, endTime);
        Assertions.assertThat(deliveryList.size()).isEqualTo(2);
    }

    List<Delivery> getDeliveryList() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setUserId("barogoUser1");
        delivery.setStartTime(System.currentTimeMillis());
        delivery.setStartAddr("경기도 용인시 수지구 상현동");
        delivery.setEndAddr("서울특별시 강남구 언주로134길 32");
        delivery.setStatus(DeliveryStatus.C0MPLETE);

        Delivery delivery2 = new Delivery();
        delivery2.setId(2L);
        delivery2.setUserId("barogoUser1");
        delivery2.setStartTime(System.currentTimeMillis());
        delivery2.setStartAddr("경기도 용인시 수지구 상현동");
        delivery2.setEndAddr("서울특별시 강남구 언주로134길 32");
        delivery2.setStatus(DeliveryStatus.DELIVERY);

        return Arrays.asList(delivery, delivery2);
    }
}
