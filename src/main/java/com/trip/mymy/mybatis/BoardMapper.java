package com.trip.mymy.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;

public interface BoardMapper {
	public int writeSave(BoardDTO dto);
	public List<BoardDTO> getBoardList(Map<String, Integer> params);
	public BoardDTO getPost(int boardNo);
	public int modify(BoardDTO dto);
	public int getTotalPosts();
	public void boardCnt(@Param("boardNo") int boardNo);
	public int deleteAllByBoardNo(@Param("boardNo") int boardNo);
	
	// 좋아요
	public int getLikes(@Param("boardNo") int boardNo);
	public int increaseLike(@Param("boardNo") int boardNo);
	public int decreaseLike(@Param("boardNo") int boardNo);
	
	// 댓글
	public void addReply(BoardRepDTO replyDTO);
	public List<BoardRepDTO> getRepData(@Param("boardNo") int boardNo);
	public int deleteReply(@Param("replyNo") int replyNo);
	public int checkParentExists(@Param("parentNo") int parentNo);
	
	// 해시태그
	public List<String> getTagsByBoardNo(@Param("boardNo") int boardNo);
	public void insertTag(@Param("tagName") String tagName);
	public void insertBoardTag(Map<String, Object> params);
	public int deleteBoardTags(@Param("boardNo") int boardNo);

}
