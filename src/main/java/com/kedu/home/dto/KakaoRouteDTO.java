package com.kedu.home.dto;

import java.util.List;
import java.util.Map;

public class KakaoRouteDTO {
	
	private Map<String, String> start; // {"x": "126.97843", "y": "37.56668"}
	private List<Map<String, String>> goal;  // {"x": "127.02758", "y": "37.49794"}
	private List<Map<String, String>> waypoints; // [{"x": "...", "y": "..."}, ...]
	
		
	public Map<String, String> getStart() {
		return start;
	}
	public void setStart(Map<String, String> start) {
		this.start = start;
	}
	public List<Map<String, String>> getGoal() {
		return goal;
	}
	public void setGoal(List<Map<String, String>> goal) {
		this.goal = goal;
	}
	public List<Map<String, String>> getWaypoints() {
		return waypoints;
	}
	public void setWaypoints(List<Map<String, String>> waypoints) {
		this.waypoints = waypoints;
	}
	
}
