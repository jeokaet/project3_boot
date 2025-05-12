package com.kedu.home.dto;

import java.util.List;

public class LLMRequestDTO {
	private String userInput;
	private List<PlaceDTO> examplePlaces;
	
	public LLMRequestDTO() {}
	
	public LLMRequestDTO(String userInput, List<PlaceDTO> examplePlaces) {
		super();
		this.userInput = userInput;
		this.examplePlaces = examplePlaces;
	}
	
	public String getUserInput() {
		return userInput;
	}
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	public List<PlaceDTO> getExamplePlaces() {
		return examplePlaces;
	}
	public void setExamplePlaces(List<PlaceDTO> examplePlaces) {
		this.examplePlaces = examplePlaces;
	}
	
}
