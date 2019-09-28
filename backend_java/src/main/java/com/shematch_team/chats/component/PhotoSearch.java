package com.shematch_team.chats.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PhotoSearch {

    @Autowired
    ChatsWebDriver chatsWebDriver;

    public String findImageByName(String imgName) {

        WebDriver driver = chatsWebDriver.get();

        String url = "https://go.mail.ru/search_images?q=" + imgName + "&fm=1#urlhash=0";
        driver.get(url);
        String link = driver.getPageSource();

        while (true) {
            String tempLink = link.substring(link.indexOf("\"imUrl\": \"") + 10);
            String res_link = tempLink.substring(0, tempLink.indexOf("\""));
            try {
                if (res_link.length() < 1) {
                    break;
                }
                String protocol = res_link.substring(0, 5);
                if (protocol.equals("https") && !res_link.contains("fotocdn")) {
                    link = res_link;
                    break;
                } else {
                    link = tempLink;
                }
            } catch (Exception exc)  {
                break;
            }
        }

        return link;
    }
}


