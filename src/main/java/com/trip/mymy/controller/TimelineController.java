package com.trip.mymy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.common.jwt.TokenProvider;
import com.trip.mymy.dto.MemberDTO;
import com.trip.mymy.dto.TimelineDTO;
import com.trip.mymy.service.TimelineService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/timeline")
public class TimelineController {
	@Autowired TimelineService ts;
	@Autowired TokenProvider tp;

	@PostMapping("/add")
	public ResponseEntity<String> addTimeline(@RequestBody TimelineDTO timeline, @RequestHeader("Authorization") String token) {
		// "Bearer " 부분을 제거하고 실제 토큰만 사용
		String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
		Authentication authentication = tp.getAuthentication(jwtToken);
		// 토큰을 통해 인증 정보를 가져옴
		MemberDTO member = (MemberDTO) authentication.getPrincipal();
		timeline.setId(member.getId());  // 타임라인에 사용자 ID 설정

		ts.insertTimeline(timeline);
		 return ResponseEntity.ok("타임라인 추가 성공");
	}

	@GetMapping("/{boardNo}")
	public TimelineDTO getTimeline(@PathVariable int boardNo) {
		return ts.getTimeline(boardNo);
	}


	@DeleteMapping("/delete/{timelineId}")
	public String deleteTimeline(@PathVariable int timelineId) {
		ts.deleteTimeline(timelineId);
		return "타임라인 삭제 성공";
	}

	@PostMapping("/updateTodo")
	public ResponseEntity<String> updateTimelineTodo(@RequestBody Map<String, String> updateData) {
		try {
			//System.out.println("받은 데이터:"+updateData);
			int boardNo = Integer.parseInt(updateData.get("boardNo")); 
			String updatedTodo = updateData.get("todo");

			//System.out.println("boardNo: "+ boardNo);
			//System.out.println("updatetodo:"+ updatedTodo);
			ts.updateTimelineTodo(boardNo, updatedTodo);
			return ResponseEntity.ok("타임라인 수정 성공");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("수정 오류: " + e.getMessage());
		}
	}

}
