package com.trkgrn.springbackend.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StringUtil {

    public List<String> splitSentences(String docs) {
        List<String> sentences = new ArrayList<>();
        Pattern pattern = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)");

        Matcher matcher = pattern.matcher(docs);
        while (matcher.find()) {
            String sentence = matcher.group().trim();
            sentences.add(sentence);
        }

        return sentences;
    }
}
