package com.example.samplegroupchatapplication.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.samplegroupchatapplication.R;
import com.example.samplegroupchatapplication.adapters.ChatMessageAdapter;
import com.example.samplegroupchatapplication.model.fireStoreChatMessage;
import com.example.samplegroupchatapplication.model.fireStoreChatRoom;
import com.example.samplegroupchatapplication.model.fireStoreChatUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    private final String TAG = "Chat Activity";

    private FirebaseFirestore firebaseDB;
    private Date lastFetchedTimeStamp;


    private ListenerRegistration mChatMessageEventListener;
    private fireStoreChatRoom mChatRoom;
    private fireStoreChatUser mUser;
    ArrayList<fireStoreChatMessage> mMessageList = new ArrayList<>();
    private Set<String> mMessageIds = new HashSet<>();

    private EditText mMessageText;
    private ImageButton mSendButton;
    private RecyclerView mMessageRecyclerView ;
    private ChatMessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Initial Set Up Functions
        getIntentData();
        setUser();
        firebaseDB = FirebaseFirestore.getInstance();
        //Set Up Views
        mMessageText = findViewById(R.id.chatMessageToSend);
        mSendButton = findViewById(R.id.sendMessageButton);
        mMessageRecyclerView = findViewById(R.id.chatMessageRecyclerView);
        //Setting up the recycler View
        setUpRecycler();
        //adding send functionality
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mMessageText.getText().toString());
            }
        });

    }

    //LifeCycle elements
    @Override
    protected void onResume() {
        super.onResume();
        getAllMessages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChatMessageEventListener != null){
            mChatMessageEventListener.remove();
        }
    }

    void setUpRecycler()
    {
        messageAdapter = new ChatMessageAdapter(mMessageList,mUser,this);
        mMessageRecyclerView.setAdapter(messageAdapter);
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMessageRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom<oldBottom)
                    mMessageRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mMessageList.size() > 0){
                                mMessageRecyclerView.smoothScrollToPosition(
                                        mMessageRecyclerView.getAdapter().getItemCount() - 1);
                            }
                        }
                    },100);
            }
        });
    }

    void setUser()
    {
        String emailID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        assert emailID != null;
        String userName = emailID.trim().split("@")[0];
        mUser = new fireStoreChatUser(emailID,userName,userID);
        Toast.makeText(this,"Created user :- " + mUser.getUserName(),Toast.LENGTH_SHORT).show();
    }



    public void getIntentData()
    {
        if(getIntent().hasExtra(getString(R.string.intent_chatroom_name)) &&
            getIntent().hasExtra(getString(R.string.intent_chatroom_id)))
        {
            String chatRoomName = getIntent().getStringExtra(getString(R.string.intent_chatroom_name));
            int chatRoomID = getIntent().getIntExtra(getString(R.string.intent_chatroom_id), -1);
            mChatRoom = new fireStoreChatRoom(chatRoomName,Integer.toString(chatRoomID));
            getSupportActionBar().setTitle(chatRoomName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);


        }
    }

    private void getAllMessages()
    {
        CollectionReference mCollectionRefrence = firebaseDB.collection("ChatRooms")
                .document(mChatRoom.getChatRoomID())
                .collection("Chat Messages");

         //TODO :- add a way to compare using timestamp to check the list
         mChatMessageEventListener = mCollectionRefrence
                 .orderBy("timestamp", Query.Direction.ASCENDING)
                 .addSnapshotListener(new EventListener<QuerySnapshot>() {
                     @Override
                     public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                         if (e != null) {
                             Log.w(TAG, "listen:error", e);
                             return;
                         }
                         Log.i(TAG, "Reading the Messages");
                         if(queryDocumentSnapshots != null){
                             for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                Log.i(TAG,"populating the list ");
                                 fireStoreChatMessage message = doc.toObject(fireStoreChatMessage.class);
                                 if(!mMessageIds.contains(message.getMessageID())){
                                     mMessageIds.add(message.getMessageID());
                                     mMessageList.add(message) ;
                                    mMessageRecyclerView.smoothScrollToPosition(mMessageList.size() - 1);
                                 }

                             }
                             messageAdapter.notifyDataSetChanged();
                         }
                         else
                         {
                             Log.e(TAG, "documents are null");
                         }

                     }
                 });
    }


    private void sendMessage(String message)
    {
        if(!message.trim().equals("") && mUser!=null )
        {
            message = message.replaceAll(System.getProperty("line.separator"), "");
            DocumentReference mDocRef  = firebaseDB.collection("ChatRooms")
                                            .document(mChatRoom.getChatRoomID())
                                            .collection("Chat Messages")
                                            .document();
            fireStoreChatMessage mChatMessage = new fireStoreChatMessage();
            mChatMessage.setUser(mUser);
            mChatMessage.setMessage(message);
            mChatMessage.setMessageID(mDocRef.getId());

            mDocRef.set(mChatMessage)
                    .addOnSuccessListener(aVoid -> {
                       Log.i(TAG,"Message Written Successfully" );
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG,"Failure Writing Message",e);
                    });


        }
        else
        {
            if(message.trim().equals("")) {
                Log.i(TAG, "Nothing to send");
                mMessageText.setError("empty");
            }
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }





}
