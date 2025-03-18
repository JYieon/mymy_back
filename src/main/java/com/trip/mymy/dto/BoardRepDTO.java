package com.trip.mymy.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class BoardRepDTO {
	String id, repContent, repDate;
	int repNo, boardNo;
	private Integer parentNo = 0; 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRepContent() {
		return repContent;
	}
	public void setRepContent(String repContent) {
		this.repContent = repContent;
	}
	public String getRepDate() {
		return repDate;
	}
	public void setRepDate(Timestamp repDate) {
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        this.repDate = sdf.format(repDate);
	}
	public int getRepNo() {
		return repNo;
	}
	public void setRepNo(int repNo) {
		this.repNo = repNo;
	}
	public Integer getParentNo() {
		return parentNo;
	}
	public void setParentNo(Integer parentNo) {
		this.parentNo = (parentNo == null) ? 0 : parentNo;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	
}
