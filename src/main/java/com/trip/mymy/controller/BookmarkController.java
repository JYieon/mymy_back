package com.trip.mymy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BookmarkDTO;
import com.trip.mymy.service.BookmarkService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/board/bookmark")
public class BookmarkController {
	@Autowired BookmarkService bookmarkService;
	
	//북마크 추가,삭제 토글
	@PostMapping("/toggle")
    public ResponseEntity<String> toggleBookmark(@RequestParam(defaultValue = "a") String id, @RequestParam int boardNo) {
        boolean isBookmarked = bookmarkService.toggleBookmark(id, boardNo);
        return ResponseEntity.ok(isBookmarked ? "북마크 추가" : "북마크 삭제");
    }
	
//	// 북마크 추가, 삭제 후 별도 메시지 없이 성공 응답만 반환
//	@PostMapping("/toggle")
//	public ResponseEntity<Void> toggleBookmark(@RequestParam(defaultValue = "a") String id, @RequestParam int boardNo) {
//	    bookmarkService.toggleBookmark(id, boardNo);
//	    return ResponseEntity.ok().build(); // ✅ HTTP 200 응답만 반환 (메시지 없음)
//	}
	
	//사용자 북마크 목록 조회
	@GetMapping("/list")
	public String getBookmarkList(@RequestParam(defaultValue = "a") String id, Model model) {
	    List<BoardDTO> BookmarkList = bookmarkService.getBookmarkList(id);
        
       // System.out.println("사용자 ID: " + id);
       // System.out.println("북마크한 게시글 수: " + BookmarkList.size());
        
        model.addAttribute("bookmarkList", BookmarkList);
        return "board/bookmarkList"; // "board/bookmarkList.jsp"로 이동
    }
	
	 @GetMapping("/check")
	    @ResponseBody
	    public ResponseEntity<Boolean> checkBookmark(@RequestParam("id") String id, 
	                                                 @RequestParam("boardNo") int boardNo) {
	        boolean isBookmarked = bookmarkService.isBookmarked(id, boardNo);
	        return ResponseEntity.ok(isBookmarked);
	    }
	
}
