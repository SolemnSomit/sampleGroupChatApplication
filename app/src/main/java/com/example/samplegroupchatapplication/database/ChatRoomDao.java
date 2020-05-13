package com.example.samplegroupchatapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatRoomDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ChatRoom room);

    @Query("DELETE  FROM ChatRoomList")
    void delete();

    @Query("DELETE FROM ChatRoomList WHERE chatRoomID = :roomID")
    void deleteByRoomId(int roomID);

    @Query("SELECT * FROM ChatRoomList")
    LiveData<List<ChatRoom>> getAll();
}
