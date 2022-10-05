package com.barogo.backend.controller;

import com.barogo.backend.dto.UserDto;
import com.barogo.backend.dto.UserRequest;
import com.barogo.backend.service.login.LoginService;
import com.barogo.backend.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = LoginController.class
)
public class LoginControllerTest {

    private final static String SIGN_UP_URI = "/barogo/auth/sign-up";
    private final static String SIGN_IN_URI = "/barogo/auth/sign-in";

    @MockBean
    private UserService userService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("User 등록 URI 체크")
    @WithMockUser
    void testRegisterUser() throws Exception {
        this.mvc.perform(post(SIGN_UP_URI)
                        .with(csrf())
                        .content(mapper.writeValueAsString(registerUserDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User 등록 URI Body정보가 없을 경우 체크")
    @WithMockUser
    void testRegisterUserNotBody() throws Exception {
        this.mvc.perform(post(SIGN_UP_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("User 로그인 URI 체크")
    @WithMockUser
    void testLogin() throws Exception {
        this.mvc.perform(post(SIGN_IN_URI)
                        .with(csrf())
                        .content(mapper.writeValueAsString(userRequest()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User 로그인 URI Body정보가 없을 경우 체크")
    @WithMockUser
    void testLoginNotBody() throws Exception {
        this.mvc.perform(post(SIGN_IN_URI)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    private UserRequest userRequest(){
        return new UserRequest("barogoUser1", "barogoUser1!");
    }
    private UserDto registerUserDto(){
        UserDto dto = new UserDto();
        dto.setUserId("barogoUser1");
        dto.setPassword("barogoUser1");
        dto.setUserName("바로고테스트유저");
        dto.setEmail("barogo@barogo.com");
        return dto;
    }
}
