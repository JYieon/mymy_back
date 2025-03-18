package com.trip.mymy.dto;

public class MapDTO {
    public int markerId, boardNo; 
    public double latitude, longitude; // 위도
    public String info;
	public int getMarkerId() {
		return markerId;
	}
	public void setMarkerId(int markerId) {
		this.markerId = markerId;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}    
}
