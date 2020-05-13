package com.example.samplegroupchatapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.samplegroupchatapplication.R;
import com.example.samplegroupchatapplication.adapters.ChatRoomAdapter;
import com.example.samplegroupchatapplication.database.ChatRoom;
import com.example.samplegroupchatapplication.utils.ItemClickListener;
import com.example.samplegroupchatapplication.viewModel.ChatRoomViewModel;

import java.util.List;

public class ChatRoomListActivity extends AppCompatActivity {
    private static final  String TAG = "Chat Room List Activity";
    private ChatRoomViewModel mChatRoomViewModel;
    ChatRoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);
        mChatRoomViewModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        getSupportActionBar().setTitle("Chat Rooms");
        getSupportActionBar().setHomeButtonEnabled(false);

        ItemClickListener listener = new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //open chat

                openChatActivity(position);

            }
        };

        RecyclerView recyclerView = findViewById(R.id.chatRoomRecyclerView);
        adapter = new ChatRoomAdapter(this, listener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRoomViewModel.getAllRooms().observe(this, new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                adapter.setRooms(chatRooms);
            }
        });




    }

    private void openChatActivity(int position)
    {
        Log.i(TAG, "Opening the chat Room at position:- " + position);
        try {

            ChatRoom mRoom = mChatRoomViewModel.getAllRooms().getValue().get(position);
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(getString(R.string.intent_chatroom_name), mRoom.getChatRoomName());
            intent.putExtra(getString(R.string.intent_chatroom_id), mRoom.getChatRoomID());
            Log.d(TAG, "Opening successful");
            startActivity(intent);

        } catch (NullPointerException e)
        {
            Toast.makeText(this,"not A valid Selection",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Couldn't Open the chat Room");
        }

        //Toast.makeText(this,mChatRoomViewModel.getAllRooms().getValue().get(position).getChatRoomName(),Toast.LENGTH_SHORT).show();
    }
}
