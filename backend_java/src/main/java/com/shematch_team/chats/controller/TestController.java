package com.shematch_team.chats.controller;

import com.shematch_team.chats.component.PhotoSearch;
import com.shematch_team.chats.component.Translator;
import com.shematch_team.chats.component.VkBot;
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
    private final VkBot vkBot;
    private final Translator translator;

    @Autowired
    public TestController(ChatsRepository chatsRepository,
                          UserRepository userRepository,
                          UsersChatsRepository usersChatsRepository,
                          PhotoSearch photoSearch, VkBot vkBot, Translator translator) {
        this.chatsRepository = chatsRepository;
        this.userRepository = userRepository;
        this.usersChatsRepository = usersChatsRepository;
        this.photoSearch = photoSearch;
        this.vkBot = vkBot;
        this.translator = translator;
    }

    @GetMapping("test")
    public ResponseEntity<String> test() throws Exception {
        return ResponseEntity.ok(translator.translateJson("{ \n" +
                "  \"first_name\":\"Антон\", \n" +
                "  \"last_name\":\"Сердюков\", \n" +
                "  \"born_date\":\"19.04.96\", \n" +
                "  \"city\":\"Астрахань\", \n" +
                "  \"country\":\"Россия\", \n" +
                "  \"groups\":[ \n" +
                "   \"VK Hackathon'19 | ON BOARD\", \n" +
                "   \"VK Hackathon\", \n" +
                "   \"FIX\", \n" +
                "   \"Some english word FABRIKA | Бизнес-инкубатор LIFT\", \n" +
                "   \"Habr\", \n" +
                "   \"ITc | сообщество программистов\", \n" +
                "   \"Типичный программист\", \n" +
                "   \"Я - SAM (типа мои инициалы)\", \n" +
                "   \"Naked Science\", \n" +
                "   \"VK Tech\", \n" +
                "   \"Библиотека программиста\", \n" +
                "   \"Starset\", \n" +
                "   \"TestGroup\", \n" +
                "   \"Программирование ITmozg:\", \n" +
                "   \"Что почитать? Лучшие книги каждый день.\", \n" +
                "   \"Tomorrowland\" \n" +
                "  ] \n" +
                " }"));
    }

    @GetMapping("testDriver")
    public ResponseEntity<String> testDriver(){
        String page = photoSearch.findImageByName("test");


        return ResponseEntity.ok(page);
    }


}
