package com.example.samplegroupchatapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplegroupchatapplication.R;
import com.example.samplegroupchatapplication.database.ChatRoom;
import com.example.samplegroupchatapplication.utils.ItemClickListener;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder> {


    private ItemClickListener mListener;
    private final LayoutInflater mInflater;
    private List<ChatRoom> mChatRoomList;//Cached Copy

    public ChatRoomAdapter(Context context, ItemClickListener listener)
    {
        mInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.chat_room_list_item,parent,false);
        return new ChatRoomHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        if(mChatRoomList!=null)
        {
            ChatRoom current = mChatRoomList.get(position);
            holder.chatRoomTextView.setText(current.getChatRoomName());
        }
        else
        {
            holder.chatRoomTextView.setText("No Chat Rooms");
        }
    }

    public void setRooms(List<ChatRoom> rooms)
    {
        this.mChatRoomList = rooms;
        notifyDataSetChanged();
    }

    public ChatRoom getItemAt(int position)
    {
        return mChatRoomList.get(position);
    }
    @Override
    public int getItemCount() {
        if (mChatRoomList!=null)
            return mChatRoomList.size();
        else
            return 0;
    }


    class ChatRoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ItemClickListener mListener;
        private TextView chatRoomTextView;
        private ChatRoomHolder(View itemview, ItemClickListener listener)
        {
            super(itemview);
            chatRoomTextView = itemView.findViewById(R.id.chatRoomName);
            mListener = listener;
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }



}
