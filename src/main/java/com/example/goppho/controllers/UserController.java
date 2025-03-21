package com.example.goppho.controllers;

import com.example.goppho.entities.UserEntity;
import com.example.goppho.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserEntity> searchByEmail(
            @RequestParam("email") String email
    ) {
        UserEntity user = this.userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

}
