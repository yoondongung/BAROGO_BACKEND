package com.barogo.backend.util;

import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
public class PasswordValidationUtil {

    /**
     * 비밀번호 길이 체크
     * @param passWord 비밀번호
     * @param min 최소 길이
     * @return 길이 이상이면 true, 길이보다 적으면 false
     */
    public static boolean rangeLength(String passWord, int min) {
        if (passWord == null || passWord.trim().length() < min) {
            return false;
        }
        return true;
    }

    /**
     * 대문자가 하나이상 포함되는지에 대한 여부
     * @param passWord 비밀번호
     * @return 포함되어 있으면 1, 없으면 0
     */
    public static  int checkUppercase(String passWord) {
        Pattern pattern = Pattern.compile("(?=.*?[A-Z])");
        Matcher matcher = pattern.matcher(passWord);
        return matcher.find() ? 1 : 0;
    }

    /**
     * 소문자가 하나이상 포함되는지에 대한 여부
     * @param passWord 비밀번호
     * @return 포함되어 있으면 1, 없으면 0
     */
    public static  int checkLowercase(String passWord) {
        Pattern pattern = Pattern.compile("(?=.*?[a-z])");
        Matcher matcher = pattern.matcher(passWord);
        return matcher.find() ? 1 : 0;
    }

    /**
     * 숫자가 하나이상 포함되는지에 대한 여부
     * @param passWord 비밀번호
     * @return 포함되어 있으면 1, 없으면 0
     */
    public static  int checkNumber(String passWord) {
        Pattern pattern = Pattern.compile("(?=.*?[0-9])");
        Matcher matcher = pattern.matcher(passWord);
        return matcher.find() ? 1 : 0;
    }

    /**
     * 특수문자가 하나이상 포함되는지에 대한 여부
     * @param passWord 비밀번호
     * @return 포함되어 있으면 1, 없으면 0
     */
    public static  int checkSpecial(String passWord) {
        Pattern pattern = Pattern.compile("(?=.*?[#?!@$%^&*-])");
        Matcher matcher = pattern.matcher(passWord);
        return matcher.find() ? 1 : 0;
    }
}
