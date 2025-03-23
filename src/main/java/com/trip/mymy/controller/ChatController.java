package com.trip.mymy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.AlarmDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.chat.ChatDTO;
import com.trip.mymy.dto.chat.ChatMessageDTO;
import com.trip.mymy.dto.chat.ChatResDTO;
import com.trip.mymy.dto.money.BankDTO;
import com.trip.mymy.dto.money.BankServiceDTO;
import com.trip.mymy.dto.money.SettlementDTO;
import com.trip.mymy.dto.money.SettlementServiceDTO;
import com.trip.mymy.service.AuthServiceImpl;
import com.trip.mymy.service.ChatServiceImpl;
import com.trip.mymy.service.MoneyServiceImpl;
import com.trip.mymy.service.PortOneService;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
public class ChatController {
   
   @Autowired AuthServiceImpl as;
   @Autowired ChatServiceImpl cs;
   @Autowired MoneyServiceImpl ms;
   @Autowired TokenProvider tp;
    @Autowired
    private AlarmController alramController;

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
   public ResponseEntity<ChatResDTO> getRoom(@RequestParam Long roomNum) {
      ChatResDTO res =  cs.joinRoom(roomNum);
      return ResponseEntity.ok(res);
   }
   
   @GetMapping("user/info")
   public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
       try {
          System.out.println("userInfo");
           Authentication authentication = tp.getAuthentication(token);
           MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
           System.out.println(member.getId());
           return ResponseEntity.ok(member);
       } catch (RuntimeException e) {
           // 예외 처리: 예를 들어, 토큰이 만료되었거나 권한이 없는 경우
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body("인증 실패: " + e.getMessage()); // 적절한 오류 메시지 반환
       }
   }
   
   @PostMapping("invite")
   public ResponseEntity<?> inviteUser(@RequestParam String token, String inviteUser, Long roomNum){
      Authentication authentication = tp.getAuthentication(token);
        MemberDTO sender = (MemberDTO) authentication.getPrincipal(); 
        
      MemberDTO dto = as.checkId(inviteUser);
       System.out.println(inviteUser);
       if (dto == null) { //아이디 DB에 없음
           return ResponseEntity.ok().body("없는 아이디"); 
       } else { //있는 아이디. 채팅방에 추가
          int result = cs.inviteMember(inviteUser, roomNum);
          System.out.println(roomNum);
          System.out.println(roomNum.intValue());
//          방 초대 알람
          AlarmDTO alarm = AlarmDTO.builder()
                    .senderId(sender.getNick())
                    .memberId(inviteUser)
                    .alarmTypeId(3)
                    .addr(roomNum.intValue())
                    .build();
              System.out.println("알람" + alarm);
              alramController.sendNotification(alarm);
         return ResponseEntity.ok(result);
       }
   }
   
   @DeleteMapping("endChat")
   public ResponseEntity<?> endChat(@RequestParam Long roomNum, String token) {
      System.out.println("endChat");
      System.out.println(roomNum + token);
      Authentication authentication = tp.getAuthentication(token);
      MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
      
      int result = cs.removeRoom(roomNum, member.getId());
      if(result == 1) {
         System.out.println("나가기 성공");
         return ResponseEntity.ok().build();
      }else {
         return ResponseEntity.status(500).build();
      }
   }

   //예금주 조회
    @GetMapping("/bank/check/name")
    public ResponseEntity<?> checkBankName(@RequestHeader("Authorization") String token, @RequestParam String bankCode, String bankNum) {
    	
		String bankHolderInfo = portOneService.getAccessToken(bankCode, bankNum);
		
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
		if(member.getName().equals(bankHolderInfo)) { //사용자 이름과 예금주 이름이 동일시
			int result = ms.insertMemberBank(member.getId(), bankNum); //통장번호 업데이트
			if(result == 1) {
				return ResponseEntity.status(200).build();  				
			}else {
		        return ResponseEntity.status(HttpStatus.CONFLICT).body("계좌 등록에 실패했습니다.");
		    }
		}else {
			 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("예금주 정보가 일치하지 않습니다.");
		}
    }
    
    //정산추가
    @PostMapping("/settlement/add")
    public ResponseEntity<Integer> addSettlement(@RequestParam int roomNum, String toMember, int money, int roomMember) throws Exception{
    	boolean member = cs.checkMember(toMember, roomNum); //채팅방 멤버의 아이디가 맞는지 확인
	    int result = 0;
	    if (member) {
	    	System.out.println(toMember + roomNum);
	    	result = ms.insertSettlement(roomNum, toMember, money, --roomMember);
	    }else {//없는 아이디
	    	System.out.println("없는 아이디 입니다.");
	    }    	
	    return ResponseEntity.ok(result);
    }
    
    //정산하기 버튼 settleNum이 1에 대한것은 프론트에서 응답이후 처리하기
    @PostMapping("/settlement")
    public void settlement(@RequestParam String token, int settleNum, int settleMember) {
    	Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
    	ms.settlement(settleNum, member.getNick()); //정산내역 추가
    	ms.updateSettleCheck(settleMember, settleNum); //check 업데이트
    }
    
    //정산내역 버튼
    @GetMapping("/settlement/list")
    public ResponseEntity<List<SettlementDTO>> getSettlementList(@RequestParam int roomNum) {
    	return ResponseEntity.ok(ms.getSettlement(roomNum));
    }
    
    //정산 서비스 내역
    @GetMapping("/settlement/service")
    public ResponseEntity<List<SettlementServiceDTO>> getSettlementServiceList(@RequestParam int settleNum) {
    	return ResponseEntity.ok(ms.getSettlementService(settleNum));
    }
    
    //여기부터 확인하기!!
    //모임통장 
    @GetMapping("/bank/check")
    public ResponseEntity<BankDTO> getBank(@RequestParam int roomNum) {
    	BankDTO bank = ms.getBank(roomNum);
    	return ResponseEntity.ok(bank);
    }
    
    @PostMapping("/bank/make")
    public void makeBank(@RequestParam int roomNum, String bankName, int target) {
       BankDTO bank = BankDTO.builder()
             .roomNum(roomNum)
             .bankName(bankName)
             .target(target)
             .build();
       ms.makeBank(bank);
    }
    
    //모임통장 내용
    @GetMapping("/bank/service/info")
    public ResponseEntity<List<BankServiceDTO>> getBankService(@RequestParam int roomNum) {
    	List<BankServiceDTO> bankInfo = ms.getBankService(roomNum);
    	
    	return ResponseEntity.ok(bankInfo);
    }
    
    
    //이체
    @PostMapping("/bank/service")
    public ResponseEntity<?> bankService(@RequestHeader("Authorization") String token, @RequestParam int roomNum, String type, int money) {
    	Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		BankServiceDTO bankSer = BankServiceDTO.builder()
				.roomNum(roomNum)
				.type(type)
				.money(money)
				.build();
    	 if(member != null) {
    		 bankSer.setMember(member.getId());
    		 BankDTO bank = ms.getBank(bankSer.getRoomNum());
    		 int result = ms.addBankService(bankSer, bank);
    		 return ResponseEntity.ok(result);
    	 }else {
    		 return ResponseEntity.status(400).build();
    	 }
    	
    }
}
