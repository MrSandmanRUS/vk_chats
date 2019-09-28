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
import com.vk.api.sdk.objects.base.UploadServer;
import com.vk.api.sdk.objects.groups.Group;
import com.vk.api.sdk.objects.wall.WallpostFull;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VkBot {

    private final Messages messages;
    private final UserActor actor;
    private final ChatsRepository chatsRepository;
    private final VkApiClient vkApiClient;
    private final WebDriver driver;

    @Autowired
    public VkBot(@Value("${config.app_id}") Integer appId,
                 @Value("${config.client_secret}") String clientSecret,
                 @Value("${config.vk.bot.login}") String login,
                 @Value("${config.vk.bot.password}") String password,
                 ChatsWebDriverFactory webDriverFactory,
                 ChatsRepository chatsRepository) throws ClientException, ApiException, InterruptedException {

        driver = webDriverFactory.create().get();
        driver.get("https://www.vk.com");
        driver.findElement(By.id("index_email")).sendKeys("1");
        driver.findElement(By.id("index_pass")).sendKeys("1");
        driver.findElement(By.id("index_login_button")).click();
        Thread.sleep(1000);
        driver.navigate().to("https://www.vk.com/im");


        this.chatsRepository = chatsRepository;
        WebDriver webDriver = webDriverFactory.create().get();
        webDriver.get("https://oauth.vk.com/authorize?client_id="
                + appId
                + "&display=page&" +
                "redirect_uri=https://oauth.vk.com/authorize&" +
                "scope=messages,friends,groups,wall,offline&" +
                "response_type=code&" +
                "v=5.101");
        webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/form/div/div/input[6]")).sendKeys(login);
        Thread.sleep(1000);
        webDriver.findElement(By.name("pass")).sendKeys(password);
        Thread.sleep(1000);
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

    public void getChatLink(Chat chat) throws Exception {
        String interest = chat.getInterest();
        //int chatId = messages.createChat(actor).title(interest).execute();
        int chatId = createChatBySelenium(chat);
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 1000 * 60 * 40)
    public void doPostsInChats() throws Exception {
        List<Chat> chats = chatsRepository.findAll();
        for (Chat chat : chats) {
            try {
                String interest = chat.getInterest();
                List<Group> groups = vkApiClient.groups().search(actor, interest).execute().getItems();
                WallpostFull wallpostFull = findPostInGroups(groups);
                String postId = "wall" + wallpostFull.getOwnerId() + "_" + wallpostFull.getId();
                vkApiClient.messages().send(actor).chatId(chat.getChatVkId()).randomId(RandomUtils.nextInt()).attachment(postId).execute();
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
    }

    private WallpostFull findPostInGroups(List<Group> groups) throws Exception {
        try {
            Group group = groups.get(RandomUtils.nextInt(0, groups.size() - 1));
            Integer id = group.getId();
            Thread.sleep(1000);
            List<WallpostFull> wallpostFullList = vkApiClient.wall().get(actor).ownerId(-id).count(10).execute().getItems();
            Thread.sleep(1000);
            WallpostFull wallpostFull = wallpostFullList.get(RandomUtils.nextInt(0, wallpostFullList.size() - 1));
            return wallpostFull;
        } catch (Exception e) {
            return findPostInGroups(groups);
        }
    }

    //returns link of generated chat
    public int createChatBySelenium(Chat chat) throws Exception {
        int chatId = 0;
        synchronized (driver) {
            try {
                Thread.sleep(500L);
                driver.findElement(By.xpath("/html/body/div[11]/div/div/div[2]/div[2]/div[2]/div/div/div/div/div[1]/div[2]/div[1]/div/div[1]/div[2]/div/button")).click();

                WebElement creationName = driver.findElement(By.id("im_dialogs_creation_name"));
                String interest = chat.getInterest();
                creationName.sendKeys(interest);
                driver.findElement(By.xpath("/html/body/div[11]/div/div/div[2]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div/div[4]/div/button")).click();
                Thread.sleep(500L);

                //chat created
                chatId = Integer.parseInt(driver.getCurrentUrl().split("=c")[1]);
                chat.setChatVkId(chatId);
                driver.findElement(By.xpath("/html/body/div[11]/div/div/div[2]/div[2]/div[2]/div/div/div/div/div[1]/div[3]/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/a")).click();
                Thread.sleep(500L);

                driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/section/div/div[2]/ul[2]/li[1]")).click();
                Thread.sleep(500L);

                String linkToChat = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/section/div/div/input")).getAttribute("value");
                chat.setLink(linkToChat);

                Thread.sleep(500L);
            } catch (Exception e) {

            }
            driver.navigate().to("https://vk.com/im");
        }
        return chatId;
    }

}
