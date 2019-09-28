package com.shematch_team.chats.component;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Translator {

    @Value("${yandex.api.key}")
    private String YAPI_KEY;

    private final String YAPI_RESOURCE = "https://translate.yandex.net/api/v1.5/tr.json/translate";

    @Autowired
    private TranslatesRepository translatesRepository;

    @Autowired
    private TranslateService translateService;

    private Pattern pattern = Pattern.compile("[a-zA-Z_]+");

    private List<String> stopWords = Lists.newArrayList(
            "first_name","last_name","city","groups","country","born_date"
    );

    public String translateJson(String json) throws UnsupportedEncodingException {
        Matcher matcher = pattern.matcher(json);
        List<String> englishWords = new ArrayList<>();
        while (matcher.find()) {
            String englishWord = matcher.group();
            if (!stopWords.contains(englishWord)) {
                englishWords.add(englishWord);
            }
        }
        String englishWordsToTranslate = Joiner.on("-").join(englishWords);
        String translation = getFromYandexService(englishWordsToTranslate).get();
        List<String> translated = Arrays.asList(translation.split("-"));

        Matcher matcherSecond = pattern.matcher(json);
        StringBuffer bufferSecond = new StringBuffer();
        ListIterator<String> translationIterator = translated.listIterator();
        while (matcherSecond.find()) {
            String translatedWord = translationIterator.next();
            String group = matcherSecond.group();
            if (!stopWords.contains(group)) {
                matcherSecond.appendReplacement(bufferSecond, translatedWord);
            } else {
                translationIterator.previous();
                matcherSecond.appendReplacement(bufferSecond, group);
            }
        }
        return bufferSecond.toString();
    }

    public Optional<String> translateToRussian(String englishWord) throws UnsupportedEncodingException {
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


    private Optional<String> getFromYandexService(String englishWord) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(YAPI_RESOURCE)
                .queryParam("key", YAPI_KEY)
                .queryParam("text", URLEncoder.encode(englishWord, "UTF-8"))
                .queryParam("lang", "en-ru")
                .queryParam("format", "plain");

        ResponseEntity<TranslateResponseDto> response = restTemplate.getForEntity(builder.toUriString(),
                TranslateResponseDto.class);
        List<String> translates = response.getBody().getText();
        return response.getStatusCode().equals(HttpStatus.OK) && !CollectionUtils.isEmpty(translates) ?
                Optional.of(translates.get(0)) : Optional.empty();
    }

}
