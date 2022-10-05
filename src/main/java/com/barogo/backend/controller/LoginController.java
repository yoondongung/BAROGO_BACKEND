package com.barogo.backend.controller;

import com.barogo.backend.dto.JwtInfo;
import com.barogo.backend.dto.UserDto;
import com.barogo.backend.dto.UserRequest;
import com.barogo.backend.service.login.LoginService;
import com.barogo.backend.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Auth")
@RequestMapping("/barogo/auth")
@SwaggerDefinition(tags = {@Tag(name = "Auth", description = "Barogo Auth Service")})
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private final UserService userService;

    @PostMapping("/sign-in")
    public JwtInfo.Response login(@RequestBody UserRequest request) {
        return loginService.login(request);
    }

    @PostMapping(value = "/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
    public void registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto.toDomain());
    }
}
