package com.example.android.tripmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.tripmanager.Adapter.MessageAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    List<ChatMessage> messages;

    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        database=FirebaseDatabase.getInstance();
        rvMessage=findViewById(R.id.rvMessage);
        etMessage=findViewById(R.id.etmessage);
        imgSend=findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etMessage.getText().toString())) {
                    ChatMessage message = new ChatMessage(etMessage.getText().toString(), Common.currentUser.getUsername());
                    etMessage.setText("");
                    messagedb.push().setValue(message);
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot send blank message", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView img=findViewById(R.id.img3);
        Glide.with(this).load("https://images.pexels.com/photos/1590549/pexels-photo-1590549.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").into(img);
        img.setScaleType(ImageView.ScaleType.FIT_XY);

    }

    @Override
    public void onClick(View v) {
        if (v == imgSend) {
            if (!TextUtils.isEmpty(etMessage.getText().toString())) {
                ChatMessage message = new ChatMessage(etMessage.getText().toString(), Common.currentUser.getUsername());
                etMessage.setText("");
                messagedb.push().setValue(message);
            } else {
                Toast.makeText(getApplicationContext(), "Cannot send blank message", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        messages=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagedb=database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                message.setKey(dataSnapshot.getKey());
                List<ChatMessage> newMessages=new ArrayList<ChatMessage>();
                for(ChatMessage m:messages) {
                    if (m.getKey().equals(message.getKey()))
                        newMessages.add(message);
                    else
                        newMessages.add(m);
                }
                messages=newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatMessage message=dataSnapshot.getValue(ChatMessage.class);
                message.setKey(dataSnapshot.getKey());
                List<ChatMessage> newMessages=new ArrayList<ChatMessage>();
                for(ChatMessage m:messages) {
                    if(!m.getKey().equals(message.getKey()))
                        newMessages.add(m);
                }
                messages=newMessages;
                displayMessages(messages);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayMessages(List<ChatMessage> messages) {
        rvMessage.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        messageAdapter=new MessageAdapter(GroupChatActivity.this,messages,messagedb);
        rvMessage.setAdapter(messageAdapter);
    }
}
