package com.shematch_team.chats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @JsonProperty("vk_id")
    private String vkId;

    @JsonProperty("vk_token")
    private String vkToken;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("info")
    private JSONObject info;

}
