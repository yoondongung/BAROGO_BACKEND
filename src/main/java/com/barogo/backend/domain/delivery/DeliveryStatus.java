package com.barogo.backend.domain.delivery;

/**
 * WAIT 배송 대기,
 * DELIVERY 배송중
 * C0MPLETE 배송완료
 * CANCEL 배송취소
 */
public enum DeliveryStatus {
    WAIT, DELIVERY, C0MPLETE, CANCEL
}
