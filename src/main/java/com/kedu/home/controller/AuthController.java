package com.kedu.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedu.home.dto.MemberDTO;
import com.kedu.home.services.AuthService;
import com.kedu.home.utils.JWTUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private JWTUtil jwt;
	@Autowired
	private AuthService aServ;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO dto) {
    
    	System.out.println("넘겨받은 값 : " + dto);
        String id = dto.getLoginId();
        String pw = dto.getPw();
        System.out.println("아이디 : " + id + ", 패스워드 : " + pw);

        if (aServ.isAvailableIdAndPw(id, pw)) {
            String token = jwt.createToken(id);
            return ResponseEntity.ok(token); // 오직 JWT 토큰만 반환s
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberDTO dto) {
        boolean result = aServ.registerMember(dto);
        if (result) {
            return ResponseEntity.ok("회원가입 성공");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok().build();
    }
}