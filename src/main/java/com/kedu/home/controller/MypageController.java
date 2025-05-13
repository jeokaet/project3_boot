package com.kedu.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.dto.MemberDTO;
import com.kedu.home.services.MypageService;

@RestController
@RequestMapping("/mypage")
public class MypageController {
	
	@Autowired
	private MypageService mServ;
	
	@GetMapping("/getMembers")
	public ResponseEntity<MemberDTO> getMembers(@RequestParam String loginId){
	
		MemberDTO result = mServ.getMembers(loginId);
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/updateMember")
	public ResponseEntity<MemberDTO> updateMember(@RequestBody MemberDTO request){
		mServ.updateMember(request);
		
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("/deleteMember")
	public ResponseEntity<String> deleteMember(@RequestParam String loginId){
		mServ. deleteMember(loginId);
		
		return ResponseEntity.ok(null);
	}
	
	
}
