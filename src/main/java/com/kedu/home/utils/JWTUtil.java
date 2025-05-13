package com.kedu.home.utils;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	public String createToken(String id) {
		
		Algorithm algorithm = Algorithm.HMAC256(secret);
		Date now = new Date();
		Date expiresAt = new Date(now.getTime()+43200000);
		return JWT.create().withSubject(id)
				.withIssuedAt(now)
				.withExpiresAt(expiresAt)
				.sign(algorithm);
	}
	
	public boolean validataion(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}	
	//token 에서 ID 추출
	public String getUsernameFromToken(String token) {
		DecodedJWT decode = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
		return decode.getSubject();
	}
	//token 에서 Roles 추출
	public List<String> getPermissionFromToken(String token) {
		DecodedJWT decode = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
		return decode.getClaim("permission").asList(String.class);
	}
	
}
