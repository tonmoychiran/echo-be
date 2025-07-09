package com.example.goppho.repositories;

import com.example.goppho.entities.ConnectionRequestEntity;
import com.example.goppho.entities.UserInformationEntity;
import com.example.goppho.enums.ConnectionRequestStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequestEntity, String> {

    Optional<ConnectionRequestEntity> findBySenderAndReceiver(UserInformationEntity sender, UserInformationEntity receiver);

    List<ConnectionRequestEntity> findAllBySenderAndStatus(UserInformationEntity sender, ConnectionRequestStatusEnum status);

    List<ConnectionRequestEntity> findAllByReceiverAndStatus(UserInformationEntity receiver,  ConnectionRequestStatusEnum status);
}
