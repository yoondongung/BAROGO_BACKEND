package com.barogo.backend.dao;

import com.barogo.backend.dao.user.UserDao;
import com.barogo.backend.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId("barogoUser1");
        user.setPassword("barogoUser1");
        user.setEmail("barogoUser1@barogo.com");
        user.setUserName("바로고");
        dao.save(user);
    }

    @Test
    @DisplayName("유저 이름이 존재하는지 체크")
    void existsUserByUserIdTest() {
        String userId = "barogoUser1";
        boolean exist = dao.existsUserByUserId(userId);
        Assertions.assertThat(exist).isTrue();
    }
}
