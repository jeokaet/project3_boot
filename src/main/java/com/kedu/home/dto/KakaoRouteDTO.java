package com.kedu.home.dto;

import java.util.List;
import java.util.Map;

public class KakaoRouteDTO {
	private Map<String, Double> origin;
	private List<Map<String, Double>> destinations;
	
	
	public Map<String, Double> getOrigin() {
		return origin;
	}
	public void setOrigin(Map<String, Double> origin) {
		this.origin = origin;
	}
	public List<Map<String, Double>> getDestinations() {
		return destinations;
	}
	public void setDestinations(List<Map<String, Double>> destinations) {
		this.destinations = destinations;
	}
	
	
}
