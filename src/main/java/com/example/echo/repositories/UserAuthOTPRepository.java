package com.example.echo.repositories;

import com.example.echo.entities.UserAuthOTPEntity;
import com.example.echo.entities.UserAuthOTPID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthOTPRepository extends JpaRepository<UserAuthOTPEntity, UserAuthOTPID> {
    Optional<UserAuthOTPEntity> findFirstByUserEmailOrderByCreatedAtDesc(String userEmail);
}
