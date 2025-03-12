package com.trip.mymy.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.dto.TimelineDTO;
import com.trip.mymy.service.TimelineService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/timeline")
public class TimelineController {
	@Autowired 
	TimelineService ts;

    @PostMapping("/add")
    public String addTimeline(@RequestBody TimelineDTO timeline) {
    	ts.insertTimeline(timeline);
        return "타임라인 추가 성공";
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
