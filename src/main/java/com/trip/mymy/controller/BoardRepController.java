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
import com.trip.mymy.dto.BoardRepDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.BoardService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board")
public class BoardRepController {

	@Autowired BoardService bs;
	@Autowired TokenProvider tp;

	// 댓글 작성
	 @PostMapping("/addReply")
	    public ResponseEntity<String> addReply(@RequestBody BoardRepDTO dto, @RequestHeader("Authorization") String token) {
	        // 토큰이 비어 있거나 null일 경우 처리
	        if (token == null || token.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT 토큰이 비어 있거나 null입니다.");
	        }
	            // "Bearer " 부분을 제거하고 실제 토큰만 사용
	            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

	            // 토큰을 통해 인증 정보를 가져옴
	            Authentication authentication = tp.getAuthentication(jwtToken);
	            if (authentication == null) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	            }

	            MemberDTO member = (MemberDTO) authentication.getPrincipal();
	            dto.setId(member.getId()); // 사용자 ID 설정
	            
	            // 댓글 작성
	            bs.addReply(dto);
	            return ResponseEntity.ok("댓글 작성 성공!");
	        }
	// 댓글 목록 조회
	@GetMapping("/replyList/{boardNo}")
	public List<BoardRepDTO> getReplyList(@PathVariable int boardNo) {
		return bs.getRepliesByBoardNo(boardNo);
	}

	// 댓글 삭제
	@DeleteMapping("/deleteReply/{replyNo}")
	public ResponseEntity<String> deleteReply(@PathVariable int replyNo, @RequestHeader("Authorization") String token) {
		bs.deleteReply(replyNo, null);
		return ResponseEntity.ok("댓글 삭제 성공!");
	}


}
