package com.example.samplegroupchatapplication.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class fireStoreChatMessage {

    private fireStoreChatUser mUser;
    private String message;
    private String messageID;
    private @ServerTimestamp Date timestamp;

    public fireStoreChatMessage(fireStoreChatUser mUser, String message, String messageID, Date timestamp) {
        this.mUser = mUser;
        this.message = message;
        this.messageID = messageID;
        this.timestamp = timestamp;
    }

    public fireStoreChatMessage() {
    }

    public fireStoreChatUser getUser() {
        return mUser;
    }

    public void setUser(fireStoreChatUser mUser) {
        this.mUser = mUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
