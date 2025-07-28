package com.example.echo.services;

import com.example.echo.entities.*;
import com.example.echo.enums.PublishableTypeEnum;
import com.example.echo.repositories.MessageAttachmentRepository;
import com.example.echo.repositories.MessageRepository;
import com.example.echo.requests.MessageRequest;
import com.example.echo.responses.GetResponse;
import com.example.echo.responses.MessageResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    UserService userService;
    WebSocketService webSocketService;
    MessageRepository messageRepository;
    AttachmentService attachmentService;
    ParticipantService participantService;
    ConversationService conversationService;
    MessageAttachmentRepository messageAttachmentRepository;

    @Autowired
    public MessageService(
            UserService userService,
            WebSocketService webSocketService,
            MessageRepository messageRepository,
            AttachmentService attachmentService,
            ParticipantService participantService,
            ConversationService conversationService,
            MessageAttachmentRepository messageAttachmentRepository
    ) {
        this.userService = userService;
        this.webSocketService = webSocketService;
        this.messageRepository = messageRepository;
        this.participantService = participantService;
        this.conversationService = conversationService;
        this.messageAttachmentRepository = messageAttachmentRepository;
        this.attachmentService = attachmentService;
    }

    @Transactional
    protected MessageEntity saveMessage(
            String message,
            UserEntity user,
            ConversationEntity conversation
    ) {
        return this.messageRepository.save(new MessageEntity(
                message,
                user,
                conversation
        ));
    }

    @Transactional
    protected void saveMessageAttachment(
            List<AttachmentEntity> attachmentEntities,
            MessageEntity message

    ) {
        List<MessageAttachmentEntity> messageAttachmentEntities = List.of();
        for (AttachmentEntity attachment : attachmentEntities) {
            messageAttachmentEntities.add(new MessageAttachmentEntity(attachment, message));
        }

        this.messageAttachmentRepository.saveAll(messageAttachmentEntities);
    }

    @Transactional
    public MessageEntity createNewMessage(
            String conversationId,
            MessageRequest messageRequest,
            UserDetails userDetails
    ) throws BadRequestException {
        UserEntity user = this.userService.getUserFromUserDetails(userDetails);

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(conversationId);
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");


        ConversationEntity conversation = conversationEntity.get();
        List<ParticipantEntity> participantEntities = this.participantService.getParticipantListByConversation(conversation);
        List<UserEntity> participantUserList = participantEntities.stream().map(
                ParticipantEntity::getUser
        ).toList();

        Boolean isParticipantAllowed = this.participantService.isParticipantAllowed(user, conversation);
        if (!isParticipantAllowed)
            throw new BadRequestException("User not allowed");

        MessageEntity messageEntity = this.saveMessage(messageRequest.getMessage(), user, conversation);
        this.saveMessageAttachment(messageRequest.getAttachments(), messageEntity);

        Optional<MessageEntity> newMessageEntity = this.messageRepository.findById(messageEntity.getMessageId());
        if (newMessageEntity.isEmpty())
            throw new RuntimeException("Message save failed");

        MessageEntity savedMessage = newMessageEntity.get();
        MessageResponse messageResponse = new MessageResponse(savedMessage, PublishableTypeEnum.CREATE_MESSAGE);

        this.webSocketService.publishToUserList(participantUserList, messageResponse);
        return savedMessage;
    }


    public AttachmentEntity uploadAttachment(
            String conversationId,
            MultipartFile file,
            UserDetails userDetails
    ) throws IOException {
        UserEntity user = this.userService.getUserFromUserDetails(userDetails);

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(conversationId);
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");

        ConversationEntity conversation = conversationEntity.get();

        Boolean isParticipantAllowed = this.participantService.isParticipantAllowed(user, conversation);
        if (!isParticipantAllowed)
            throw new BadRequestException("User not allowed");

        return this.attachmentService.saveFile(file);
    }

    public byte[] downloadAttachment(
            String conversationId,
            String key,
            UserDetails userDetails
    ) throws BadRequestException {
        UserEntity user = this.userService.getUserFromUserDetails(userDetails);

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(conversationId);
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");

        ConversationEntity conversation = conversationEntity.get();

        Boolean isParticipantAllowed = this.participantService.isParticipantAllowed(user, conversation);
        if (!isParticipantAllowed)
            throw new BadRequestException("User not allowed");

        return this.attachmentService.getFile(key);
    }

    public GetResponse<List<MessageEntity>> getMessage(
            String conversationId,
            UserDetails userDetails,
            Optional<Long> before
    ) throws BadRequestException {
        UserEntity user = this.userService.getUserFromUserDetails(userDetails);

        Optional<ConversationEntity> conversationEntity = this.conversationService.getConversationById(conversationId);
        if (conversationEntity.isEmpty())
            throw new EntityNotFoundException("Conversation not found");

        ConversationEntity conversation = conversationEntity.get();

        Boolean isParticipantAllowed = this.participantService.isParticipantAllowed(user, conversation);
        if (!isParticipantAllowed)
            throw new BadRequestException("User not allowed");

        List<MessageEntity> messages = (before.isEmpty())?
        this.messageRepository.findFirst50ByConversationOrderByMessageIdDesc(conversation)
        :this.messageRepository.findFirst50ByConversationAndMessageIdLessThanOrderByMessageIdDesc(conversation, before.get());

        return new GetResponse<>("Messages", messages);
    }



}
