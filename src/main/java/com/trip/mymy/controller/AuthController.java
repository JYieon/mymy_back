package com.trip.mymy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.AuthServiceImpl;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
	
	@Autowired AuthServiceImpl ls;
	
	@PostMapping("/login")
	public ResponseEntity<MemberDTO> loginCheck(@RequestBody Map<String, String> loginData) {
		
		return ResponseEntity.ok(ls.loginCheck(loginData));

	}
	
	
	
}
