package com.shematch_team.chats.controller;

import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.repository.ChatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChatsController {

    private final ChatsRepository chatsRepository;

    @Autowired
    public ChatsController(ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    @GetMapping("getAllChats")
    public ResponseEntity<List<Chat>> getAllChats(){
        List<Chat> allChats = chatsRepository.findAll();
        return ResponseEntity.ok(allChats);
    }

}
