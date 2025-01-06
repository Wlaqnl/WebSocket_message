package org.fastcampus.chatservice.controllers;

import org.fastcampus.chatservice.entities.Chatroom;
import org.fastcampus.chatservice.entities.Member;
import org.fastcampus.chatservice.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    ChatService chatService;

    @GetMapping("/test")
    public ResponseEntity<List<Chatroom>> getChatroomList(@RequestParam Long id) {
        System.out.println("id@@@@@@@@@@@@@@@");
        System.out.println(id);
        return ResponseEntity.ok(chatService.getChatroomList(Member.builder().id(id).build()));
    }

}
