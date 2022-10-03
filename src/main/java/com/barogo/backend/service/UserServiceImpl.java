package com.barogo.backend.service;

import com.barogo.backend.dao.UserDao;
import com.barogo.backend.domain.User;
import com.barogo.backend.exception.PasswordException;
import com.barogo.backend.util.PasswordValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final int PASSWORD_MIN_LENGTH = 12;
    final int MIN_CASE = 3;

    private final UserDao userDao;

    @Override
    @Transactional
    public void registerUser(User user) {
        isValidUserId(user.getUserId());
        isValidPassword(user.getPassword());
        saveUser(user);
    }

    private void saveUser(User user){
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(String userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Can't find User : %s",
                        userId)));
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        userDao.deleteById(userId);
    }


    @Override
    public void isValidPassword(String password) {

        if (!PasswordValidationUtil.rangeLength(password, PASSWORD_MIN_LENGTH)) {
            throw new PasswordException("비밀번호는 " + PASSWORD_MIN_LENGTH +"자리 이상 입력해 주세요.");
        }

        int totalCase = PasswordValidationUtil.checkUppercase(password)
                + PasswordValidationUtil.checkLowercase(password)
                + PasswordValidationUtil.checkNumber(password)
                + PasswordValidationUtil.checkSpecial(password);
        if(totalCase < MIN_CASE){
            throw new PasswordException("비밀번호는 영어 대문자, 소문자, 숫자, 특수문자 중 3종류 이상으로 설정해야 합니다.");
        }
    }

    private void isValidUserId(String userId) {
        if(userDao.existsUserByUserId(userId)){
            throw new IllegalArgumentException("중복된 아이디가 존재합니다. 아이디 : " + userId);
        };
    }
}
