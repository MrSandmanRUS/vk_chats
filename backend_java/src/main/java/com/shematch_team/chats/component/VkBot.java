package com.shematch_team.chats.component;

import com.shematch_team.chats.entity.Chat;
import com.vk.api.sdk.actions.Messages;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VkBot {

    private final Messages messages;
    private final UserActor actor;

    @Autowired
    public VkBot(@Value("${config.app_id}") Integer appId,
            @Value("${config.client_secret}") String clientSecret,
            ChatsWebDriverFactory webDriverFactory) throws ClientException, ApiException {
        WebDriver webDriver = webDriverFactory.create().get();
        webDriver.get("https://oauth.vk.com/authorize?client_id="
                +appId
                +"&display=page&" +
                "redirect_uri=https://oauth.vk.com/authorize&" +
                "scope=messages,offline&" +
                "response_type=code&" +
                "v=5.101");
        webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/form/div/div/input[6]")).sendKeys("some");
        webDriver.findElement(By.xpath("/html/body/div/div/div/div[2]/form/div/div/input[7]")).sendKeys("some");
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
    }

    public void createChat(Chat chat) throws Exception {
        String interest = chat.getInterest();
        int chatId = messages.createChat(actor).title(interest).execute();
        String link = messages.getInviteLink(actor, 2000000000 + chatId).execute().getLink();
        chat.setLink(link);
    }
}
