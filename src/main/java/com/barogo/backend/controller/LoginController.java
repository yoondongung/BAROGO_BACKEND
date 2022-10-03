package com.barogo.backend.controller;

import com.barogo.backend.dto.UserDto;
import com.barogo.backend.service.UserService;
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

    private final UserService userService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public void login(@RequestBody UserDto userDto) {

    }
}
