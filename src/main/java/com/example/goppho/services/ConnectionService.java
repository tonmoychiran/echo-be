package com.example.goppho.services;

import com.example.goppho.entities.ConnectionEntity;
import com.example.goppho.entities.ConnectionRequestEntity;
import com.example.goppho.entities.UserEntity;
import com.example.goppho.enums.ConnectionRequestStatusEnum;
import com.example.goppho.repositories.ConnectionRepository;
import com.example.goppho.repositories.ConnectionRequestRepository;
import com.example.goppho.requests.ConnectionActionRequest;
import com.example.goppho.requests.ConnectionRequest;
import com.example.goppho.responses.GetResponse;
import com.example.goppho.responses.Response;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {
    private final ConnectionRequestRepository connectionRequestRepository;
    private final ConnectionRepository connectionRepository;
    private final UserService userService;

    @Autowired
    public ConnectionService(
            ConnectionRequestRepository connectionRequestRepository,
            ConnectionRepository connectionRepository,
            UserService userService
    ) {
        this.connectionRequestRepository = connectionRequestRepository;
        this.connectionRepository = connectionRepository;
        this.userService = userService;
    }

    public GetResponse<List<ConnectionRequestEntity>> getReceivedConnectionRequests(
            UserDetails userDetails
    ) {
        UserEntity user = userService.getUserFromUserDetails(userDetails);
        List<ConnectionRequestEntity> connectionRequests = this
                .connectionRequestRepository
                .findAllByReceiver(user);
        return new GetResponse<>("Connection Requests", connectionRequests);
    }

    public GetResponse<List<ConnectionRequestEntity>> getSentConnectionRequests(
            UserDetails userDetails
    ) {
        UserEntity user = userService.getUserFromUserDetails(userDetails);
        List<ConnectionRequestEntity> connectionRequests = this
                .connectionRequestRepository
                .findAllBySender(user);
        return new GetResponse<>("Connection Requests", connectionRequests);
    }

    public Response sendConnectionRequest(
            ConnectionRequest connectionRequest,
            UserDetails userDetails
    ) throws BadRequestException {

        UserEntity senderUserEntity = userService.getUserFromUserDetails(userDetails);

        Optional<UserEntity> receiverUser = this.userService.getUserById(connectionRequest.getUserId());
        if (receiverUser.isEmpty()) {
            throw new EntityNotFoundException("Receiver not found");
        }

        UserEntity receiverUserEntity = receiverUser.get();

        Optional<ConnectionRequestEntity> reverseRequest = this.connectionRequestRepository.findBySenderAndReceiver(receiverUserEntity, senderUserEntity);
        if (reverseRequest.isPresent()) {
            handleReverseConnectionRequest(reverseRequest.get());
        }

        Optional<ConnectionRequestEntity> identicalRequest = this.connectionRequestRepository.findBySenderAndReceiver(senderUserEntity, receiverUserEntity);
        if (identicalRequest.isPresent()) {
            ConnectionRequestEntity connectionRequestEntity = identicalRequest.get();
            this.handleIdenticalConnectionRequest(connectionRequestEntity);

            this.connectionRequestRepository.save(connectionRequestEntity);
            return new Response("Request sent");
        }

        ConnectionRequestEntity connectionRequestEntity = new ConnectionRequestEntity(senderUserEntity, receiverUserEntity);
        this.connectionRequestRepository.save(connectionRequestEntity);

        return new Response("Request sent");
    }

    @Transactional
    public Response approveConnectionRequest(
            ConnectionActionRequest connectionActionRequest,
            UserDetails userDetails
    ) throws BadRequestException {
        String connectionRequestId = connectionActionRequest.getConnectionRequestId();
        Optional<ConnectionRequestEntity> request = this.connectionRequestRepository.findById(connectionRequestId);
        if (request.isEmpty())
            throw new EntityNotFoundException("Request not found");

        ConnectionRequestEntity requestEntity = request.get();
        if (requestEntity.getStatus().equals(ConnectionRequestStatusEnum.ACCEPTED))
            throw new BadRequestException("Request already accepted");
        if (requestEntity.getStatus().equals(ConnectionRequestStatusEnum.REJECTED))
            throw new BadRequestException("Request already rejected");

        UserEntity sender = requestEntity.getSender();
        UserEntity receiver = requestEntity.getReceiver();

        if (!receiver.getUser().getUserId().equals(userDetails.getUsername())) {
            throw new BadRequestException("Bad request");
        }

        requestEntity.setStatus(ConnectionRequestStatusEnum.ACCEPTED);

        this.connectionRequestRepository.save(requestEntity);

        ConnectionEntity connectionForSender = new ConnectionEntity(sender, receiver);
        ConnectionEntity connectionForReceiver = new ConnectionEntity(receiver, sender);

        this.connectionRepository.save(connectionForSender);
        this.connectionRepository.save(connectionForReceiver);

        return new Response("Accepted");
    }

    public Response rejectConnectionRequest(
            ConnectionActionRequest connectionActionRequest,
            UserDetails userDetails
    ) throws BadRequestException {
        String connectionRequestId = connectionActionRequest.getConnectionRequestId();
        Optional<ConnectionRequestEntity> request = this.connectionRequestRepository.findById(connectionRequestId);
        if (request.isEmpty())
            throw new EntityNotFoundException("Request not found");

        ConnectionRequestEntity requestEntity = request.get();
        if (requestEntity.getStatus().equals(ConnectionRequestStatusEnum.ACCEPTED))
            throw new BadRequestException("Request already accepted");
        if (requestEntity.getStatus().equals(ConnectionRequestStatusEnum.REJECTED))
            throw new BadRequestException("Request already rejected");

        UserEntity sender = requestEntity.getSender();
        UserEntity receiver = requestEntity.getReceiver();

        if (receiver.getUser().getUserId().equals(userDetails.getUsername())) {
            requestEntity.setStatus(ConnectionRequestStatusEnum.REJECTED);
            this.connectionRequestRepository.save(requestEntity);
            return new Response("Rejected");
        } else if (sender.getUser().getUserId().equals(userDetails.getUsername())) {
            requestEntity.setStatus(ConnectionRequestStatusEnum.REJECTED);
            this.connectionRequestRepository.save(requestEntity);
            return new Response("Cancelled");
        }

        throw new BadRequestException("Bad request");

    }


    protected void handleIdenticalConnectionRequest(
            ConnectionRequestEntity connectionRequestEntity
    ) throws BadRequestException {

        if (connectionRequestEntity.getStatus().equals(ConnectionRequestStatusEnum.PENDING))
            throw new BadRequestException("Request already sent");
        if (connectionRequestEntity.getStatus().equals(ConnectionRequestStatusEnum.ACCEPTED))
            throw new BadRequestException("Connection request already accepted");

    }

    protected void handleReverseConnectionRequest(
            ConnectionRequestEntity connectionRequestEntity
    ) throws BadRequestException {

        if (connectionRequestEntity.getStatus().equals(ConnectionRequestStatusEnum.PENDING))
            throw new BadRequestException("User already sent you a connection request");
        if (connectionRequestEntity.getStatus().equals(ConnectionRequestStatusEnum.ACCEPTED))
            throw new BadRequestException("User is already a friend");
    }
}
