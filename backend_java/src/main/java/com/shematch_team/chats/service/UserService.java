package com.shematch_team.chats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shematch_team.chats.component.Translator;
import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.User;
import com.shematch_team.chats.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    private final ObjectMapper om = new ObjectMapper();
    private final UserRepository userRepository;
    private final Translator translator;

    @Autowired
    public UserService(UserRepository userRepository, Translator translator) {
        this.userRepository = userRepository;
        this.translator = translator;
    }

    public void save(UserRequestDto userRequestDto) throws JsonProcessingException {
        User user = new User();
        JSONObject info = userRequestDto.getInfo();
        user.setInfo(om.writeValueAsString(info));
        user.setVkId(userRequestDto.getVkId());
        userRepository.save(user);
    }

    public void translateInfo(UserRequestDto userRequestDto) throws IOException {
        JSONObject info = userRequestDto.getInfo();
        String infoString = om.writeValueAsString(info);
        String translated = translator.getFromYandexService(infoString).get();
        JSONObject translatedObject = new JSONObject(translated.replace("\n","").replace("\r",""));
        userRequestDto.setInfo(translatedObject);
    }

    private void translateInfoForMap(Map<String, Object> info) {
        Set<String> keySet = info.keySet();
        for (String key : keySet) {
            Object childObject = info.get(key);
            Class<?> childObjectClass = childObject.getClass();
            if (ArrayList.class.equals(childObjectClass)) {
                translateInfoForList((ArrayList<Object>) childObject);
            } else if (LinkedHashMap.class.equals(childObjectClass)) {
                translateInfoForMap((Map<String, Object>) childObject);
            } else if (String.class.equals(childObjectClass)) {
                translateIfNecessary(info, key);
            }
        }
    }

    private void translateIfNecessary(Map<String, Object> info, String key) {
        String words = (String) info.get(key);
        String translatedWords = getTranslatedWords(words);
        info.put(key, translatedWords);
    }

    private void translateIfNecessary(ListIterator<Object> iterator, String words) {
        String translatedWords = getTranslatedWords(words);
        iterator.set(translatedWords);
    }

    private String getTranslatedWords(String words) {
        StringBuilder translatedWordsBuilder = new StringBuilder();
        //todo can be handled in parallel in stringbuffer
        String[] wordsArray = words.split("[\\s,\\.:\\-_#\\{\\}\\[\\]]");
        for (String word : wordsArray) {
            if (isEnglish(word)) {
                Optional<String> russian = Optional.empty();// = translator.translateToRussian(word);
                if (russian.isPresent()) {
                    translatedWordsBuilder.append(russian.get());
                } else {
                    translatedWordsBuilder.append(word);
                }
            } else {
                translatedWordsBuilder.append(word);
            }
            translatedWordsBuilder.append(" ");
        }
        return translatedWordsBuilder.toString();
    }

    private boolean isEnglish(String word) {
        return word.matches("[a-zA-Z]+");
    }

    private void translateInfoForList(ArrayList<Object> list) {
        ListIterator<Object> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            Object listItem = listIterator.next();
            Class<?> listItemClass = listItem.getClass();
            if (ArrayList.class.equals(listItemClass)) {
                translateInfoForList((ArrayList<Object>) listItem);
            } else if (LinkedHashMap.class.equals(listItemClass)) {
                translateInfoForMap((Map) listItem);
            } else if (String.class.equals(listItemClass)) {
                translateIfNecessary(listIterator, (String) listItem);
            }
        }
    }
}
