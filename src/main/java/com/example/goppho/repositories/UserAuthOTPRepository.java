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
    @Query(value = "SELECT * FROM user_auth_otp u WHERE u.user_id = :userId ORDER BY u.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<UserAuthOTPEntity> findFirstByUserOrderByCreatedAtDesc(@Param("userId") String userId);
}
