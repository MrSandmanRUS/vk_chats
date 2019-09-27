package com.shematch_team.chats.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class PhotoSearch {
    public String findImageByName(String imgName) {
        String exePath = "chromedriver_linux";

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("window-size=1200x600");


        System.setProperty("webdriver.chrome.driver", exePath);
        WebDriver driver = new ChromeDriver(options);

        String url = "https://go.mail.ru/search_images?q=" + imgName + "&fm=1#urlhash=0";
        driver.get(url);
        String link = driver.getPageSource();
        link = link.substring(link.indexOf("\"imUrl\": \"") + 10);
        link = link.substring(0, link.indexOf("\""));

        return link;
    }
}


