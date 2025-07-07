package com.example.goppho.controllers;

import com.example.goppho.entities.UserEntity;
import com.example.goppho.entities.UserInformationEntity;
import com.example.goppho.requests.UserDOBUpdateRequest;
import com.example.goppho.requests.UserNameUpdateRequest;
import com.example.goppho.responses.Response;
import com.example.goppho.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<UserEntity> searchByEmail(
            @RequestParam("email") String email
    ) {
        UserEntity user = this.userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserInformationEntity> getUserProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UserInformationEntity user = this.userService.getUserInformationByEmail(userDetails);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/name")
    public ResponseEntity<Response> updateUserUserName(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserNameUpdateRequest userNameUpdateRequest
    ) {
        Response response = this.userService.updateUserName(userDetails, userNameUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/dob")
    public ResponseEntity<Response> updateUserUserDOB(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserDOBUpdateRequest userDOBUpdateRequest
    ) {
        Response response = this.userService.updateUserDOB(userDetails, userDOBUpdateRequest);
        return ResponseEntity.ok(response);
    }


}
