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
	
	//북마크 추가 삭제 토글
	@Transactional
	@Override
    public boolean toggleBookmark(String id, int boardNo) {
        boolean exists = bookmarkMapper.checkBookmark(id, boardNo) > 0;
        if (exists) {
            int deleted = bookmarkMapper.deleteBookmark(id, boardNo);
            return deleted > 0; // 삭제 성공 여부 반환
        } else {
            int added = bookmarkMapper.addBookmark(id, boardNo);
            return added > 0; // 추가 성공 여부 반환
        }
    }

    // 북마크 목록 조회
	public List<BoardDTO> getBookmarkList(String id) {
		id="a";
	    List<BoardDTO> list = bookmarkMapper.getBookmarkList(id);

//	    System.out.println("=== 북마크된 게시글 목록 ===");
//	    for (BoardDTO dto : list) {
//	        System.out.println("boardNo: " + dto.getBoardNo() + ", title: " + dto.getTitle());
//	    }
//	    System.out.println("==========================");

	    return list;
	}


    // 특정 게시글이 북마크되었는지 확인
    @Override
    public boolean isBookmarked(String id, int boardNo) {
        return bookmarkMapper.checkBookmark(id, boardNo) > 0;
    }
}
