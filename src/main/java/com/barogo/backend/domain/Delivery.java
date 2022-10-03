package com.barogo.backend.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BAROGO_DELIVERY")
@Data
public class Delivery {

    @Id @GeneratedValue
    private Long id;
    private String userId;
    private Long startTime;
    private Long endTime;
    private String startAddr;
    private String endAddr;
    private DeliveryStatus status;
}
