package com.example.samplegroupchatapplication.model;

public class fireStoreChatRoom {

    private String chatRoomTitle;
    private String chatRoomID;


    public fireStoreChatRoom(String chatRoomTitle, String chatRoomID) {
        this.chatRoomTitle = chatRoomTitle;
        this.chatRoomID = chatRoomID;
    }

    public fireStoreChatRoom() {
    }

    public String getChatRoomTitle() {
        return chatRoomTitle;
    }

    public void setChatRoomTitle(String chatRoomTitle) {
        this.chatRoomTitle = chatRoomTitle;
    }

    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }
}
