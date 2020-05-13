package com.example.samplegroupchatapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplegroupchatapplication.R;
import com.example.samplegroupchatapplication.model.fireStoreChatMessage;
import com.example.samplegroupchatapplication.model.fireStoreChatUser;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageHolder> {

    private ArrayList<fireStoreChatMessage> mMessageList;
    private fireStoreChatUser mUser;
    private Context context;


    public ChatMessageAdapter(ArrayList<fireStoreChatMessage> mMessageList, fireStoreChatUser mUser, Context context)
    {
        this.mMessageList = mMessageList;
        this.mUser = mUser;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatMessageAdapter.ChatMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_list_item, parent, false);
        final ChatMessageHolder holder= new ChatMessageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ChatMessageHolder holder, int position) {

        if(mUser.getUserName().equalsIgnoreCase(mMessageList.get(position).getUser().getUserName()))
            holder.mUserView.setTextColor(ContextCompat.getColor(context, R.color.colorUser));
        else
            holder.mUserView.setTextColor(ContextCompat.getColor(context, R.color.colorSender));
        holder.mMessageView.setText(mMessageList.get(position).getMessage());
        holder.mUserView.setText(mMessageList.get(position).getUser().getUserName());
    }

    @Override
    public int getItemCount() {

        if(mMessageList!=null)
            return mMessageList.size();
        else
            return 0;

    }

    public class ChatMessageHolder extends RecyclerView.ViewHolder {
        TextView mMessageView, mUserView;
        public ChatMessageHolder(@NonNull View itemView) {
            super(itemView);
            mMessageView = itemView.findViewById(R.id.chatMessageText);
            mUserView = itemView.findViewById(R.id.chatMessageUserName);
        }
    }
}
