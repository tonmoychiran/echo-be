package com.example.echo.repositories;

import com.example.echo.entities.ConnectionEntity;
import com.example.echo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, String> {
    Optional<ConnectionEntity> findByUserAndFriend(UserEntity user, UserEntity friend);
    List<ConnectionEntity> findAllByUser(UserEntity user);
}
