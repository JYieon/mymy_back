package com.trip.mymy.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.dto.TimelineDTO;
import com.trip.mymy.service.TimelineService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/timeline")
public class TimelineController {
    @Autowired 
    TimelineService ts;

    // 특정 게시글의 타임라인 조회
    @GetMapping("/{boardNo}")
    public List<TimelineDTO> getTimeline(@PathVariable int boardNo){
        System.out.println("🔍 일정 조회 요청 - boardNo: " + boardNo);
        List<TimelineDTO> timelineList = ts.TimelineList(boardNo);

        // 데이터 조회 후 로그 출력
        if (timelineList.isEmpty()) {
            System.out.println("🚫 해당 게시글의 일정이 없습니다.");
        } else {
            System.out.println("✅ 조회된 일정:");
            for (TimelineDTO t : timelineList) {
            	System.out.println("📌 일정 ID: " + t.getTimelineId());
                System.out.println("📅 시작 날짜: " + t.getStartDt());
                System.out.println("📅 종료 날짜: " + t.getEndDt());
                System.out.println("📜 일정 데이터 (JSON): " + t.getTodo()); 
            }
        }

        return timelineList;
    }

    // 📌 타임라인 데이터 추가 (POST 요청)
    @PostMapping("/add")
    public String addTimeline(@RequestBody TimelineDTO timeline) {
        System.out.println("📝 일정 추가 요청: " + timeline.toString());

        // ID 및 게시글 번호 설정 (임시)
        timeline.setId("a");
        timeline.setBoardNo(58);

        // 데이터 저장
        ts.addTimeline(timeline);
        System.out.println("✅ 일정 추가 완료: " + timeline.toString());

        return "일정 추가 성공!";
    }
}
