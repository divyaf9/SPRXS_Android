package com.divya.sprxs.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterChat;
import com.divya.sprxs.model.ChatMessage;
import com.divya.sprxs.model.ChatUserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatDisplayActivity extends AppCompatActivity {


    private RecyclerView recyclerViewChat;
    private DataAdapterChat dataAdapterChat;
    private String ownerFirebaseUID, chatLocation, name, message, collabFirebaseID;
    private List<String> chatMessageList = new ArrayList<>();
    private List<String> collabID = new ArrayList<>();
    private List<String> collabFirebaseUID = new ArrayList<>();

    private List<ChatUserDetails> chatUsersDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_display);


        setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerViewChat = findViewById(R.id.recycler_view_chat);

        if (chatUsersDetailsList != null && chatUsersDetailsList.size() > 0) {
            chatUsersDetailsList.clear();
            dataAdapterChat.notifyDataSetChanged();

        }

        displayUser(new FirebaseCallback() {
            @Override
            public void onCallBack(List<ChatUserDetails> chatUsersDetailsList) {
                recyclerViewChat.setHasFixedSize(false);
                recyclerViewChat.setNestedScrollingEnabled(false);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatDisplayActivity.this);
                recyclerViewChat.setLayoutManager(layoutManager);
                dataAdapterChat = new DataAdapterChat(ChatDisplayActivity.this, chatUsersDetailsList, getApplicationContext());
                recyclerViewChat.setAdapter(dataAdapterChat);
                dataAdapterChat.notifyDataSetChanged();
                dataAdapterChat.notifyItemChanged(chatUsersDetailsList.size() - 1);


            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void displayUser(final FirebaseCallback firebaseCallback) {

        ownerFirebaseUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("users").child(ownerFirebaseUID).child("conversations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        for (DataSnapshot preSnapshpot : snapshot.getChildren()) {

                            collabFirebaseUID.add(snapshot.getKey());

                            chatLocation = snapshot.child("location").getValue().toString();

                            databaseReference.child("conversations").child(chatLocation).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    final ChatUserDetails chatUserDetails = new ChatUserDetails();


                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {


                                            ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);

                                            message = chatMessage.getContent();

                                            if (ownerFirebaseUID.equals(chatMessage.getFromID())) {
                                                collabFirebaseID = chatMessage.getToID();

                                            } else {
                                                collabFirebaseID = chatMessage.getFromID();
//                                                message = chatMessage.getContent();
                                            }

                                        }

                                        collabID.add(collabFirebaseID);
                                        chatMessageList.add(message);
                                        chatUserDetails.setChatMessage(chatMessageList);
                                        chatUserDetails.setCollabFirebaseUID(collabFirebaseID);

                                        databaseReference.child("users").child(collabFirebaseID).child("credentials").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.exists()) {

                                                    name = dataSnapshot.child("firstName").getValue().toString() + " " + dataSnapshot.child("lastName").getValue().toString();
                                                    chatUserDetails.setFullName(name);

                                                    chatUsersDetailsList.add(chatUserDetails);
                                                    firebaseCallback.onCallBack(chatUsersDetailsList);

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }


    private interface FirebaseCallback {

        void onCallBack(List<ChatUserDetails> chatUsersDetailsList);
    }
}
