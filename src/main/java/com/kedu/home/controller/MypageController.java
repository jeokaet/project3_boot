package com.kedu.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.services.MypageService;

@RestController
@RequestMapping("/mypage")
public class MypageController {
	
	@Autowired
	private MypageService mServ;
	
	@GetMapping("/getMembers")
	public ResponseEntity<String> getMembers(){
		
//		mServ.getMembers();
		return ResponseEntity.ok(null);
	}
	
	
}
