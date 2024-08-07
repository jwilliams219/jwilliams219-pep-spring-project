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

    /**
     * @return all messages from db
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * @param id
     * @return message with id
     */
    public Message findMessageById(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        return null;
    }

    /**
     * @param id
     * @return rows deleted
     */
    public int deleteMessage(int id) {
        if (!messageRepository.existsById(id)) {
            return 0;
        }
        messageRepository.deleteById(id);
        return 1;
    }

    /**
     * @param message
     * @return message added to db
     */
    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    /**
     * Update a message in the db
     * @param newMessage
     * @throws Exception if message with id is not in db
     */
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

    /**
     * @param postedById
     * @return messages all messages in db with given accountId
     */
    public List<Message> findMessagesByUser(int postedById) {
        return messageRepository.findMessagesByUser(postedById);
    }
}
