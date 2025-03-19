package com.trip.mymy.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoardDTO {
	int boardNo, boardCnt, boardLikes;
	int boardCategory;
	private Integer boardOpen;
	String title, content, id, date; 
	private List<String> hashtags;
	
	public List<String> getHashtags() {
		return hashtags;
	}
	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}
	public Integer getBoardOpen() {
		return boardOpen;
	}
	public void setBoardOpen(Integer boardOpen) {
		this.boardOpen = (boardOpen == null) ? 1 : boardOpen; // null이면 1로 설정
	}
	public int getBoardCategory() {
		return boardCategory;
	}
	public void setBoardCategory(int boardCategory) {
		this.boardCategory = boardCategory;
	}	
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public int getBoardCnt() {
		return boardCnt;
	}
	public void setBoardCnt(int boardCnt) {
		this.boardCnt = boardCnt;
	}
	public int getBoardLikes() {
		return boardLikes;
	}
	public void setBoardLikes(int boardLikes) {
		this.boardLikes = boardLikes;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		SimpleDateFormat fo= new SimpleDateFormat("YYYY년 MM월 dd일 HH시 mm분 ss초");
		this.date = fo.format(date);
	}
	
}
