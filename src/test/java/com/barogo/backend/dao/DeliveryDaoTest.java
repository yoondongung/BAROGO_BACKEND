package com.barogo.backend.dao;

import com.barogo.backend.dao.delivery.DeliveryDao;
import com.barogo.backend.domain.delivery.Delivery;
import com.barogo.backend.domain.delivery.DeliveryStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
public class DeliveryDaoTest {

    @Autowired
    private DeliveryDao dao;

    @BeforeEach
    void setUp() {
        Delivery delivery = new Delivery();
        delivery.setUserId("barogoUser1");
        delivery.setStartTime(System.currentTimeMillis());
        delivery.setStartAddr("경기도 용인시 수지구 상현동");
        delivery.setEndAddr("서울특별시 강남구 언주로134길 32");
        delivery.setStatus(DeliveryStatus.DELIVERY);

        dao.save(delivery);
    }

    @Test
    @DisplayName("사용자 ID, 배송 시작시간 기준으로 START - END 시간 사이의 존재하는 값 체크")
    void testFindDeliverysByUserIdAndStartTimeBetween(){
        String userId = "barogoUser1";
        long startTime = System.currentTimeMillis() - 1000 * 60L;
        long endTime = System.currentTimeMillis() + 1000 * 60L;
        List<Delivery> deliveryList = dao.findDeliveryByUserIdAndStartTimeBetween(userId, startTime, endTime);
        Assertions.assertThat(deliveryList.size()).isEqualTo(1);
        for (Delivery delivery : deliveryList) {
            Assertions.assertThat(delivery.getUserId()).isEqualTo(userId);
        }
    }
}
