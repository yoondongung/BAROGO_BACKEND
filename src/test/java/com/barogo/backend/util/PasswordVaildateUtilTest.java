package com.barogo.backend.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordVaildateUtilTest {

    @Test
    @DisplayName("비밀번호 글자수 체크 결과 테스트")
    void rangeLengthTest(){
        final int MIN = 12;
        String password = "barogoTestUs"; // 12자리
        boolean checkRangeLength = PasswordValidationUtil.rangeLength(password, MIN);
        Assertions.assertThat(checkRangeLength).isTrue();
    }

    @Test
    @DisplayName("비밀번호 글자수 체크 실패 결과 테스트")
    void rangeLengthFailTest(){
        final int MIN = 12;
        String password = "barogoTest"; //10자리
        boolean checkRangeLength = PasswordValidationUtil.rangeLength(password, MIN);
        Assertions.assertThat(checkRangeLength).isFalse();
    }

    @Test
    @DisplayName("비밀번호 대문자 포함 테스트")
    void checkUppercaseTest(){
        String password = "barogoTTest";
        int checkUppercase = PasswordValidationUtil.checkUppercase(password);
        Assertions.assertThat(checkUppercase).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호 대문자 포함 하지 않는 경우 테스트")
    void checkUppercaseFailTest(){
        String password = "barogotest";
        int checkUppercase = PasswordValidationUtil.checkUppercase(password);
        Assertions.assertThat(checkUppercase).isEqualTo(0);
    }

    @Test
    @DisplayName("비밀번호 소문자 포함 테스트")
    void checkLowercaseTest(){
        String password = "bAROGO";
        int checkLowercase = PasswordValidationUtil.checkLowercase(password);
        Assertions.assertThat(checkLowercase).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호 소문자 포함 하지 않는 경우 테스트")
    void checkLowercaseFailTest(){
        String password = "BAROGO";
        int checkLowercase = PasswordValidationUtil.checkLowercase(password);
        Assertions.assertThat(checkLowercase).isEqualTo(0);
    }

    @Test
    @DisplayName("비밀번호 숫자 포함 테스트")
    void checkNumberTest(){
        String password = "bAROGO0613";
        int checkNumber = PasswordValidationUtil.checkNumber(password);
        Assertions.assertThat(checkNumber).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호 소문자 포함 하지 않는 경우 테스트")
    void checkNumberFailTest(){
        String password = "BAROGO";
        int checkNumber = PasswordValidationUtil.checkNumber(password);
        Assertions.assertThat(checkNumber).isEqualTo(0);
    }

    @Test
    @DisplayName("비밀번호 특수문자 포함 테스트")
    void checkSpecialTest(){
        String password = "bAROGO0613!";
        int checkSpecial = PasswordValidationUtil.checkSpecial(password);
        Assertions.assertThat(checkSpecial).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호 특수문자 포함 하지 않는 경우 테스트")
    void checkSpecialFailTest(){
        String password = "BAROGO";
        int checkSpecial = PasswordValidationUtil.checkSpecial(password);
        Assertions.assertThat(checkSpecial).isEqualTo(0);
    }
}
