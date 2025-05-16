package com.kedu.home.utils;

public class JsonCleanUtils {
//	  /**
//     * JSON 문자열에서 //, /* */ 형태의 주석을 제거합니다.
//     *
//     * @param input LLM 응답 JSON 문자열
//     * @return 주석 제거된 JSON 문자열
//     */
    public static String removeJsonComments(String input) {
        if (input == null) return null;

        return input
                .replaceAll("(?s)/\\*.*?\\*/", "")      // /* 주석 */ 제거
                .replaceAll("(?m)^\\s*//.*?$", "")      // // 주석 한 줄 제거
                .replaceAll("//.*", "");                // 라인 내 주석 제거
    }
}
