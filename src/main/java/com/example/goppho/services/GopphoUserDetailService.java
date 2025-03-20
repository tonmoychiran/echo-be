package com.example.goppho.services;

import com.example.goppho.entities.UserEntity;
import com.example.goppho.entities.UserPrincipalEntity;
import com.example.goppho.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GopphoUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public GopphoUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipalEntity(user.get());
    }
}