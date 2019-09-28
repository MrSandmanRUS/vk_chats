package com.shematch_team.chats.controller;

import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.entity.User;
import com.shematch_team.chats.entity.UsersChats;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import com.shematch_team.chats.repository.UsersChatsRepository;
import com.shematch_team.chats.service.ChatsService;
import com.shematch_team.chats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ChatsController {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository;
    private final UsersChatsRepository usersChatsRepository;
    private final ChatsService chatsService;
    private final UserService userService;
    @Value("${config.token}")
    private String token;


    @Autowired
    public ChatsController(ChatsRepository chatsRepository, UserRepository userRepository, ChatsService chatsService,
                           UserService userService, UsersChatsRepository usersChatsRepository) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.chatsService = chatsService;
        this.userService = userService;
        this.usersChatsRepository = usersChatsRepository;
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
    public ResponseEntity<Set<Chat>> getRecommended(@RequestBody UserRequestDto userRequestDto) throws Exception {
        if (isCorrectUser(userRequestDto)) {
            String vkId = userRequestDto.getVkId();
            Optional<User> user = userRepository.findByVkId(vkId);
            if (!user.isPresent()) {
              //  userService.translateInfo(userRequestDto);
                userService.save(userRequestDto);
                chatsService.createChatsForUser(userRequestDto);
            }
        } else {
            throw new Exception("Incorrect User");
        }
        return ResponseEntity.ok(getRecommendedChats(userRequestDto));
    }


    @GetMapping("getLikeUser")
    public ResponseEntity<Set<User>> getLikeUser(@RequestParam("vk_id") String vkId,
                                                 @RequestParam("start_id") Long startId,
                                                 @RequestParam("page") Integer page) throws Exception {
        Optional<User> user = userRepository.findByVkId(vkId);
        HashSet<User> users = new HashSet<>();

        if (user.isPresent()) {
            User curUser = user.orElse(null);

            PageRequest pageable = new PageRequest(page, 20);

            List<UsersChats> usersChatsArray;
            if (startId == -1) {
                usersChatsArray = usersChatsRepository.findAllByUserIdOrderById(curUser.getId(), pageable);
            } else {
                usersChatsArray = usersChatsRepository.findAllByUserIdAndIdLessThanEqualOrderById(curUser.getId(), startId, pageable);
            }

            for (UsersChats usersChats : usersChatsArray) {
                User tempUser = userRepository.findById(usersChats.getId()).orElse(null);
                if (tempUser != null) {
                    users.add(tempUser);
                }
            }


        } else {
            throw new Exception("User doesn't exist");
        }

        return ResponseEntity.ok(users);
    }


    private Set<Chat> getRecommendedChats(UserRequestDto userRequestDto) {
        String vkId = userRequestDto.getVkId();
        Optional<User> user = userRepository.findByVkId(vkId);
        return user.get().getChats();
    }

    private boolean isCorrectUser(UserRequestDto userRequestDto) throws IOException {
//        String vkId = userRequestDto.getVkId();
//        String ip = userRequestDto.getIp();
//        String vkToken = userRequestDto.getVkToken();
//        String urlToRead = "https://api.vk.com/method/secure.checkToken?ip=" + ip + "&token=" + token + "&access_token=" + vkToken + "&v=5.101";
//        StringBuilder result = new StringBuilder();
//        URL url = new URL(urlToRead);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        String line;
//        while ((line = rd.readLine()) != null) {
//            result.append(line);
//        }
//        rd.close();
//        JSONObject jsonResponse = new JSONObject(result.toString());
//        jsonResponse = (JSONObject) jsonResponse.get("response");
//
//        Integer vkIdResponse = (Integer) jsonResponse.get("user_id");
//
//        if (!vkIdResponse.toString().equals(vkId)) {
//            return false;
//        } else {
//            return true;
//        }
        return true;
    }


}
