package com.test.controllers;

import com.framework.annotation.Controller;
import com.framework.annotations.API;
import com.framework.annotations.Get;
import com.framework.annotations.Json;
import java.util.*;

@Controller
public class UserApiController {
    @API
    @Get("/api/users")
    @Json
    public List<UserDto> listUsers() {
        List<UserDto> users = new ArrayList<>();
        users.add(new UserDto(1, "Alice"));
        users.add(new UserDto(2, "Bob"));
        return users;
    }

    @API
    @Get("/api/user/{id}")
    @Json
    public UserDto getUserById(int id) {
        if (id == 1) return new UserDto(1, "Alice");
        if (id == 2) return new UserDto(2, "Bob");
        return null;
    }

    public static class UserDto {
        public int id;
        public String name;
        public UserDto(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
