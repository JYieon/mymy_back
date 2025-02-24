package com.trip.mymy.service;

import java.util.List;
import java.util.Map;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;

public interface BoardService {
	public boolean writeSave(BoardDTO dto);
	public List<Map<String, Object>> getBoardList(int page);  // 게시글 목록 조회 추가
	public BoardDTO getPost(int boardNo);
	public boolean modify(BoardDTO dto);
	public boolean delete(int boardNo);
	public int getTotalPosts();
	
	// 좋아요
	public void toggleLike(int boardNo);
	public int getLikes(int boardNo);
	public void increaseLike(int boardNo);
	public void decreaseLike(int boardNo);
	
	// 댓글
    public void addReply(BoardRepDTO replyDTO);
    public List<BoardRepDTO> getRepliesByBoardNo(int boardNo);
    public String deleteReply(int replyNo, String path);
	
	// 해시태그
    public List<String> tagList(int boardNo); 
    public void addTags(int boardNo, List<String> tags);
    public void deleteTags(int boardNo);
    public void updateTags(int boardNo, List<String> tags);
	}
