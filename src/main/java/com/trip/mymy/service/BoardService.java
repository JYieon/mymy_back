package com.trip.mymy.service;

import java.util.List;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;

public interface BoardService {
	public String writeSave(BoardDTO dto);
	public List<BoardDTO> getBoardList(int page);  // 게시글 목록 조회 추가
	public BoardDTO getPost(int boardNo);
	public String modify(BoardDTO dto, String path);
	public String delete(int boardNo, String path);
	public int getTotalPosts();
	
	public void toggleLike(int boardNo);
	public int getLikes(int boardNo);
	public void increaseLike(int boardNo);
	public void decreaseLike(int boardNo);
	
	//댓글
    public void addReply(BoardRepDTO replyDTO);
    public List<BoardRepDTO> getRepliesByBoardNo(int boardNo);
    public String deleteReply(int replyNo, String path);
	
	
	}
