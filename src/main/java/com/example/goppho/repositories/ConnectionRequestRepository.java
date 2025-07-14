package com.example.goppho.repositories;

import com.example.goppho.entities.ConnectionRequestEntity;
import com.example.goppho.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequestEntity, String> {

    Optional<ConnectionRequestEntity> findBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    List<ConnectionRequestEntity> findAllBySender(UserEntity sender);

    List<ConnectionRequestEntity> findAllByReceiver(UserEntity receiver);
}
