package com.barogo.backend.controller;

import com.barogo.backend.configuration.security.JwtTokenProvider;
import com.barogo.backend.configuration.security.SecurityConfig;
import com.barogo.backend.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
        }
)
public class UserControllerTest {

    private final static String BASE_URI = "/barogo/users";
    private final static String CHECK_PW_URI = "/barogo/users/check-pw";

    @MockBean
    private UserService userService;

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
    @DisplayName("패스워드 정합성 체크 URI 체크")
    @WithMockUser
    void testCheckUserPassword() throws Exception {
        this.mvc.perform(get(CHECK_PW_URI)
                        .header("Authorization", testTokenBearer)
                        .with(csrf())
                        .param("userPw", "barogoUser!!")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("패스워드 정합성 체크 URI 파라미터가 없을 경우 체크")
    @WithMockUser
    void testCheckUserPasswordNotParam() throws Exception {
        this.mvc.perform(get(CHECK_PW_URI)
                        .header("Authorization", testTokenBearer)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }


    @Test
    @DisplayName("패스워드 정합성 체크 성공 리턴 값 체크")
    @WithMockUser
    void testCheckUserPasswordResponse() throws Exception {
        String pw = "barogoUser!!";
        MvcResult result = this.mvc.perform(get(CHECK_PW_URI)
                        .header("Authorization", testTokenBearer)
                        .with(csrf())
                        .param("userPw", pw)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertTrue(mapper.readValue(response.getContentAsString(), Boolean.class));
    }
}
