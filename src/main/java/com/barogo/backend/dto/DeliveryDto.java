package com.barogo.backend.dto;

import com.barogo.backend.domain.Delivery;
import com.barogo.backend.domain.DeliveryStatus;
import com.barogo.backend.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class DeliveryDto {
    private Long id;
    private String userId;
    private Long startTime;
    private Long endTime;
    private String startAddr;
    private String endAddr;
    private DeliveryStatus status;

    private DeliveryDto(Delivery source) {
        BeanUtils.copyProperties(source, this);
    }

    public static DeliveryDto toDto(Delivery source) {
        return new DeliveryDto(source);
    }

    public Delivery toDomain() {
        Delivery delivery = new Delivery();
        BeanUtils.copyProperties(this, delivery);
        return delivery;
    }
}
