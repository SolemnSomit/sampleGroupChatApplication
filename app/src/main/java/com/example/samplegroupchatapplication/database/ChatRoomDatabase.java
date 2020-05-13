package com.example.samplegroupchatapplication.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ChatRoom.class}, version = 1, exportSchema = false)
public abstract class ChatRoomDatabase extends RoomDatabase {

    public abstract ChatRoomDao chatRoomDao();

    private static volatile ChatRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ChatRoomDatabase getDatabase(final Context context)
    {
        if(INSTANCE==null)
        {
            synchronized (ChatRoomDatabase.class)
            {
                if(INSTANCE==null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ChatRoomDatabase.class, "ChatRoomList").addCallback(roomDatabaseCallback).build();
                }
            }

        }
        return INSTANCE;

    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db)
        {
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    ChatRoomDao dao = INSTANCE.chatRoomDao();
                    ChatRoom room = new ChatRoom(1001, "First Chat Room");
                    dao.insert(room);
                }
            });
        }

    };
}
