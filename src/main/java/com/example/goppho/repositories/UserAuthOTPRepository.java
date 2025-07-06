package com.example.goppho.repositories;

import com.example.goppho.entities.UserAuthOTPEntity;
import com.example.goppho.entities.UserAuthOTPID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthOTPRepository extends JpaRepository<UserAuthOTPEntity, UserAuthOTPID> {
    Optional<UserAuthOTPEntity> findFirstByUserUserEmailOrderByCreatedAtDesc(String userEmail);
}
