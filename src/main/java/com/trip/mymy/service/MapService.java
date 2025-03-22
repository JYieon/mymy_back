package com.trip.mymy.service;

import java.util.List;
import com.trip.mymy.dto.MapDTO;

public interface MapService {
    public int insertMarker(MapDTO map);            // 마커 추가
    public List<MapDTO> getMarkersByBoard(int boardNo); // 특정 게시판의 마커 조회
    public void deleteMarker(int markerId);          // 특정 마커 삭제
    public void deleteMarkersByBoard(int boardNo);   // 특정 게시판의 모든 마커 삭제
}
