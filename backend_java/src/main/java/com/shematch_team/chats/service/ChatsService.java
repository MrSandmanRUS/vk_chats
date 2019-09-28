package com.shematch_team.chats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.shematch_team.chats.component.VkBot;
import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.entity.User;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

@Service
public class ChatsService {

    private final UserRepository userRepository;

    private final ObjectMapper om = new ObjectMapper();

    private final ChatsRepository chatsRepository;
    private final VkBot vkBot;

    @Autowired
    public ChatsService(UserRepository userRepository, ChatsRepository chatsRepository, VkBot vkBot) {
        this.userRepository = userRepository;
        this.chatsRepository = chatsRepository;
        this.vkBot = vkBot;
    }

    private Chat createOrFindChat(String interest) throws Exception {
        Chat chat = chatsRepository.findFirstByInterest(interest.toLowerCase());
        if (chat == null) {
            chat = new Chat();
            chat.setInterest(interest.toLowerCase());
            vkBot.createChat(chat);
            chatsRepository.save(chat);
            chat = chatsRepository.findFirstByInterest(interest.toLowerCase());
        }

        return chat;
    }

    public void createChatsForUser(UserRequestDto userRequestDto) throws Exception {
        List<String> interests = getInterestsFromPython(userRequestDto);
        Set<Chat> chats = Sets.newHashSet();
        for (String interest : interests) {
            Chat chat = createOrFindChat(interest);
            chats.add(chat);
        }
        String vkId = userRequestDto.getVkId();
        User user = userRepository.findByVkId(vkId).get();
        user.setChats(chats);
        userRepository.save(user);
    }


    private List<String> getInterestsFromPython(UserRequestDto userRequestDto) throws IOException {
        String info = om.writeValueAsString(userRequestDto.getInfo());
        String query = "http://127.0.0.1:81/getInterest";
        URL url = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        OutputStream os = conn.getOutputStream();
        os.write(info.getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        in.close();
        conn.disconnect();
        return om.readValue(result, List.class);
    }

}
