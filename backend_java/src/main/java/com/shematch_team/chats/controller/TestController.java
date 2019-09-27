package com.shematch_team.chats.controller;

import com.shematch_team.chats.component.PhotoSearch;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import com.shematch_team.chats.repository.UsersChatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository;
    private final UsersChatsRepository usersChatsRepository;
    private final PhotoSearch photoSearch;

    @Autowired
    public TestController(ChatsRepository chatsRepository,
                          UserRepository userRepository,
                          UsersChatsRepository usersChatsRepository, PhotoSearch photoSearch) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.usersChatsRepository = usersChatsRepository;
        this.photoSearch = photoSearch;
    }

    @GetMapping("test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("OKOKOKOKOK");
    }

    @GetMapping("testDriver")
    public ResponseEntity<String> testDriver(){
        String page = photoSearch.findImageByName("test");


        return ResponseEntity.ok(page);
    }

}
