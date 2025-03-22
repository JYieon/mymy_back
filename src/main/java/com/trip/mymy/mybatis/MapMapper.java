package com.trip.mymy.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.MapDTO;

@Mapper
public interface MapMapper {
    // 마커 추가
    public int insertMarker(MapDTO map);

    // 특정 게시판(boardNo)에 등록된 마커 조회
    public List<MapDTO> getMarkersByBoard(@Param("boardNo") int boardNo);

    // 특정 마커 삭제
    public void deleteMarker(@Param("markerId") int markerId);

    // 특정 게시판(boardNo)의 모든 마커 삭제
    public void deleteMarkersByBoard(@Param("boardNo") int boardNo);
}
