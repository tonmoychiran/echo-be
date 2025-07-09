package com.example.goppho.services;

import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.entities.UserInformationEntity;
import com.example.goppho.repositories.UserInformationRepository;
import com.example.goppho.repositories.UserRepository;
import com.example.goppho.requests.UserDOBUpdateRequest;
import com.example.goppho.requests.UserNameUpdateRequest;
import com.example.goppho.responses.GetResponse;
import com.example.goppho.responses.Response;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    UserRepository userRepository;
    UserInformationRepository userInformationRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            UserInformationRepository userInformationRepository
    ) {
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
    }

    @Transactional
    protected List<UserEntity> getUserListById(List<String> userIds) {
        return this.userRepository.findAllById(userIds);
    }

    @Transactional
    protected Optional<UserEntity> getUserById(String userId) {
        return this.userRepository.findById(userId);
    }

    public GetResponse<UserInformationEntity> getUserInformationByEmail(UserDetails userDetails) {
        UserAuthOTPEntity userAuthOTPEntity = (UserAuthOTPEntity) userDetails;
        UserEntity user = userAuthOTPEntity.getUser();

        Optional<UserInformationEntity> userInformation = this.userInformationRepository.findByUser(user);
        UserInformationEntity userInformationEntity = userInformation.orElse(null);

        return new GetResponse<>("User Information", userInformationEntity);
    }

    public UserInformationEntity getUserByName(String name) {
        Optional<UserInformationEntity> user = this.userInformationRepository.findByName(name);
        return user.orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Response updateUserName(
            UserDetails userDetails,
            UserNameUpdateRequest userNameUpdateRequest
    ) {
        String name = userNameUpdateRequest.getName();

        UserAuthOTPEntity userAuthOTPEntity = (UserAuthOTPEntity) userDetails;
        UserEntity user = userAuthOTPEntity.getUser();
        Optional<UserInformationEntity> userInformation = this.userInformationRepository.findByUser(user);

        if (userInformation.isPresent()) {
            UserInformationEntity updatableUserInformationEntity = userInformation.get();
            updatableUserInformationEntity.setName(name);
            this.userInformationRepository.save(updatableUserInformationEntity);
        } else {
            this.userInformationRepository.save(new UserInformationEntity(name, user));
        }

        return new Response("Updated");
    }

    public Response updateUserDOB(
            UserDetails userDetails,
            UserDOBUpdateRequest userDOBUpdateRequest
    ) {
        LocalDate localDate = userDOBUpdateRequest.getDob();
        Date dob = Date.valueOf(localDate);

        UserAuthOTPEntity userAuthOTPEntity = (UserAuthOTPEntity) userDetails;
        UserEntity user = userAuthOTPEntity.getUser();
        Optional<UserInformationEntity> userInformation = this.userInformationRepository.findByUser(user);

        if (userInformation.isEmpty()) {
            throw new EntityNotFoundException("Update failed");
        }

        UserInformationEntity updatableUserInformationEntity = userInformation.get();
        updatableUserInformationEntity.setDob(dob);
        this.userInformationRepository.save(updatableUserInformationEntity);

        return new Response("Updated");
    }
}
