package com.shematch_team.chats.component;

import com.shematch_team.chats.dto.TranslateResponseDto;
import com.shematch_team.chats.repository.TranslatesRepository;
import com.shematch_team.chats.service.TranslateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Component
public class Translator {

    @Value("${yandex.api.key}")
    private String YAPI_KEY;

    private final String YAPI_RESOURCE = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    @Autowired
    private TranslatesRepository translatesRepository;

    @Autowired
    private TranslateService translateService;

    public Optional<String> translateToRussian(String englishWord) {
        //todo safe synchronization on english word string
        Optional<String> russianWordFromDb = translateService.getByEnglish(englishWord);
        if (russianWordFromDb.isPresent()) {
            return russianWordFromDb;
        } else {
            Optional<String> russianWord;
            russianWord = getFromYandexService(englishWord);
            return russianWord;
        }
    }


    private Optional<String> getFromYandexService(String englishWord) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(YAPI_RESOURCE)
                .queryParam("key", YAPI_KEY)
                .queryParam("text", englishWord)
                .queryParam("lang", "en-ru")
                .queryParam("format", "plain");

        ResponseEntity<TranslateResponseDto> response = restTemplate.getForEntity(builder.toUriString(),
                TranslateResponseDto.class);
        List<String> translates = response.getBody().getText();
        return response.getStatusCode().equals(HttpStatus.OK) && !CollectionUtils.isEmpty(translates) ?
                Optional.of(translates.get(0)) : Optional.empty();
    }

}
