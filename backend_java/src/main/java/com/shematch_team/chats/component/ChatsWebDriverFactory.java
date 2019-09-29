package com.shematch_team.chats.component;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatsWebDriverFactory {

    @Autowired
    private ObjectFactory<ChatsWebDriver> prototypeBeanObjectFactory;

    public ChatsWebDriver create() {
        return prototypeBeanObjectFactory.getObject();
    }

}
