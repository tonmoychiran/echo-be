package com.example.goppho.repositories;

import com.example.goppho.entities.ConnectionEntity;
import com.example.goppho.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, String> {
    Optional<ConnectionEntity> findByUserAndFriend(UserEntity user, UserEntity friend);
}
