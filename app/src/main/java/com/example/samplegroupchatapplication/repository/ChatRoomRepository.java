package com.example.samplegroupchatapplication.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.samplegroupchatapplication.database.ChatRoom;
import com.example.samplegroupchatapplication.database.ChatRoomDao;
import com.example.samplegroupchatapplication.database.ChatRoomDatabase;

import java.util.List;

public class ChatRoomRepository  {

    private ChatRoomDao chatRoomDao;
    private LiveData<List<ChatRoom>> chatRoomList;

    //TODO:- decouple application from here for unit testing
    public ChatRoomRepository(Application application)
    {
        ChatRoomDatabase db = ChatRoomDatabase.getDatabase(application);
        chatRoomDao = db.chatRoomDao();
        chatRoomList = chatRoomDao.getAll();
    }

    public LiveData<List<ChatRoom>> getAllChatRooms()
    {
        return chatRoomList;
    }

    public void inset(final ChatRoom chatRoom)
    {
        ChatRoomDatabase.databaseWriteExecutor.execute(() -> chatRoomDao.insert(chatRoom));
    }
}
