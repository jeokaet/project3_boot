package com.kedu.home.services;

import com.kedu.home.dto.PlaceDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    public List<PlaceDTO> getOptimizedRoute(List<PlaceDTO> places) {
        if (places == null || places.size() < 3) return places;

        PlaceDTO origin = places.get(0);
        PlaceDTO destination = places.get(places.size() - 1);
        List<PlaceDTO> waypoints = places.subList(1, places.size() - 1);

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
