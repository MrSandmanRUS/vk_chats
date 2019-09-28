package com.shematch_team.chats.controller;

import com.shematch_team.chats.component.InterestGen;
import com.shematch_team.chats.component.PhotoSearch;
import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import com.shematch_team.chats.repository.UsersChatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@Controller
public class TestController {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository;
    private final UsersChatsRepository usersChatsRepository;
    private final PhotoSearch photoSearch;
    private final InterestGen interestGen;

    @Autowired
    public TestController(ChatsRepository chatsRepository,
                          UserRepository userRepository,
                          UsersChatsRepository usersChatsRepository,
                          PhotoSearch photoSearch,
                          InterestGen interestGen) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.usersChatsRepository = usersChatsRepository;
        this.photoSearch = photoSearch;
        this.interestGen = interestGen;
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

    @PostMapping("getInterest")
    public ResponseEntity<String> getInterest(@RequestBody UserRequestDto userRequestDto) throws IOException {
        List<String> interest = interestGen.getInterestsFromPython(userRequestDto);


        return ResponseEntity.ok(interest.toString());
    }

}
