package com.example.goppho.services;

import com.example.goppho.entities.UserEntity;
import com.example.goppho.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Transactional
    protected Optional<UserEntity> getUserById(String userId) {
        return this.userRepository.findById(userId);
    }

    public UserEntity getUserByEmail(String email) {
        Optional<UserEntity> user = this.userRepository.findByUserEmail(email);
        if (user.isPresent()) {
            System.out.println(user.get());
        } else {
            System.out.println("User not found");
        }
        return user.orElse(null);
    }

}
