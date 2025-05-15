package com.kedu.home.utils;

import java.util.Arrays;
import java.util.List;

public class AbuseFilterUtils {

    private static final List<String> BAD_WORDS = Arrays.asList(
        "병신", "븅신", "비융신", "병1신", "병ㅣ신", "ㅂㅅ", "ㅄ",
        "시발", "쉬발", "씨발", "ㅅㅂ", "ㅆㅂ", "십창", "시팍",
        "개새끼", "개쉑", "개쉐", "개쌔끼", "개쉨", "개쉨키", "개색기",
        "좆", "ㅈㄹ", "ㅈ같", "존나", "ㅈㄴ"
    );

    // ✅ 단어가 단독으로만 있을 경우 욕설 판단
    public static boolean isAbusiveOnly(String input) {
        if (input == null || input.trim().isEmpty()) return false;

        // 전처리: 특수문자 제거, 숫자 → 문자 치환 등
        String cleaned = input
            .replaceAll("[^\\p{L}\\p{N}\\s]", "") // 문자, 숫자, 공백만 남김
            .replaceAll("1", "ㅣ")
            .replaceAll("!", "ㅣ")
            .toLowerCase()
            .trim();

        // 단어 단위 분리
        String[] words = cleaned.split("\\s+"); // 공백 기준 분리

        // 욕설만 입력되었는지 판단
        return Arrays.stream(words)
                .allMatch(word -> BAD_WORDS.contains(word));
    }

    // ✅ 욕설이 포함되어 있는지만 판단 (입력에 섞여 있음)
    public static boolean containsAbuse(String input) {
        if (input == null) return false;

        String normalized = input
            .replaceAll("[^\\p{L}\\p{N}]", "")
            .replaceAll("1", "ㅣ")
            .replaceAll("!", "ㅣ")
            .toLowerCase();

        return BAD_WORDS.stream().anyMatch(normalized::contains);
    }
}
