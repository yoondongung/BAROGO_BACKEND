package com.barogo.backend.service.delivery;

import com.barogo.backend.domain.delivery.Delivery;

import java.util.List;

public interface DeliveryService {

    /**
     * 유저에 해당하는 배송 목록 조회
     * @param userId 유저 아이디
     * @param startTime 시작시간
     * @param endTime 종료시간
     * @return 배송 목록
     */
    List<Delivery> getDeliveryList(String userId, long startTime, long endTime);

    /**
     * 배송 아이디로 배송 조회
     * @param id 배송 아이디
     * @return 배송 정보
     */
    Delivery getDeliveryById(Long id);

    /**
     * 도착 주소 업데이트
     * @param id 배송id
     * @param endAddr 도착 주소
     */
     void changeEndAddr(Long id, String endAddr);
}
