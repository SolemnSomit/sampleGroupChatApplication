package com.example.samplegroupchatapplication.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ChatRoomList")
public class ChatRoom {

    @PrimaryKey
    private int chatRoomID;
    private String chatRoomName;

    public ChatRoom( int chatRoomID, String chatRoomName)
    {
        this.chatRoomID  = chatRoomID;
        this.chatRoomName = chatRoomName;
    }

    public int getChatRoomID() {
        return chatRoomID;
    }

    public String getChatRoomName()
    {
        return chatRoomName;
    }
}
