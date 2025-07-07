package com.example.goppho.repositories;


import com.example.goppho.entities.UserEntity;
import com.example.goppho.entities.UserInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInformationRepository extends JpaRepository<UserInformationEntity, String> {
    Optional<UserInformationEntity> findByUser(UserEntity user);
    Optional<UserInformationEntity> findByName(String name);
}
