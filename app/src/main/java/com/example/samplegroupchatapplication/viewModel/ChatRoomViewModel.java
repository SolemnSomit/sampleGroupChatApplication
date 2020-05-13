package com.example.samplegroupchatapplication.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.samplegroupchatapplication.database.ChatRoom;
import com.example.samplegroupchatapplication.repository.ChatRoomRepository;

import java.util.List;

public class ChatRoomViewModel extends AndroidViewModel {

    private ChatRoomRepository mRepository;
    private LiveData<List<ChatRoom>> mChatRoomList;


    public ChatRoomViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ChatRoomRepository(application);
        mChatRoomList = mRepository.getAllChatRooms();
    }

    public LiveData<List<ChatRoom>> getAllRooms()
    {
        return mChatRoomList;
    }

    public void insert(ChatRoom room)
    {
        mRepository.inset(room);
    }
}
