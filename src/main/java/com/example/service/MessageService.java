package com.example.service;

import com.example.entity.*;
import com.example.repository.MessageRepository;

import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MessageService {

    private MessageRepository messageRepository;


    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message findMessageById(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        return null;
    }

    public void deleteMessage(int id) {
        messageRepository.deleteById(id);
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public void patchMessage(Message newMessage) throws Exception {
        Optional<Message> oldMessage = messageRepository.findById(newMessage.getMessageId());
        if (oldMessage.isPresent()) {
            Message message = oldMessage.get();
            message.setMessageText(newMessage.getMessageText());
            if (newMessage.getPostedBy() != null) {
                message.setPostedBy(newMessage.getPostedBy());
            }
            if (newMessage.getTimePostedEpoch() != null) {
                message.setTimePostedEpoch(newMessage.getTimePostedEpoch());
            }
            messageRepository.save(message);
        } else {
            throw new Exception("Message with id not found");
        }
    }

}
