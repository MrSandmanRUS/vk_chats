package com.shematch_team.chats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shematch_team.chats.component.PhotoSearch;
import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.entity.User;
import com.shematch_team.chats.repository.ChatsRepository;
import com.shematch_team.chats.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class ChatsService {

    private final UserRepository userRepository;

    private final ObjectMapper om = new ObjectMapper();

    private final ChatsRepository chatsRepository;

    private final PhotoSearch photoSearch;


    @Autowired
    public ChatsService(UserRepository userRepository, ChatsRepository chatsRepository, PhotoSearch photoSearch) {
        this.userRepository = userRepository;
        this.chatsRepository = chatsRepository;
        this.photoSearch = photoSearch;
    }

    private Chat createOrFindChat(String interest) throws Exception {
        Chat chat = chatsRepository.findFirstByInterest(interest.toLowerCase());
        if (chat == null) {
            chat = new Chat();
            chat.setInterest(interest.toLowerCase());
            chat.setPreview(photoSearch.findImageByName(interest.toLowerCase()));
            chatsRepository.save(chat);
            chat = chatsRepository.findFirstByInterest(interest.toLowerCase());
        }

        return chat;
    }

    public void createChatsForUser(UserRequestDto userRequestDto) throws Exception {
        List<String> interests = getInterestsFromPython(userRequestDto);
        int counter = 0;
        for (String interest : interests) {
            interests.set(counter, toUpperCaseForFirstLetter(interest));
            ++counter;
        }
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
