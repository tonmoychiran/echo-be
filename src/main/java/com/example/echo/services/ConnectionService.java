package com.example.echo.services;

import com.example.echo.entities.ConnectionEntity;
import com.example.echo.entities.ConnectionRequestEntity;
import com.example.echo.entities.UserEntity;
import com.example.echo.repositories.ConnectionRepository;
import com.example.echo.repositories.ConnectionRequestRepository;
import com.example.echo.requests.ConnectionActionRequest;
import com.example.echo.requests.ConnectionRequest;
import com.example.echo.responses.GetResponse;
import com.example.echo.responses.Response;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public GetResponse<List<ConnectionEntity>> getConnectedList(
            UserDetails userDetails
    ){
        UserEntity user = userService.getUserFromUserDetails(userDetails);
        List<ConnectionEntity> connectedList = this
                .connectionRepository
                .findAllByUser(user);
        return new GetResponse<>("Friends List", connectedList);
    }

    public Response sendConnectionRequest(
            ConnectionRequest connectionRequest,
            UserDetails userDetails
    ) throws BadRequestException {

        UserEntity userEntity = userService.getUserFromUserDetails(userDetails);

        Optional<UserEntity> friendUser = this.userService.getUserByUsername(connectionRequest.getUsername());
        if (friendUser.isEmpty()) {
            throw new EntityNotFoundException("Receiver not found");
        }

        UserEntity friendEntity = friendUser.get();
        Optional<ConnectionEntity> isAlreadyFriends = this.connectionRepository.findByUserAndFriend(userEntity, friendEntity);
        if (isAlreadyFriends.isPresent()) {
            throw new BadRequestException("User is already a friend");
        }

        Optional<ConnectionRequestEntity> identicalRequest = this.connectionRequestRepository.findBySenderAndReceiver(userEntity, friendEntity);
        if (identicalRequest.isPresent()) {
            throw new BadRequestException("Request already sent");
        }

        Optional<ConnectionRequestEntity> reverseRequest = this.connectionRequestRepository.findBySenderAndReceiver(userEntity, friendEntity);
        if (reverseRequest.isPresent()) {
            throw new BadRequestException("User already sent you a connection request");
        }

        ConnectionRequestEntity connectionRequestEntity = new ConnectionRequestEntity(userEntity, friendEntity);
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

        UserEntity userEntity = requestEntity.getSender();
        UserEntity friendEntity = requestEntity.getReceiver();

        if (!friendEntity.getUserId().equals(userDetails.getUsername())) {
            throw new BadRequestException("Bad request");
        }

        ConnectionEntity connectionForSender = new ConnectionEntity(userEntity, friendEntity);
        ConnectionEntity connectionForReceiver = new ConnectionEntity(friendEntity, userEntity);

        this.connectionRepository.save(connectionForSender);
        this.connectionRepository.save(connectionForReceiver);
        this.connectionRequestRepository.delete(requestEntity);

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

        UserEntity userEntity = requestEntity.getSender();
        UserEntity friendEntity = requestEntity.getReceiver();

        this.connectionRequestRepository.delete(requestEntity);
        if (friendEntity.getUserId().equals(userDetails.getUsername())) {
            return new Response("Rejected");
        } else if (userEntity.getUserId().equals(userDetails.getUsername())) {
            return new Response("Cancelled");
        }

        throw new BadRequestException("Bad request");

    }

}
