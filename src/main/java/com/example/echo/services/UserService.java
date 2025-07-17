package com.example.echo.services;

import com.example.echo.entities.UserAuthOTPEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.UserRepository;
import com.example.echo.requests.UserRegistrationRequest;
import com.example.echo.responses.GetResponse;
import com.example.echo.responses.Response;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
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

    protected Optional<UserEntity> getUserByUsername(
            String username
    ) {
        return this.userRepository.findByUsername(username);
    }

    protected Optional<UserEntity> getUserByEmail(
            String email
    ) {
        return this.userRepository.findByEmail(email);
    }

    protected UserEntity getUserFromUserDetails(
            UserDetails userDetails
    ) {
        UserAuthOTPEntity userAuthOTPEntity = (UserAuthOTPEntity) userDetails;
        return userAuthOTPEntity.getUser();
    }

    @Transactional
    public Response registration(
            UserRegistrationRequest userRegistrationRequest
    ) throws BadRequestException {
        String email = userRegistrationRequest.getEmail();
        String username = userRegistrationRequest.getUsername();
        String name = userRegistrationRequest.getName();
        LocalDate dob = userRegistrationRequest.getDob();
        Date sqlDob = Date.valueOf(dob);

        this.checkEmail(email);
        this.checkUsername(username);

        UserEntity userEntity = new UserEntity(username, email, name, sqlDob);
        this.userRepository.save(userEntity);

        return new Response("Registration successful");
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

    private void updateUserOnlineStatus(
            UserEntity user,
            boolean online
    ) {
        if (user.isOnline() == online)
            return;
        user.setOnline(online);
        this.userRepository.save(user);
    }

    public void handleWebSocketSessionConnect(
            Principal principal
    ) {
        if (principal == null)
            return;

        Optional<UserEntity> userEntity = this.getUserById(principal.getName());
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            user.setOnline(true);
        }
    }

    public void handleWebSocketSessionDisconnect(
            Principal principal
    ) {
        if (principal == null)
            return;

        Optional<UserEntity> userEntity = this.getUserById(principal.getName());
        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            user.setOnline(false);
        }
    }

}
