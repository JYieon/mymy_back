package com.trip.mymy.dto;

public class MapDTO {
    private int markerId;   // 마커 ID
    private int boardNo;    // 게시판 번호 (외래키)
    private double latitude; // 위도
    private double longitude; // 경도
    private String info;    // 인포윈도우 내용

    // 기본 생성자
    public MapDTO() {}

    // 생성자
    public MapDTO(int markerId, int boardNo, double latitude, double longitude, String info) {
        this.markerId = markerId;
        this.boardNo = boardNo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
    }

    // Getter & Setter
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
