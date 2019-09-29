package com.shematch_team.chats.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.shematch_team.chats.component.PhotoSearch;
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

    private final PhotoSearch photoSearch;

    private final VkBot vkBot;

    @Autowired
    public ChatsService(UserRepository userRepository, ChatsRepository chatsRepository, PhotoSearch photoSearch, VkBot vkBot) {
        this.userRepository = userRepository;
        this.chatsRepository = chatsRepository;
        this.photoSearch = photoSearch;
        this.vkBot = vkBot;
    }

    private Chat createOrFindChat(String interest) throws Exception {
        Chat chat = chatsRepository.findFirstByInterest(interest.toLowerCase());
        if (chat == null) {
            chat = new Chat();
            chat.setInterest(interest.toLowerCase());
            chat.setPreview(photoSearch.findImageByName(interest.toLowerCase()));
            //vkBot.getChatLink(chat);
            chatsRepository.save(chat);
            chat = chatsRepository.findFirstByInterest(interest.toLowerCase());
        }

        return chat;
    }

    public void createChatsForUser(UserRequestDto userRequestDto) throws Exception {
        List<String> interests = getInterestsFromPython(userRequestDto);
        int counter = 0;
        for (String interest : interests) {
            String tempStr = toUpperCaseForFirstLetter(interest);
            interests.set(counter, tempStr);
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
        String info = userRequestDto.getInfoJson().toString();
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
