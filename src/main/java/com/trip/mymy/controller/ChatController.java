package com.trip.mymy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.ChatDTO;
import com.trip.mymy.dto.ChatMessageDTO;
import com.trip.mymy.dto.ChatResDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.AuthServiceImpl;
import com.trip.mymy.service.ChatServiceImpl;
import com.trip.mymy.service.PortOneService;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {
	
	@Autowired AuthServiceImpl ls;
	@Autowired ChatServiceImpl cs;
	@Autowired TokenProvider tp;

	 @Autowired PortOneService portOneService;
	
	@GetMapping("list")
	public ResponseEntity<List<ChatDTO>> getList(@RequestParam String token){
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
		List<ChatDTO> chatList = cs.findChatList(member.getId());
		for (ChatDTO chat : chatList) {
		    System.out.println(chat);
		}
		return ResponseEntity.ok(chatList);
	}
	
	@PostMapping("create")
	public ResponseEntity<Integer> createRoom(@RequestParam String token, String roomName) {
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
		int result = cs.createRoom(member.getId(), roomName);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("join") //모든 채팅 정보 리턴
	public ResponseEntity<ChatResDTO> getRoom(@RequestParam String roomNum) {
		ChatResDTO res =  cs.joinRoom(Long.parseLong(roomNum));
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("user/info")
	public ResponseEntity<MemberDTO> getuserInfo(@RequestParam String token){
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		return ResponseEntity.ok(member);
	}
	
	@PostMapping("invite")
	public ResponseEntity<?> inviteUser(@RequestParam String inviteUser, String roomNum){
		MemberDTO dto = ls.checkId(inviteUser);
	    System.out.println(inviteUser);
	    if (dto == null) { //아이디 DB에 없음
	        return ResponseEntity.ok().body("없는 아이디"); 
	    } else { //있는 아이디. 채팅방에 추가
	    	int result = cs.inviteMember(inviteUser, Long.parseLong(roomNum));
			return ResponseEntity.ok(result);
	    }
		
	}
	
	@GetMapping("/bank/check")
	public ResponseEntity<String> checkBank(@RequestParam String token) {
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
		ResponseEntity.ok(cs.bankCheck(member.getId()));
	}

	//예금주 조회
    @PostMapping("/bank/check")
    public ResponseEntity<HashMap> checkBankName(@RequestParam String bankCode, @RequestParam String bankNum, @RequestParam String token) {

    	Map<String, String> result = new HashMap();
    	
		String bankHolderInfo = portOneService.getAccessToken(bankCode, bankNum);
		
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
		if(member.getId().equals(bankHolderInfo)) {
			
			cs.insertMemberBank()
		}else {
			
		}
		
		return ResponseEntity.ok(bankHolderInfo);
    }
    
    //정산하기
    //@PostMapping()
}
