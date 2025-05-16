package com.kedu.home.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedu.home.dto.PlaceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class OptimalRouteService {

    // Haversine 거리 계산 메서드
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // 최적 동선 계산
    public List<PlaceDTO> getOptimizedRoute(Map<String, Object>  data) {
    	if (data == null || data.size() < 3) return Collections.emptyList();

        
        ObjectMapper mapper = new ObjectMapper();
    	
    	List<PlaceDTO> places = mapper.convertValue(data.get("places"), new TypeReference<List<PlaceDTO>>() {});
        double longitude = Double.parseDouble(data.get("longitude").toString());
        double latitude = Double.parseDouble(data.get("latitude").toString());
        
        // 여기서 시작점이랑 끝점 설정.

        // 출발지 생성 및 배열에 추가
        PlaceDTO origin = new PlaceDTO();
        origin.setName("출발지");
        origin.setType("출발");
        origin.setReason("사용자가 선택한 출발지");
        origin.setDescription("출발지입니다.");
        origin.setReason("사용자 선택");
        origin.setLatitude(latitude);
        origin.setLongitude(longitude);
        origin.setImageUrl(null);
        
        places.add(0, origin);
        
        // 도착지 설정
        
        PlaceDTO destination = null;
        double maxDistance = -1;
        for (PlaceDTO place : places) {
            double dist = haversine(latitude, longitude, place.getLatitude(), place.getLongitude());
            if (dist > maxDistance) {
                maxDistance = dist;
                destination = place;
            }
        } 


        // 경유지 설정
        List<PlaceDTO> waypoints = new ArrayList<>();
        for (PlaceDTO place : places) {
            if (!place.equals(destination)) {
                waypoints.add(place);
            }
        }
        

        List<List<PlaceDTO>> permutations = new ArrayList<>();
        permute(waypoints, 0, permutations);

        double minDistance = Double.MAX_VALUE;
        List<PlaceDTO> bestRoute = null;

        for (List<PlaceDTO> perm : permutations) {
            List<PlaceDTO> route = new ArrayList<>();
            route.add(origin);
            route.addAll(perm);
            route.add(destination);

            double totalDistance = 0;
            for (int i = 0; i < route.size() - 1; i++) {
                PlaceDTO a = route.get(i);
                PlaceDTO b = route.get(i + 1);
                totalDistance += haversine(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
            }

            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                bestRoute = route;
            }
        }

        return bestRoute;
    }

    // 순열 생성
    private void permute(List<PlaceDTO> list, int start, List<List<PlaceDTO>> result) {
        if (start == list.size() - 1) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < list.size(); i++) {
            Collections.swap(list, i, start);
            permute(list, start + 1, result);
            Collections.swap(list, i, start);
        }
    }
}
