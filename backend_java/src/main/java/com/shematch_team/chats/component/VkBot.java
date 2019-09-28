package com.shematch_team.chats.component;

import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.repository.ChatsRepository;
import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.groups.Group;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VkBot {

    private final Messages messages;
    private final UserActor actor;
    private final ChatsRepository chatsRepository;
    private final VkApiClient vkApiClient;

    @Autowired
    public VkBot(@Value("${config.app_id}") Integer appId,
                 @Value("${config.client_secret}") String clientSecret,
                 @Value("${config.vk.bot.login}") String login,
                 @Value("${config.vk.bot.password}") String password,
                 ChatsWebDriverFactory webDriverFactory,
                 ChatsRepository chatsRepository) throws ClientException, ApiException {
        this.chatsRepository = chatsRepository;
        WebDriver webDriver = webDriverFactory.create().get();
        webDriver.get("https://oauth.vk.com/authorize?client_id="
                +appId
                +"&display=page&" +
                "redirect_uri=https://oauth.vk.com/authorize&" +
                "scope=messages,friends,groups,wall,offline&" +
                "response_type=code&" +
                "v=5.101");
        webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/form/div/div/input[6]")).sendKeys("1");
        webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/form/div/div/input[7]")).sendKeys("1");
        webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/form/div/div/button")).click();
        try {
            WebElement element = webDriver.findElement(By.xpath("/html/body/div/div/div/div[3]/div/div[1]/button[1]"));
            if (element != null) {
                element.click();
            }
        } catch (Exception e) {
            //похер го дальше код выполнять
        }
        String code = webDriver.getCurrentUrl().split("=")[1];
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        UserAuthResponse authResponse = vk.oAuth()
                .userAuthorizationCodeFlow(appId, clientSecret, "https://oauth.vk.com/authorize", code)
                .execute();
        actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
        messages = vk.messages();
        this.vkApiClient = vk;
    }

    public String getChatLink(Chat chat) throws Exception {
        String interest = chat.getInterest();
        int chatId = messages.createChat(actor).title(interest).execute();
        chat.setChatVkId(chatId);
        String link = messages.getInviteLink(actor, 2000000000 + chatId).execute().getLink();
        return link;
    }

    //executes one time in 40 minutes
    //@Scheduled(fixedDelay = 1000 * 60 * 40)
    public void doPostsInChats() throws Exception {
        List<Chat> chats = chatsRepository.findAll();
        for (Chat chat : chats) {
            String interest = chat.getInterest();
            List<Group> groups = vkApiClient.groups().search(actor,interest).execute().getItems();
            Group group = groups.get(RandomUtils.nextInt(0, groups.size() - 1));
            Integer id = group.getId();
            Thread.sleep(500);
            List<WallpostFull> wallpostFullList = vkApiClient.wall().get(actor).ownerId(-id).count(10).execute().getItems();
            Thread.sleep(500);
            WallpostFull wallpostFull = wallpostFullList.get(RandomUtils.nextInt(0, wallpostFullList.size() - 1));
            String postId = "wall" + wallpostFull.getOwnerId() + "_" + wallpostFull.getId();
            vkApiClient.messages().send(actor).chatId(chat.getChatVkId()).attachment(postId);
            Thread.sleep(500);
        }
    }
}
