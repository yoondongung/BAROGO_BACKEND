package com.barogo.backend.dto;

import com.barogo.backend.domain.User;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class UserDto {

    @NotNull
    private String userId;
    @NotNull
    private String password;

    private String userName;
    private String email;

    private UserDto(User source) {
        BeanUtils.copyProperties(source, this);
    }

    public static UserDto toDto(User source) {
        return new UserDto(source);
    }

    public User toDomain() {
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }
}
