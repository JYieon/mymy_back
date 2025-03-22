package com.trip.mymy.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.trip.mymy.dto.BoardDTO;
import com.trip.mymy.dto.BoardRepDTO;

public interface BoardMapper {
	public int writeSave(BoardDTO dto);
	public List<BoardDTO> getBoardList(Map<String, Integer> params);
	public List<BoardDTO> getUserBoardList(@Param("offset") int offset, @Param("limit") int limit,@Param("category") int category, @Param("id") String id);
	public int getTotalPosts(@Param("category") int category);
	public BoardDTO getPost(int boardNo);
	public int modify(BoardDTO dto);
	public void boardCnt(@Param("boardNo") int boardNo);
	public int deleteAllByBoardNo(@Param("boardNo") int boardNo);

	// 좋아요
	public int getBoardLikes(@Param("boardNo") int boardNo);
	public int checkUserLike(@Param("id") String id, @Param("boardNo") int boardNo);
	public void addLike(Map<String, Object> params);
	public void removeLike(Map<String, Object> params);
	public void updateBoardLikes(@Param("boardNo") int boardNo);


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

	// 검색
	public List<Map<String, Object>> searchBoardList(Map<String, Object> params);
	public int getSearchTotalPosts(Map<String, Object> params);

	// 여행
	public int insertMateBoard(BoardDTO dto);
	public int modifyMateBoard(BoardDTO dto);
	public int deleteMateBoard(@Param("boardNo") int boardNo);
	public BoardDTO getMateBoardDetail(@Param("boardNo") int boardNo);
	public List<Map<String, Object>> searchMateBoardList(Map<String, Object> params);
	public int getSearchMateTotalPosts(Map<String, Object> params);
}
