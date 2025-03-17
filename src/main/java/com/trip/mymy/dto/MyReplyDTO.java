package com.trip.mymy.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class MyReplyDTO {
    private int replyNo;
    private String content;
    private int boardNo;
    private String boardTitle;
    private String date;

    public int getReplyNo() {
        return replyNo;
    }

    public void setReplyNo(int replyNo) {
        this.replyNo = replyNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.format(date);
    }
}
