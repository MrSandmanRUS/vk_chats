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
        link = link.substring(link.indexOf("\"imUrl\": \"") + 10);
        link = link.substring(0, link.indexOf("\""));

        return link;
    }
}


