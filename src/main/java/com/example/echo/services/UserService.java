package com.example.echo.services;

import com.example.echo.entities.UserAuthOTPEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.UserRepository;
import com.example.echo.responses.GetResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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

    protected List<UserEntity> getUserListById(
            List<String> userIds
    ) {
        return this.userRepository.findAllById(userIds);
    }

    protected Optional<UserEntity> getUserById(
            String userId
    ) {
        return this.userRepository.findById(userId);
    }

    protected Optional<UserEntity> getUserByEmail(
            String email
    ){
        return this.userRepository.findByEmail(email);
    }

    protected UserEntity getUserFromUserDetails(
            UserDetails userDetails
    ) {
        UserAuthOTPEntity userAuthOTPEntity = (UserAuthOTPEntity) userDetails;
        return userAuthOTPEntity.getUser();
    }

    public GetResponse<UserEntity> getUserProfile(
            UserDetails userDetails
    ) {
        UserEntity user = getUserFromUserDetails(userDetails);
        return new GetResponse<>("User Information", user);
    }

    public void checkEmail(
            String email
    ) throws BadRequestException {
        if (this.userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email exists");
        }
    }

    public void checkUsername(
            String username
    ) throws BadRequestException {
        if (this.userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username exists");
        }
    }
}
