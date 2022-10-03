package com.barogo.backend.controller;

import com.barogo.backend.dto.UserDto;
import com.barogo.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class
)
@ActiveProfiles("test")
public class UserControllerTest {

    private final static String REGISTER_USER_URI = "/barogo/users";
    private final static String CHECK_PW_URI = "/barogo/users/check-pw";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    @DisplayName("User 등록 URI 체크")
    void testRegisterUser() throws Exception {
        this.mvc.perform(post(REGISTER_USER_URI)
                        .content(mapper.writeValueAsString(registerUserDto()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User 등록 URI Body정보가 없을 경우 체크")
    void testRegisterUserNotBody() throws Exception {
        this.mvc.perform(post(REGISTER_USER_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("패스워드 정합성 체크 URI 체크")
    void testCheckUserPassword() throws Exception {
        this.mvc.perform(get(CHECK_PW_URI)
                        .param("userPw", "barogoUser!!")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("패스워드 정합성 체크 URI 파라미터가 없을 경우 체크")
    void testCheckUserPasswordNotParam() throws Exception {
        this.mvc.perform(get(CHECK_PW_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }


    @Test
    @DisplayName("패스워드 정합성 체크 성공 리턴 값 체크")
    void testCheckUserPasswordResponse() throws Exception {
        String pw = "barogoUser!!";
        MvcResult result = this.mvc.perform(get(CHECK_PW_URI)
                        .param("userPw", pw)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assertions.assertTrue(mapper.readValue(response.getContentAsString(), Boolean.class));
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
