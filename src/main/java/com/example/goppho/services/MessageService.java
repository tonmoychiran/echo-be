package com.example.goppho.services;

import com.example.goppho.entities.MessageEntity;
import com.example.goppho.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    MessageRepository messageRepository;

    @Autowired
    public MessageService(
            MessageRepository messageRepository
    ) {
        this.messageRepository = messageRepository;
    }


    public MessageEntity createNewMessage(
            MessageEntity messageEntity
    ) {
        return messageRepository.save(messageEntity);
    }
}
