package com.kedu.home.utils;

import java.util.List;
import com.kedu.home.dto.PlaceDTO;

public class PromptBuilder {

    public static String buildPrompt(String userInput, List<PlaceDTO> places) {
        StringBuilder sb = new StringBuilder();

        sb.append("당신은 여행지 추천 시스템의 어시스턴트입니다.\n\n");

        sb.append("사용자의 입력은 오타, 축약어, 비속어가 포함될 수 있습니다.\n");
        sb.append("입력을 문맥으로 분석하여 정제된 의미로 해석하고, 이에 적합한 장소를 추천하세요.\n\n");

        sb.append("너의 역할은 제공된 후보 리스트에서 조건에 맞는 장소를 필터링하여 추천하는 것입니다.\n");
        sb.append("후보 리스트에 없는 장소는 절대 포함하지 마세요. 새로운 장소를 생성하지 마세요.\n\n");

        sb.append("📌 지역 조건 처리 지침:\n");
        sb.append("1. 사용자가 한 개의 지역을 입력하면 해당 지역 내 장소만 추천하세요.\n");
        sb.append("2. 두 개 이상의 지역이 입력되면:\n");
        sb.append("   - 후보 리스트에서 모두 찾을 수 있다면, 두 지역 모두 포함해서 추천하세요.\n");
        sb.append("   - 둘 다 만족하기 어렵다면, 문맥상 우선순위가 높은 지역만 골라 추천하세요.\n");
        sb.append("   - 그래도 결과가 충분하지 않으면, 아래 형식으로 에러 응답하세요:\n");
        sb.append("     { \"error\": \"추천할 만한 장소가 없습니다. 다시 입력해주세요.\" }\n\n");

        sb.append("📌 응답 형식은 반드시 아래 JSON만 사용하세요. 마크다운, 코드블럭, 설명 문장 포함 금지:\n\n");

        sb.append("{\n");
        sb.append("  \"results\": [\n");
        sb.append("    {\n");
        sb.append("      \"name\": \"장소명\",\n");
        sb.append("      \"type\": \"장소 유형\",\n");
        sb.append("      \"region\": \"지역명\",\n");
        sb.append("      \"description\": \"장소 설명 (1문장)\",\n");
        sb.append("      \"reason\": \"추천 이유\",\n");
        sb.append("      \"imageUrl\": \"이미지파일경로\"\n");
        sb.append("    }\n");
        sb.append("  ]\n");
        sb.append("}\n\n");

        sb.append("📌 예외 상황일 경우 반드시 다음 형식으로 응답:\n");
        sb.append("{ \"error\": \"추천할 만한 장소가 없습니다. 다시 입력해주세요.\" }\n\n");

        sb.append("📌 기타 조건:\n");
        sb.append("1. 장소 수는 최소 3개, 최대 5개.\n");
        sb.append("2. 장소는 반드시 후보 리스트에서만 선택하세요.\n");
        sb.append("3. description과 reason은 서로 다른 내용을 담도록 하세요.\n");
        sb.append("4. JSON 외 텍스트, 설명, 마크다운, 코드블럭 등은 절대 포함하지 마세요.\n");
        sb.append("5. 특히 아래 단어들은 description 또는 reason에 절대 포함하지 마세요:\n");
        sb.append("   - 기본, 대체, 잘못된 입력, 임의, 예시, 추천이 부족하여\n");
        sb.append("6. 반드시 의미 있는 추천이 가능한 경우에만 results를 응답하세요.\n");

        sb.append("\n🧾 사용자 입력:\n");
        sb.append("\"").append(userInput).append("\"\n\n");

        sb.append("🗂️ 후보 장소 리스트:\n");
        for (int i = 0; i < places.size(); i++) {
            PlaceDTO p = places.get(i);
            sb.append(String.format("%d. %s (%s, %s) - %s / %s\n",
                    i + 1, p.getName(), p.getType(), p.getRegion(), p.getDescription(), p.getReason(), p.getImageUrl()));
        }

        return sb.toString();
    }
    
    public static String buildPrompt2(String date, String startingLocation ) {
    	StringBuilder sb = new StringBuilder();

        sb.append("당신은 특정 가게 및 주소가 해당하는 지역의 관광지 및 볼거리 및 인기있는 식당 추천 시스템의 어시스턴트입니다.\n\n");
        
        sb.append("너의 역할은 건네받은 주소와 날짜를 기반으로 가볼만한 장소 및 식당을 추천하는 것입니다.\n");
        sb.append("실재하지 않는 장소는 절대 포함하지 마세요. 새로운 장소를 생성하여 추천하지 마세요. 넘겨받은 주소로부터 반경 10km 이내에 위치한 가게 및 식당 및 볼거리를 추천하세요. \n\n");
        sb.append("📌 요청된 주소: ").append(startingLocation).append("\n");
        sb.append("📌 요청된 날짜: ").append(date).append("\n\n");
        
        
        sb.append("📌 응답 형식은 반드시 아래 JSON만 사용하세요. 마크다운, 코드블럭, 설명 문장 포함 금지:\n\n");

        sb.append("{\n");
        sb.append("  \"results\": [\n");
        sb.append("    {\n");
        sb.append("      \"name\": \"장소명\",\n");
        sb.append("      \"type\": \"장소 유형\",\n");
        sb.append("      \"region\": \"지역명\",\n");
        sb.append("      \"description\": \"장소 설명 (1문장)\",\n");
        sb.append("      \"reason\": \"추천 이유\",\n");
        sb.append("      \"latitude\": \"위도\",\n");
        sb.append("      \"longitude\": \"경도\",\n");
        sb.append("      \"imageUrl\": \"null\"\n");
        sb.append("    }\n");
        sb.append("  ]\n");
        sb.append("}\n\n");

        sb.append("📌 예외 상황일 경우 반드시 다음 형식으로 응답:\n");
        sb.append("{ \"error\": \"해당 지역에 대한 추천장소를 불러오지 못했습니다.출발지를 확인해주세요.\" }\n\n");
        
        sb.append("📌 기타 조건:\n");
        sb.append("1. 장소 수는 최소 50개. 최대 1500개. \n");
        sb.append("2. 장소는 반드시 실재하는 곳만 선택하세요.\n");
        sb.append("3. description과 reason은 서로 다른 내용을 담도록 하세요.\n");
        sb.append("4. JSON 외 텍스트, 설명, 마크다운, 코드블럭 등은 절대 포함하지 마세요.\n");
        sb.append("5. 특히 아래 단어들은 description 또는 reason에 절대 포함하지 마세요:\n");
        sb.append("   - 기본, 대체, 잘못된 입력, 임의, 예시, 추천이 부족하여\n");
        sb.append("6. 반드시 넘겨받은 주소에서 반경 10km에 있거나 넘겨받은 주소가 속한  가능한 경우에만 results를 응답하세요.\n");


        return sb.toString();
    }
}
