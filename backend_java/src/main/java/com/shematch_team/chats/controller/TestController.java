package com.shematch_team.chats.controller;

import com.shematch_team.chats.entity.UsersChats;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository;
    private final UsersChats usersChats;

    @Autowired
    public TestController(ChatsRepository chatsRepository, UserRepository userRepository, UsersChats usersChats) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.usersChats = usersChats;
    }

    @GetMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("OKOKOKOKOK");
    }

}
