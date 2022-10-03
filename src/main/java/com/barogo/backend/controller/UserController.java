package com.barogo.backend.controller;

import com.barogo.backend.dto.UserDto;
import com.barogo.backend.service.UserService;
import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "User")
@RequestMapping("/barogo/users")
@SwaggerDefinition(tags = {@Tag(name = "User", description = "Barogo User Service")})
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void registerUser(@RequestBody UserDto userDto) {
        userService.registerUser(userDto.toDomain());
    }

    @GetMapping(value = "/check-pw", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean checkUserPassword(@RequestParam("userPw") @NotNull String userPw) {
        userService.isValidPassword(userPw);
        return true;
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@PathVariable("userId") @NotNull String userId) {
        return UserDto.toDto(userService.findUser(userId));
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
    }

}
