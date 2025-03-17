package com.trip.mymy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.MapDTO;
import com.trip.mymy.mybatis.MapMapper;

@Service
@Transactional
public class MapServiceImpl implements MapService {

    @Autowired
    private MapMapper mapMapper;

    @Override
    public void insertMarker(MapDTO map) {
        mapMapper.insertMarker(map);
    }

    @Override
    public List<MapDTO> getMarkersByBoard(int boardNo) {
        return mapMapper.getMarkersByBoard(boardNo);
    }

    @Override
    public void deleteMarker(int markerId) {
        mapMapper.deleteMarker(markerId);
    }

    @Override
    public void deleteMarkersByBoard(int boardNo) {
        mapMapper.deleteMarkersByBoard(boardNo);
    }
}
