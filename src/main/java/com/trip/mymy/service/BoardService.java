package com.trip.mymy.service;

import java.util.List;
import java.util.Map;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;

public interface BoardService {
	public boolean writeSave(BoardDTO dto);
	public List<Map<String, Object>> getBoardList(int page, int category, String id);  // 게시글 목록 조회 추가
	public BoardDTO getPost(int boardNo);
	public boolean modify(BoardDTO dto);
	public boolean delete(int boardNo);
	public int getTotalPosts(int category);

	// 좋아요
	public boolean toggleLike(String id, int boardNo);
	public int getBoardLikes(int boardNo);
	public boolean checkUserLike(String id, int boardNo);

	// 댓글
	public void addReply(BoardRepDTO replyDTO);
	public List<BoardRepDTO> getRepliesByBoardNo(int boardNo);
	public String deleteReply(int replyNo, String path);

	// 해시태그
	public List<String> tagList(int boardNo); 
	public void addTags(int boardNo, List<String> tags);
	public void deleteTags(int boardNo);
	public void updateTags(int boardNo, List<String> tags);

	//검색
	public List<Map<String, Object>> searchBoardList(int page,int category, String searchType, String keyword);
	public int getSearchTotalPosts(int category, String searchType, String keyword);


	// 여행 메이트
	public boolean writeMateBoardSave(BoardDTO dto);
	public boolean modifyMateBoard(BoardDTO dto);
	public boolean deleteMateBoard(int boardNo);
	public BoardDTO getMateBoardDetail(int boardNo);
	public int getSearchMateTotalPosts(String searchType, String keyword);
	public List<Map<String, Object>> searchMateBoardList(int page, int category, String searchType, String keyword);
}
