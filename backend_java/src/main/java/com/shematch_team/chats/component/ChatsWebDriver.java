package com.shematch_team.chats.component;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
@Scope("prototype")
public class ChatsWebDriver {

    private final WebDriver driver;

    @Autowired
    public ChatsWebDriver(@Value("${config.selenium_path}") String seleniumPath) {
        System.setProperty("webdriver.chrome.driver", seleniumPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1200x600");
        driver = new ChromeDriver(options);
    }

    @PreDestroy
    private void destroy(){
        driver.close();
    }

    public WebDriver get() {
        return driver;
    }
}
