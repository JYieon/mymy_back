package com.trip.mymy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.service.BookmarkService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board/bookmark")
public class BookmarkController {
	@Autowired BookmarkService bs;
	@Autowired TokenProvider tp;
	
	// 북마크 추가, 삭제 토글
	@PostMapping("/toggle")
	public ResponseEntity<String> toggleBookmark(@RequestParam String token, @RequestParam int boardNo) {
	    System.out.println("token: "+token+", boardNo: "+boardNo);
		
	    Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
	    
	    boolean isBookmarked = bs.toggleBookmark(member.getId(), boardNo);
	    return ResponseEntity.ok(isBookmarked ? "북마크 추가" : "북마크 삭제");
	}

	// 사용자 북마크 목록 조회
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<List<BoardDTO>> getBookmarkList(@RequestParam String token) {
		
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
	 
	    // 사용자 ID로 북마크 목록 조회
	    List<BoardDTO> bookmarkList = bs.getBookmarkList(member.getId());
	    return ResponseEntity.ok(bookmarkList); // JSON으로 반환
	}

	// 북마크 상태 확인
	@GetMapping("/check")
	public ResponseEntity<Boolean> checkBookmark(@RequestParam int boardNo, @RequestParam String token) {
	    // 토큰에서 사용자 ID 추출
		Authentication authentication = tp.getAuthentication(token);
		MemberDTO member = (MemberDTO) authentication.getPrincipal(); 
		
	    boolean isBookmarked = bs.isBookmarked(member.getId(), boardNo);
	    return ResponseEntity.ok(isBookmarked); // 상태 반환
	}


}
