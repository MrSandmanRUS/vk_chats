package com.shematch_team.chats.controller;

import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.repository.ChatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChatsController {

    private final ChatsRepository chatsRepository;

    @Autowired
    public ChatsController(ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Chat>> getAll(@RequestParam("start_id") Long startId,
                                             @RequestParam("page") Integer page) {
        PageRequest pageable = new PageRequest(page, 20);
        List<Chat> allChats;
        if (startId == -1) {
            allChats = chatsRepository.findAllByOrderByIdDesc(pageable);
        } else {
            allChats = chatsRepository.findAllByIdLessThanEqualOrderByIdDesc(startId, pageable);
        }
        return ResponseEntity.ok(allChats);
    }

    @PostMapping("getRecommended")
    public ResponseEntity<List<Chat>> getRecommended(UserRequestDto userRequestDto) {
        return null;
    }


}
