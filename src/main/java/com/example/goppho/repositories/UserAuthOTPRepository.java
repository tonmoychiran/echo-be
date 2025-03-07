package com.example.goppho.repositories;

import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthOTPRepository extends JpaRepository<UserAuthOTPEntity, String> {
    Optional<UserAuthOTPEntity> deleteAllByUserIs(UserEntity user);
}
