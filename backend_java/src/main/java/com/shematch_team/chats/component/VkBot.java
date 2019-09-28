package com.shematch_team.chats.component;

import com.google.common.collect.Sets;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class VkBot {

    private final Messages messages;
    private final UserActor actor;
    private final ChatsRepository chatsRepository;
    private final VkApiClient vkApiClient;
    private final WebDriver driver;
    private final static Object monitor = new Object();

    private List<String> repostingComments = Arrays.asList(
            "Какой интересный пост!",
            "А что вы думаете про это?",
            "Следите за новостями",
            "И на рубрике дня у нас",
            "Немножечко постов по теме беседы",
            "Очередной репост",
            "Такие дела",
            "Репостну-ка я вам это",
            "Посмотрите на этот пост",
            "Предлагаю обсудить этот пост",
            "Запощу очередной пост",
            "Я бот, хочу репощу",
            "Всем привет я бот, и я люблю репостить посты",
            "Пост! ",
            "Репоооост!",
            "Новый репост поста по текущей тематике",
            "Обсудим тему нашего чата",
            "Вот такой пост я подобрал на нашу тему"
    );

    @Autowired
    public VkBot(@Value("${config.app_id}") Integer appId,
                 @Value("${config.client_secret}") String clientSecret,
                 @Value("${config.vk.bot.login}") String login,
                 @Value("${config.vk.bot.password}") String password,
                 ChatsWebDriverFactory webDriverFactory,
                 ChatsRepository chatsRepository) throws ClientException, ApiException, InterruptedException {

        driver = webDriverFactory.create().get();
        driver.get("https://www.vk.com");
        driver.findElement(By.id("index_email")).sendKeys("krushon96@mail.ru");
        driver.findElement(By.id("index_pass")).sendKeys("GGoWork17");
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

    @PostConstruct
    public void doPostsInChats() throws Exception {
        while (true) {
            List<Chat> chats = chatsRepository.findAll();
            for (Chat chat : chats) {
                System.out.println("Репостну-ка я в чат " + chat.getInterest());
                try {
                    //doVkApiPosting(chat);
                    doPostingWithSelenium(chat);
                } catch (Exception e) {

                }
            }
            Thread.sleep(180000L);
        }
    }

    private void doPostingWithSelenium(Chat chat) throws InterruptedException {
        String interest = chat.getInterest();
        String link = chat.getLink();
        joinIfNecessary(link);
        findPostAndRepost(interest, chat);
    }

    private void findPostAndRepost(String interest, Chat chat) throws InterruptedException {
        WebElement share = null;
        int random;
        while (share == null) {
            driver.get("https://vk.com/search?c%5Bper_page%5D=40&c%5Bq%5D=" + interest + "&c%5Bsection%5D=communities");
            Thread.sleep(2000L);
            List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"results\"]/div"));
            int size = elements.size();
            random = RandomUtils.nextInt(0, size - 1);
            elements.get(random).findElement(By.tagName("a")).click();

            Thread.sleep(2000L);

            List<WebElement> posts = driver.findElements(By.xpath("//*[starts-with(@id,\"post\")]"));
            size = posts.size();
            random = RandomUtils.nextInt(0, size - 1);
            share = posts.get(random).findElement(By.className("share"));
        }
        share.click();
        Thread.sleep(1000L);
        driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[3]/div[2]/div/input")).sendKeys(interest);
        Thread.sleep(1000L);
        driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[3]/div[2]/div/input")).sendKeys(Keys.ENTER);
        Thread.sleep(1000L);

        random = RandomUtils.nextInt(0, repostingComments.size() - 1);
        driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[4]/div[2]")).sendKeys(repostingComments.get(random));
        Thread.sleep(1000L);
        driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[7]/button")).click();
        Thread.sleep(1000L);

    }

    private void joinIfNecessary(String link) {
        try {
            driver.get(link);
            Thread.sleep(1000L);
            driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[5]/button")).click();
            Thread.sleep(1000L);
        } catch (Exception e) {
        }
        driver.navigate().to("https://vk.com/feed");
    }

    private void doVkApiPosting(Chat chat) throws Exception {

        String interest = chat.getInterest();
        List<Group> groups = vkApiClient.groups().search(actor, interest).execute().getItems();
        WallpostFull wallpostFull = findPostInGroups(groups);
        String postId = "wall" + wallpostFull.getOwnerId() + "_" + wallpostFull.getId();
        Thread.sleep(2000L);
        try {
            vkApiClient.messages().joinChatByInviteLink(actor, chat.getLink()).execute();
        } catch (Exception e) {
        }
        Thread.sleep(2000L);
        String repostingComent = repostingComments.get(RandomUtils.nextInt(0, repostingComments.size() - 1));
        vkApiClient.messages().send(actor).chatId(chat.getChatVkId()).randomId(RandomUtils.nextInt())
                .message(repostingComent).attachment(postId).execute();
        Thread.sleep(2000);
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
                Thread.sleep(1500L);
                driver.findElement(By.xpath("/html/body/div[11]/div/div/div[2]/div[2]/div[2]/div/div/div/div/div[1]/div[2]/div[1]/div/div[1]/div[2]/div/button")).click();

                WebElement creationName = driver.findElement(By.id("im_dialogs_creation_name"));
                String interest = chat.getInterest();
                creationName.sendKeys(interest);
                driver.findElement(By.xpath("/html/body/div[11]/div/div/div[2]/div[2]/div[2]/div/div/div/div/div[1]/div[1]/div/div[4]/div/button")).click();
                Thread.sleep(1500L);

                //chat created
                chatId = Integer.parseInt(driver.getCurrentUrl().split("=c")[1]);
                chat.setChatVkId(chatId);
                driver.findElement(By.xpath("/html/body/div[11]/div/div/div[2]/div[2]/div[2]/div/div/div/div/div[1]/div[3]/div[2]/div[1]/div[1]/div[2]/div[2]/div[2]/a")).click();
                Thread.sleep(1500L);

                driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/section/div/div[2]/ul[2]/li[1]")).click();
                Thread.sleep(1500L);

                String linkToChat = driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/section/div/div/input")).getAttribute("value");
                chat.setLink(linkToChat);

                Thread.sleep(1500L);
                //назад
                // driver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/section/header/div[1]/button")).click();

                Thread.sleep(1500L);
                driver.navigate().to("https://vk.com/im");
            } catch (Exception e) {

            }
        }
        return chatId;
    }

}
