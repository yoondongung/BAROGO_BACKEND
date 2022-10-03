package com.barogo.backend.service;

import com.barogo.backend.domain.Delivery;

import java.util.List;

public interface DeliveryService {

    /**
     * 유저에 해당하는 배송 목록 조회
     * @param userId 유저 아이디
     * @param startTime 시작시간
     * @param endTime 종료시간
     * @return 배송 목록
     */
    List<Delivery> deliveryList(String userId, long startTime, long endTime);
}
