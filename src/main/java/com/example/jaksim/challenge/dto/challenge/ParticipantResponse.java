package com.example.jaksim.challenge.dto;

public class ParticipantResponse {
    private String userUuid;
    private String userImage;
    private String userName;
    private int userPoint;

// 각 유저별 포인트

    public ParticipantResponse(String userUuid, String userImage, String userName, int userPoint) {
        this.userUuid = userUuid;
        this.userImage = userImage;
        this.userName = userName;
        this.userPoint = userPoint;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public int getUserPoint(){
        return userPoint;
    }

    public void setUserPoint(int userPoint){
        this.userPoint = userPoint;
    }
}
