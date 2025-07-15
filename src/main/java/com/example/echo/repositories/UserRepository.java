package com.example.echo.repositories;

import com.example.echo.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
