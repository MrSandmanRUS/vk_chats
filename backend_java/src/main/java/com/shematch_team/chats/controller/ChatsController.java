package com.shematch_team.chats.controller;

import com.shematch_team.chats.component.VkBot;
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
import java.util.*;

@Controller
public class ChatsController {

    private final ChatsRepository chatsRepository;
    private final UserRepository userRepository;
    private final UsersChatsRepository usersChatsRepository;
    private final ChatsService chatsService;
    private final UserService userService;
    private final VkBot vkBot;
    @Value("${config.token}")
    private String token;


    @Autowired
    public ChatsController(ChatsRepository chatsRepository, UserRepository userRepository, ChatsService chatsService,
                           UserService userService, UsersChatsRepository usersChatsRepository, VkBot vkBot) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.chatsService = chatsService;
        this.userService = userService;
        this.usersChatsRepository = usersChatsRepository;
        this.vkBot = vkBot;
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
        List<Chat> allChatsRes = new ArrayList<>();

        for (Chat chat : allChats) {
            String tempStr = toUpperCaseForFirstLetter(chat.getInterest());
            chat.setInterest(tempStr);
            allChatsRes.add(chat);
        }

        return ResponseEntity.ok(allChatsRes);
    }

    @PostMapping("getRecommended")
    public ResponseEntity<Set<Chat>> getRecommended(@RequestBody UserRequestDto userRequestDto) throws Exception {
        if (isCorrectUser(userRequestDto)) {
            String vkId = userRequestDto.getVkId();
            Optional<User> user = userRepository.findByVkId(vkId);
            if (!user.isPresent()) {
                userService.translateInfo(userRequestDto);
                userService.save(userRequestDto);
                chatsService.createChatsForUser(userRequestDto);
            }
        } else {
            throw new Exception("Incorrect User");
        }
        return ResponseEntity.ok(getRecommendedChats(userRequestDto));
    }

    @GetMapping("getChatLink")
    public ResponseEntity<Chat> getRecommended(@RequestParam("chat_id") Long chatId) throws Exception {
        Chat chat = chatsRepository.findById(chatId).orElse(null);
        if (chat == null) {
            throw new Exception("Incorrect chat id");
        }

        if (chat.getLink() != null) {
            vkBot.getChatLink(chat);
            chatsRepository.save(chat);
            chat = chatsRepository.findById(chatId).orElse(null);
        }

        return ResponseEntity.ok(chat);
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
                User tempUser = userRepository.findById(usersChats.getUserId()).orElse(null);
                if (tempUser != null && !tempUser.getVkId().equals(vkId)) {
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
        Set<Chat> tempRes = user.get().getChats();
        HashSet<Chat> res = new HashSet<>();

        for (Chat chat : tempRes) {
            String tempStr = toUpperCaseForFirstLetter(chat.getInterest());
            chat.setInterest(tempStr);
            res.add(chat);

        }

        return res;
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

    private String toUpperCaseForFirstLetter(String text) {
        StringBuilder builder = new StringBuilder(text);
        //выставляем первый символ заглавным, если это буква
        if (Character.isAlphabetic(text.codePointAt(0)))
            builder.setCharAt(0, Character.toUpperCase(text.charAt(0)));

        //крутимся в цикле, и меняем буквы, перед которыми пробел на заглавные
        for (int i = 1; i < text.length(); i++)
            if (Character.isAlphabetic(text.charAt(i)) && Character.isSpaceChar(text.charAt(i - 1)))
                builder.setCharAt(i, Character.toUpperCase(text.charAt(i)));

        return builder.toString();
    }

}
