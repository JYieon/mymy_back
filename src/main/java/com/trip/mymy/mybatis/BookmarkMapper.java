package com.trip.mymy.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BookmarkDTO;

public interface BookmarkMapper {
	public int addBookmark(@Param("id") String id, @Param("boardNo") int boardNo);
	public int deleteBookmark(@Param("id") String id, @Param("boardNo") int boardNo);
	public int checkBookmark(@Param("id") String id, @Param("boardNo") int boardNo);
	public List<BoardDTO> getBookmarkList(@Param("id") String id);
}
