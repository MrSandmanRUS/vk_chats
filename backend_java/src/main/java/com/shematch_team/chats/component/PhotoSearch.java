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

        String url = "https://mail.ru";
        driver.get(url);
        String page = driver.getPageSource();

        return page;
    }
//
//    public static void main(String args[]) {
//        PhotoSearch photoSearch = new PhotoSearch();
//        String test = photoSearch.findImageByName("test");
//
//        System.out.println(test);
//    }
}


