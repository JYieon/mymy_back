package com.trip.mymy.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MyBoardDTO {
	private int boardNo;
    private String title;
    private String content;
    private String originalPost; // 원본 게시글 (댓글 조회 시 필요)
    private String id;
    private String boardDate;
    private int boardCategory;
    private int boardCnt;
    private int replyCount;
    private int boardLikes;
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOriginalPost() {
		return originalPost;
	}
	public void setOriginalPost(String originalPost) {
		this.originalPost = originalPost;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getBoardDate() {
		return boardDate;
	}
	public void setBoardDate(String boardDate) {
		this.boardDate = boardDate;
	}
	public int getBoardCategory() {
		return boardCategory;
	}
	public void setBoardCategory(int boardCategory) {
		this.boardCategory = boardCategory;
	}
	public int getBoardCnt() {
		return boardCnt;
	}
	public void setBoardCnt(int boardCnt) {
		this.boardCnt = boardCnt;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public int getBoardLikes() {
		return boardLikes;
	}
	public void setBoardLikes(int boardLikes) {
		this.boardLikes = boardLikes;
	}
    
    
}
