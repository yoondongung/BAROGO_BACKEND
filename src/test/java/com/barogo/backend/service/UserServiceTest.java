package com.barogo.backend.service;

import com.barogo.backend.dao.UserDao;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    UserDao userDao;

    @InjectMocks
    UserServiceImpl userService;


}
