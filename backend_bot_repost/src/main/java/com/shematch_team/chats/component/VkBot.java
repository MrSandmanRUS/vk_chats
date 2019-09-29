package com.shematch_team.chats.component;

import com.shematch_team.chats.entity.Chat;
import com.shematch_team.chats.repository.ChatsRepository;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class VkBot {

    private final ChatsRepository chatsRepository;
    private final WebDriver repostDriver;
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


        repostDriver = webDriverFactory.create().get();
        repostDriver.get("https://www.vk.com");
        repostDriver.findElement(By.id("index_email")).sendKeys(login);
        repostDriver.findElement(By.id("index_pass")).sendKeys(password);
        repostDriver.findElement(By.id("index_login_button")).click();
        Thread.sleep(1000);
        repostDriver.navigate().to("https://www.vk.com/im");
        this.chatsRepository = chatsRepository;
    }

    @PostConstruct
    public void doPostsInChats() throws Exception {
        while (true) {
            List<Chat> chats = chatsRepository.findAll();
            for (Chat chat : chats) {
                if (chat.getLink() == null) {
                    continue;
                }
                System.out.println("Репостну-ка я в чат " + chat.getInterest());
                try {
                    doPostingWithSelenium(chat);
                } catch (Exception e) {

                }
            }
            Thread.sleep(60000L);
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
        int tryesCount = 0;
        while (tryesCount < 10) {
            try {
                repostDriver.get("https://vk.com/search?c%5Bper_page%5D=40&c%5Bq%5D=" + interest + "&c%5Bsection%5D=communities");
                Thread.sleep(2000L);
                List<WebElement> elements = repostDriver.findElements(By.xpath("//*[@id=\"results\"]/div"));
                int size = elements.size();
                random = RandomUtils.nextInt(0, size - 1);
                elements.get(random).findElement(By.tagName("a")).click();

                Thread.sleep(2000L);

                List<WebElement> posts = repostDriver.findElements(By.xpath("//*[starts-with(@id,\"post\")]"));
                size = posts.size();
                random = RandomUtils.nextInt(0, size - 1);
                share = posts.get(random).findElement(By.className("share"));
                if (share != null) {
                    break;
                }
            } catch (Exception e) {

            }
            tryesCount++;
        }

        if (share == null) {
            return;
        }
        share.click();
        try {
            Thread.sleep(1000L);
            repostDriver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[3]/div[2]/div/input")).sendKeys(interest);
            Thread.sleep(1000L);
            repostDriver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[2]/div[3]/div[2]/div/input")).sendKeys(Keys.ENTER);
            Thread.sleep(1000L);

            random = RandomUtils.nextInt(0, repostingComments.size() - 1);
            repostDriver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[4]/div[2]")).sendKeys(repostingComments.get(random));
            Thread.sleep(1000L);
            repostDriver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[2]/div[1]/div[7]/button")).click();
            Thread.sleep(1000L);
        } catch (Exception e){}

    }

    private void joinIfNecessary(String link) {
        try {
            repostDriver.get(link);
            Thread.sleep(1000L);
            repostDriver.findElement(By.xpath("/html/body/div[6]/div/div[2]/div/div[2]/div/div[5]/button")).click();
            Thread.sleep(1000L);
        } catch (Exception e) {
        }
        repostDriver.navigate().to("https://vk.com/feed");
    }

}
