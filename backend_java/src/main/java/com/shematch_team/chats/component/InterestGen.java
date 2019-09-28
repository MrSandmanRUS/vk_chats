package com.shematch_team.chats.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shematch_team.chats.dto.UserRequestDto;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Component
public class InterestGen {
    private final ObjectMapper om = new ObjectMapper();

    public List<String> getInterestsFromPython(UserRequestDto userRequestDto) throws IOException {
        String info = om.writeValueAsString(userRequestDto.getInfo());
        String query = "http://127.0.0.1:81/getInterest";
        URL url = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        OutputStream os = conn.getOutputStream();
        os.write(info.getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        in.close();
        conn.disconnect();
        return om.readValue(result, List.class);
    }
}
