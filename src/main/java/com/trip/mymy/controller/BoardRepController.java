package com.trip.mymy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.BoardService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board")
public class BoardRepController {

	@Autowired BoardService bs;
	@Autowired TokenProvider tp;
	@Autowired AlarmController alramController;

	// 댓글 작성
	 @PostMapping("/addReply")
	    public ResponseEntity<String> addReply(@RequestBody BoardRepDTO dto, @RequestHeader("Authorization") String token) {
	        // 토큰이 비어 있거나 null일 경우 처리
	        if (token == null || token.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 비어 있거나 null입니다.");
	        }
	            // "Bearer " 부분을 제거하고 실제 토큰만 사용
//	            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

	            // 토큰을 통해 인증 정보를 가져옴
	            Authentication authentication = tp.getAuthentication(token);
	            if (authentication == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	            }

	            MemberDTO member = (MemberDTO) authentication.getPrincipal();
	            dto.setId(member.getId()); // 사용자 ID 설정
	            
	            // 댓글 작성
	            bs.addReply(dto);
	            
//	            게시글 작성자 불러오기
	            BoardDTO post = bs.getPost(dto.getBoardNo());
//	            댓글 알림
	            if(!member.getId().equals(post.getId())) { //내가 쓴 댓글은 알림 안가도록
	            	AlarmDTO alarm = AlarmDTO.builder()
				            .senderId(member.getNick())      
				            .memberId(post.getId()) 
				            .alarmTypeId(2)
				            .addr(dto.getBoardNo())
				            .build();

				    System.out.println("🔔 알람 전송: " + alarm);
				    alramController.sendNotification(alarm);
	            }
	            
			    
	            return ResponseEntity.ok("댓글 작성 성공!");
	        }
	 @GetMapping("/replyList/{boardNo}")
	 public ResponseEntity<List<BoardRepDTO>> getReplyList(@PathVariable int boardNo) {
	     System.out.println("🔍 댓글 가져오기 요청: boardNo = " + boardNo);
	     List<BoardRepDTO> replies = bs.getRepliesByBoardNo(boardNo);
	     System.out.println("✅ 조회된 댓글: " + replies);
	     return ResponseEntity.ok(replies);
	 }


	@DeleteMapping("/deleteReply/{replyNo}")
	public ResponseEntity<String> deleteReply(@PathVariable int replyNo, @RequestHeader("Authorization") String token) {
		if (token == null || token.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 비어 있습니다.");
		}

		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		Authentication authentication = tp.getAuthentication(jwtToken);
		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
		}

		MemberDTO member = (MemberDTO) authentication.getPrincipal();
		String loggedInUserId = member.getId();

		try {
			
			int deletedCount = bs.deleteReply(replyNo, loggedInUserId);

			if (deletedCount > 0) {
				return ResponseEntity.ok("댓글 삭제 성공!");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("삭제할 댓글이 존재하지 않습니다.");
			}
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다");
		}

	}

	}
