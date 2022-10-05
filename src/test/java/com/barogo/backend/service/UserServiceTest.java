package com.barogo.backend.service;

import com.barogo.backend.dao.user.UserDao;
import com.barogo.backend.domain.user.User;
import com.barogo.backend.service.user.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceTest {

    @Mock
    UserDao userDao;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("유저 아이디로 유저 찾기")
    void testFindUserByUserId() {
        String userId = "barogoUser1";
        when(userDao.findById(userId)).thenReturn(Optional.of(getUser(userId)));
        User user = userService.findUser(userId);
        Assertions.assertThat(user.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("패스워드가 유효한 경우")
    void testValidPassword() {
        String pw = "barogoUser1!";
        userService.isValidPassword(pw);
    }

    @Test
    @DisplayName("패스워드가 유효하지 않은 경우")
    void testIsNotValidPassword() {
        String pw = "barogoUser1";
        try {
            userService.isValidPassword(pw);
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("비밀번호는 12자리 이상 입력해 주세요.");
        }
    }

    @Test
    @DisplayName("패스워드가 유효하지 않은 경우")
    void testIsNotValidPassword2() {
        String pw = "barogoUserTest";
        try {
            userService.isValidPassword(pw);
        } catch (Exception e) {
            Assertions.assertThat(e.getMessage()).isEqualTo("비밀번호는 영어 대문자, 소문자, 숫자, 특수문자 중 3종류 이상으로 설정해야 합니다.");
        }
    }

    User getUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword("barogo_default_PW!!");
        user.setUserName("바로고 테스트 계정");
        user.setEmail("barogo_Test@barogo.com");

        return user;
    }
}
