package com.shematch_team.chats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shematch_team.chats.dto.UserRequestDto;
import com.shematch_team.chats.entity.User;
import com.shematch_team.chats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final ObjectMapper om = new ObjectMapper();
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserRequestDto userRequestDto) throws JsonProcessingException {
        User user = new User();
        Map<String, Object> info = userRequestDto.getInfo();
        user.setInfo(om.writeValueAsString(info));
        user.setVkId(userRequestDto.getVkId());
        userRepository.save(user);
    }
}
