package com.example.goppho.repositories;

import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserAuthOTPID;
import com.example.goppho.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthOTPRepository extends JpaRepository<UserAuthOTPEntity, UserAuthOTPID> {
    Optional<UserAuthOTPEntity> findFirstByUserUserEmailOrderByCreatedAtDesc(String userEmail);
}
