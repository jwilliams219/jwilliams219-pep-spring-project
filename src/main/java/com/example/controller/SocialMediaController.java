package com.example.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    @RequestMapping(method = RequestMethod.GET)
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping(params = "messageId") 
    public ResponseEntity<Message> findMessagebyId(@RequestParam int messageId) {
        return new ResponseEntity<>(messageService.findMessageById(messageId), HttpStatus.OK);
    }

    @DeleteMapping("delete/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable int messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.accepted().body("Successfully deleted");
    }

    @PostMapping("create") 
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        messageService.addMessage(newMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMessage);
    }
}
