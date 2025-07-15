package com.example.echo.controllers;

import com.example.echo.entities.ConnectionEntity;
import com.example.echo.entities.ConnectionRequestEntity;
import com.example.echo.requests.ConnectionActionRequest;
import com.example.echo.requests.ConnectionRequest;
import com.example.echo.responses.GetResponse;
import com.example.echo.responses.Response;
import com.example.echo.services.ConnectionService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/connection")
public class ConnectionController {

    ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @GetMapping("/sent")
    public ResponseEntity<GetResponse<List<ConnectionRequestEntity>>> getSentConnectionRequests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        GetResponse<List<ConnectionRequestEntity>> response = this.connectionService.getSentConnectionRequests(userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<GetResponse<List<ConnectionRequestEntity>>> getReceivedConnectionRequests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        GetResponse<List<ConnectionRequestEntity>> response = this.connectionService.getReceivedConnectionRequests(userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/connected")
    public ResponseEntity<GetResponse<List<ConnectionEntity>>> getConnectedList(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        GetResponse<List<ConnectionEntity>> response = this.connectionService.getConnectedList(userDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity<Response> sendConnectionRequest(
            @Valid @RequestBody ConnectionRequest connectionRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws BadRequestException {
        Response response = this.connectionService.sendConnectionRequest(
                connectionRequest,
                userDetails
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/approve")
    public ResponseEntity<Response> approveConnectionRequest(
            @Valid @RequestBody ConnectionActionRequest connectionActionRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws BadRequestException {
        Response response = this.connectionService.approveConnectionRequest(
                connectionActionRequest,
                userDetails
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/reject")
    public ResponseEntity<Response> rejectConnectionRequest(
            @Valid @RequestBody ConnectionActionRequest connectionActionRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws BadRequestException {
        Response response = this.connectionService.rejectConnectionRequest(
                connectionActionRequest,
                userDetails
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
