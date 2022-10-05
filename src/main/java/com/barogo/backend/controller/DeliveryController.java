package com.barogo.backend.controller;

import com.barogo.backend.domain.delivery.Delivery;
import com.barogo.backend.dto.DeliveryDto;
import com.barogo.backend.service.delivery.DeliveryService;
import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Delivery")
@RequestMapping("/barogo/deliverys")
@SwaggerDefinition(tags = {@Tag(name = "Delivery", description = "Barogo Delivery Service")})
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryDto> getDeliveryList(@RequestParam("userId") @NotNull String userId,
                                          @RequestParam("startTime") @NotNull Long startTime,
                                          @RequestParam("endTime") @NotNull Long endTime) {

        List<Delivery> deliveryList = deliveryService.getDeliveryList(userId, startTime, endTime);
        return deliveryList.stream().map(DeliveryDto::toDto).collect(Collectors.toList());
    }

    @PatchMapping(value = "/{deliveryId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateDelivery(@PathVariable("deliveryId") @NotNull Long deliveryId,
                                @RequestBody @NotNull DeliveryDto deliveryDto) {
        deliveryService.changeEndAddr(deliveryId, deliveryDto.getEndAddr());
    }
}
