package com.shematch_team.chats.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.entity.User;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import com.shematch_team.chats.service.ChatsService;
import com.shematch_team.chats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatsController {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository;
    private final ChatsService chatsService;
    private final UserService userService;

    @Autowired
    public ChatsController(ChatsRepository chatsRepository, UserRepository userRepository, ChatsService chatsService, UserService userService) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.chatsService = chatsService;
        this.userService = userService;
    }

    @GetMapping("getAll")
    public List<Chat> getAll(@RequestParam("start_id") Long startId,
                             @RequestParam("page") Integer page) {
        PageRequest pageable = new PageRequest(page, 20);
        List<Chat> allChats;
        if (startId == -1) {
            allChats = chatsRepository.findAllByOrderByIdDesc(pageable);
        } else {
            allChats = chatsRepository.findAllByIdLessThanEqualOrderByIdDesc(startId, pageable);
        }
        return allChats;
    }

    @PostMapping("/getRecommended")
    public List<Chat> getRecommended(@RequestBody UserRequestDto userRequestDto) throws Exception {
        String vkId = userRequestDto.getVkId();
        Optional<User> user = userRepository.findByVkId(vkId);
        if (!user.isPresent()) {
            userService.save(userRequestDto);
            chatsService.createChatsForUser(userRequestDto);
        }
        return getRecommendedChats(userRequestDto);
    }

    private List<Chat> getRecommendedChats(UserRequestDto userRequestDto) {
        String vkId = userRequestDto.getVkId();
        Optional<User> user = userRepository.findByVkId(vkId);
        List<Chat> recommendedChats = new ArrayList<>(user.get().getChats());
        return recommendedChats;
    }


}
