package com.example.controller;

import javax.naming.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.service.*;
import com.example.entity.*;

import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
@RequestMapping
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    /*
     * 1: Our API should be able to process new User registrations
     */
    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account newAccount) {
        if (newAccount.getUsername().isEmpty() || newAccount.getUsername() == "" || newAccount.getPassword().length() < 4) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Account account = accountService.register(newAccount);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /*
     * 2: Our API should be able to process User logins
     */
    public class LoginRequest {
        String username;
        String password;
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody LoginRequest loginRequest) {
        try {
            Account account = accountService.login(loginRequest.username, loginRequest.password);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /*
     * 3: Our API should be able to process the creation of new messages
     */
    @PostMapping("messages") 
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        if (newMessage.getMessageText().isEmpty() || newMessage.getMessageText() == "" || newMessage.getMessageText().length() > 255 
            || newMessage.getPostedBy() == null || !accountService.accountExists(newMessage.getPostedBy())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        messageService.addMessage(newMessage);
        return ResponseEntity.status(HttpStatus.OK).body(newMessage);
    }

    /*
     * 4: Our API should be able to retrieve all messages
     */
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }

    /*
     * 5: Our API should be able to retrieve a message by its ID
     */
    @GetMapping("messages/{messageId}") 
    public ResponseEntity<Message> findMessagebyId(@PathVariable int messageId) {
        return new ResponseEntity<>(messageService.findMessageById(messageId), HttpStatus.OK);
    }

    /*
     * 6: Our API should be able to delete a message identified by a message ID
     */
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable int messageId) {
        int deleted = messageService.deleteMessage(messageId);
        if (deleted == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    /*
     * 7: Our API should be able to update a message text identified by a message ID
     */
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> patchMessage(@PathVariable int messageId, @RequestBody Message newMessage) {
        if (newMessage.getMessageText().isEmpty() || newMessage.getMessageText() == "" || newMessage.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            messageService.patchMessage(newMessage);
            return new ResponseEntity<>(1, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /*
     * 8: Our API should be able to retrieve all messages written by a particular user
     */
    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> findMessagesByUser(@PathVariable int accountId) {
        return new ResponseEntity<>(messageService.findMessagesByUser(accountId), HttpStatus.OK);
    }
}
