package com.trip.mymy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.service.BookmarkService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board/bookmark")
public class BookmarkController {
	@Autowired BookmarkService bookmarkService;

	// 북마크 추가,삭제 토글
	@PostMapping("/toggle")
	public ResponseEntity<String> toggleBookmark(@RequestParam(defaultValue = "a") String id, @RequestParam int boardNo) {
		boolean isBookmarked = bookmarkService.toggleBookmark(id, boardNo);
		return ResponseEntity.ok(isBookmarked ? "북마크 추가" : "북마크 삭제");
	}

	// 사용자 북마크 목록 조회
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<List<BoardDTO>> getBookmarkList(@RequestParam(defaultValue = "a") String id) {
		List<BoardDTO> bookmarkList = bookmarkService.getBookmarkList(id);
		return ResponseEntity.ok(bookmarkList); // JSON으로 반환
	}

	// 북마크 상태 확인
	@GetMapping("/check")
	@ResponseBody
	public ResponseEntity<Boolean> checkBookmark(@RequestParam("id") String id, 
			@RequestParam("boardNo") int boardNo) {
		boolean isBookmarked = bookmarkService.isBookmarked(id, boardNo);
		return ResponseEntity.ok(isBookmarked);
	}

}
