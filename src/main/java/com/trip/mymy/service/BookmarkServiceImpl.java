package com.trip.mymy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BookmarkDTO;
import com.trip.mymy.mybatis.BookmarkMapper;

@Service
public class BookmarkServiceImpl implements BookmarkService{
	@Autowired
	public BookmarkMapper bookmarkMapper;
	
	@Transactional
	@Override
	public boolean toggleBookmark(String id, int boardNo) {
	    // 북마크 여부 확인
	    boolean exists = bookmarkMapper.checkBookmark(id, boardNo) > 0;

	    // 북마크가 존재하는 경우 삭제
	    if (exists) {
	        int deleted = bookmarkMapper.deleteBookmark(id, boardNo);
	        if (deleted > 0) {
	            // 삭제 후 북마크가 없으므로 false 반환
	            return false; // 북마크 삭제
	        } else {
	            return false; // 삭제 실패
	        }
	    } else {
	        // 북마크가 없으면 추가
	        int added = bookmarkMapper.addBookmark(id, boardNo);
	        return added > 0; // 추가 성공 시 true
	    }
	}

    // 북마크 목록 조회
	public List<BoardDTO> getBookmarkList(String id) { 
	    List<BoardDTO> list = bookmarkMapper.getBookmarkList(id);
	    return list;
	}


    // 특정 게시글이 북마크되었는지 확인
    public boolean isBookmarked(String id, int boardNo) {
        return bookmarkMapper.checkBookmark(id, boardNo) > 0;
    }
}
