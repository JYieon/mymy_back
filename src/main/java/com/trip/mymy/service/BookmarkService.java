package com.trip.mymy.service;

import java.util.List;
import com.trip.mymy.dto.BoardDTO;

public interface BookmarkService {
	public boolean toggleBookmark(String id, int boardNo);
	public List<BoardDTO> getBookmarkList(String id);
	public boolean isBookmarked(String id, int boardNo);
}
