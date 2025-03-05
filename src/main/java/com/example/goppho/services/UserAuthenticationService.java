package com.example.goppho.services;

import com.example.goppho.dtos.UserLoginRequestDTO;
import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.repositories.UserAuthOTPRepository;
import com.example.goppho.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService {
    UserAuthOTPRepository userAuthOTPRepository;
    UserRepository userRepository;

    @Autowired
    public UserAuthenticationService(
            UserAuthOTPRepository userAuthOTPRepository,
            UserRepository userRepository
    ) {
        this.userAuthOTPRepository = userAuthOTPRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void requestLogin(
            UserLoginRequestDTO userLoginRequest
    ) {

    }
}
