package com.trip.mymy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trip.mymy.dto.MapDTO;
import com.trip.mymy.service.MapService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/map")
public class MapController {

    @Autowired
    private MapService mapService;
    
    // 마커 추가
    @PostMapping("/add")
    public ResponseEntity<Integer> addMarker(@RequestBody MapDTO map) {
        int markerId = mapService.insertMarker(map);
        return ResponseEntity.ok(markerId);
    }

    // 마커 불러오기
    @GetMapping("/list/{boardNo}")
    public ResponseEntity<List<MapDTO>> getMarkersByBoard(@PathVariable int boardNo) {
        List<MapDTO> markers = mapService.getMarkersByBoard(boardNo);
        System.out.println("📍 [백엔드] 마커 리스트 조회 결과: " + markers);
        return ResponseEntity.ok(markers);
    }

    // 마커 삭제
    @DeleteMapping("/delete/{markerId}")
    public ResponseEntity<String> deleteMarker(@PathVariable int markerId) {
        mapService.deleteMarker(markerId);
        return ResponseEntity.ok("마커가 삭제되었습니다!");
    }

    // 특정 게시판의 모든 마커 삭제
    @DeleteMapping("/deleteByBoard/{boardNo}")
    public ResponseEntity<String> deleteMarkersByBoard(@PathVariable int boardNo) {
        mapService.deleteMarkersByBoard(boardNo);
        return ResponseEntity.ok("해당 게시판의 모든 마커가 삭제되었습니다!");
    }
}
