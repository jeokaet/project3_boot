package com.kedu.home.utils;

import java.util.Arrays;
import java.util.List;

public class AbuseFilterUtils {

    private static final List<String> BAD_WORDS = Arrays.asList(
        "병신", "ㅄ", "시발", "씨발", "ㅅㅂ", "개쌔끼", "ㅂㅅ", "개새끼"
    );

    public static boolean isAbusiveOnly(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        String clean = input
            .replaceAll("[^\\p{IsHangul}\\p{IsAlphabetic}\\s]", "") // 특수문자 제거
            .replaceAll("\\s+", " ")                                 // 공백 정리
            .trim()
            .toLowerCase();

        String[] words = clean.split(" ");
        long validWordCount = Arrays.stream(words)
            .filter(w -> !BAD_WORDS.contains(w))
            .count();

        return validWordCount == 0;
    }
}
