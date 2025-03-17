package com.example.goppho.services;

import com.example.goppho.entities.UserEntity;
import com.example.goppho.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Transactional
    protected List<UserEntity> getUserListById(List<String> userIds) {
        return this.userRepository.findAllById(userIds);
    }

}
