package com.barogo.backend.controller;

import com.barogo.backend.configuration.security.JwtTokenProvider;
import com.barogo.backend.configuration.security.SecurityConfig;
import com.barogo.backend.domain.delivery.Delivery;
import com.barogo.backend.domain.delivery.DeliveryStatus;
import com.barogo.backend.dto.DeliveryDto;
import com.barogo.backend.service.delivery.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = DeliveryController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
public class DeliveryControllerTest {

    private final static String BASE_URI = "/barogo/deliverys";

    @MockBean
    private DeliveryService deliveryService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @SpyBean
    JwtTokenProvider tokenProvider;
    private String testTokenBearer;
    private String testToken;

    @BeforeEach
    void setUp() {
        testToken = tokenProvider.generateToken("test");
        testTokenBearer = "Bearer " + testToken;
    }

    @Test
    @DisplayName("배송 조회 URI 체크")
    @WithMockUser
    void testGetDeliveryList() throws Exception {

        String userId = "barogoUser1";
        Long endTime = System.currentTimeMillis();
        Long startTime = endTime - ( 1000 * 60 * 60 *2L);

        this.mvc.perform(get(BASE_URI)
                        .param("userId",userId)
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString())
                        .header("Authorization", testTokenBearer)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("배송 조회 URI 파라미터가 없을 경우 체크")
    @WithMockUser
    void testGetDeliveryListNotBody() throws Exception {

        String userId = "barogoUser1";

        this.mvc.perform(get(BASE_URI)
                        .param("userId",userId)
                        .header("Authorization", testTokenBearer)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("배송지 정보 업데이트 URI")
    @WithMockUser
    void testUpdateDelivery() throws Exception {
        Long deliveryId = 1L;
        this.mvc.perform(patch(BASE_URI + "/" + deliveryId)
                        .content(mapper.writeValueAsString(DeliveryDto.toDto(getDelivery())))
                        .header("Authorization", testTokenBearer)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    Delivery getDelivery(){
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setUserId("barogoUser1");
        delivery.setStartTime(System.currentTimeMillis());
        delivery.setStartAddr("경기도 용인시 수지구 상현동");
        delivery.setEndAddr("서울특별시 강남구 언주로134길 32");
        delivery.setStatus(DeliveryStatus.C0MPLETE);
        return delivery;
    }
}
