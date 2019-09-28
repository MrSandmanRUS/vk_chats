package com.shematch_team.chats.service;

import com.shematch_team.chats.entity.Translate;
import com.shematch_team.chats.repository.TranslatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;

@Service
public class TranslateService {

    @Autowired
    private TranslatesRepository translatesRepository;

    public Optional<String> getByEnglish(String english) {
        Optional<Translate> translateOptional = translatesRepository.findByEnglish(english);
        if (translateOptional.isPresent()) {
            Translate translate = translateOptional.get();
            translate.setUsageCount(translate.getUsageCount().add(BigInteger.ONE));
            translatesRepository.save(translate);
            String russian = translate.getRussian();
            return Optional.of(russian);
        } else {
            return Optional.empty();
        }
    }

    public void save(String russianWord, String englishWord) {
        Translate translate = new Translate();
        translate.setUsageCount(BigInteger.ZERO);
        translate.setCreatedWhen(new Date());
        try {
            translate.setEnglish(new String(englishWord.getBytes("UTF-8")));
            translate.setRussian(new String(russianWord.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {

        }
        translatesRepository.save(translate);
    }
}
