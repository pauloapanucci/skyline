package com.papp.skyline.controller;

import com.papp.skyline.dto.UserDTO;
import com.papp.skyline.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("api")
public class UserController{

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody UserDTO user) {
        if(Objects.isNull(user.getCpf()) || Objects.isNull(user.getName())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(Objects.isNull(userService.create(user.getName(), user.getCpf()))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
