package com.example.adeeliftikhar.ticktalk;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.ticktalk.Model.MessagesDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.adeeliftikhar.ticktalk.GetTimeAgo.getTimeSeenAgo;

public class ChatActivity extends AppCompatActivity {

    ImageView imageViewChatPerson;
    TextView textViewChatPersonName, textViewChatPersonStatus;
    ImageView imageViewPlusIcon, imageViewSendMessage;
    EditText editTextEnterMessage;
    RecyclerView chatRecyclerView;

    DatabaseReference dbUserRef;
    DatabaseReference dbChatRef;
    DatabaseReference dbMessageRef;
    String comingUserId;
    FirebaseAuth mAuth;
    String currentUser;

    private final List<MessagesDataModel> myMessagesList = new ArrayList<>();
    private LinearLayoutManager myLinearLayoutManager;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
//          Now Enabled Arrow Button...
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            imageViewChatPerson = findViewById(R.id.image_view_chat_person);
            textViewChatPersonName = findViewById(R.id.text_view_chat_person_name);
            textViewChatPersonStatus = findViewById(R.id.text_view_chat_person_status);

            editTextEnterMessage = findViewById(R.id.edit_text_enter_message);
            imageViewPlusIcon = findViewById(R.id.image_view_plus_icon);
            imageViewSendMessage = findViewById(R.id.image_view_send_message);

            messageAdapter = new MessageAdapter(myMessagesList);
            chatRecyclerView = findViewById(R.id.chat_recycler_view);
            myLinearLayoutManager = new LinearLayoutManager(ChatActivity.this);
            chatRecyclerView.setHasFixedSize(true);
            chatRecyclerView.setLayoutManager(myLinearLayoutManager);
            chatRecyclerView.setAdapter(messageAdapter);

            dbChatRef = FirebaseDatabase.getInstance().getReference();
            dbMessageRef = FirebaseDatabase.getInstance().getReference();

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser().getUid();
            Intent intent = getIntent();
            comingUserId = intent.getStringExtra("UserId");
            comingUserId = getIntent().getStringExtra("UserId");
            if (!comingUserId.equals(null)) {
                dbUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(comingUserId);
                retrieveData();
            }
        }
        loadMessages();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void retrieveData() {
        dbUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String chatUserName = dataSnapshot.child("name").getValue().toString();
                String chatUserOnline = dataSnapshot.child("online").getValue().toString();
                String chatUserImage = dataSnapshot.child("thumb_image").getValue().toString();
                textViewChatPersonName.setText(chatUserName);

                if (chatUserOnline.equals("true")) {
                    textViewChatPersonStatus.setText("Online");
                } else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(chatUserOnline);
                    String lastSeenTime = getTimeAgo.getTimeSeenAgo(lastTime, getApplicationContext());
                    textViewChatPersonStatus.setText(lastSeenTime);
                }

                if (!chatUserImage.equals("Default")) {
                    Picasso.get().load(chatUserImage).resize(360, 300).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.bg1).
                            into(imageViewChatPerson, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(comingUserId)) {
                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + currentUser + "/" + comingUserId, chatAddMap);
                    chatUserMap.put("Chat/" + comingUserId + "/" + currentUser, chatAddMap);

                    dbChatRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.d("Chat Log", databaseError.getMessage());
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message = editTextEnterMessage.getText().toString();
//        TetxUtils class is used to perform an action on the Strings.
        if(!TextUtils.isEmpty(message)){
            String currentUserRef = "Message/"+ currentUser + "/" + comingUserId;
            String comingUserRef = "Message/"+ comingUserId + "/" + currentUser;

            DatabaseReference userMessagePushRef = dbMessageRef.child("Message").child(currentUser).child(comingUserRef).push();
            String pushId = userMessagePushRef.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("timestamp", ServerValue.TIMESTAMP);
            messageMap.put("from", currentUser);
            messageMap.put("to", comingUserId);

            Map userMessageMap = new HashMap();
            userMessageMap.put(currentUserRef + "/" + pushId, messageMap);
            userMessageMap.put(comingUserRef + "/" + pushId, messageMap);

            editTextEnterMessage.setText("");

            dbMessageRef.updateChildren(userMessageMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError != null){
                        Log.d("ChatLog", databaseError.getMessage());
                    }
                }
            });
        }
    }

    private void loadMessages() {
        dbMessageRef.child("Message").child(currentUser).child(comingUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessagesDataModel messagesDataModel = dataSnapshot.getValue(MessagesDataModel.class);
                myMessagesList.add(messagesDataModel);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}