package com.divya.sprxs.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterMessages;
import com.divya.sprxs.model.ChatMessage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private String chatName, ownerFirebaseUID, collabFirebaseUID, message, collabUID, chatLocation;
    private EditText chatEditText;
    private ImageButton sendButton, attachFileChat;
    private RecyclerView recyclerViewMessages;
    private List<ChatMessage> chatMessageList = new ArrayList<>();
    List<String> imageFile = new ArrayList<>();
    private DataAdapterMessages dataAdapterMessages;
    private static final int PICK_FROM_GALLERY = 1;
    private String attachmentFile = null;
    private String type, fileName;
    private Uri uri = null;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private String intentType, imageMessage;
    private Boolean Flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatName = getIntent().getStringExtra("chatName");

        collabFirebaseUID = getIntent().getStringExtra("collabFirebaseUID");

        this.setTitle(chatName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        chatEditText = findViewById(R.id.chatEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(this);
        attachFileChat = findViewById(R.id.attachFileChat);
        attachFileChat.setOnClickListener(this);
        recyclerViewMessages = findViewById(R.id.recycler_view_messages);

        ownerFirebaseUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerViewMessages.setHasFixedSize(false);
        recyclerViewMessages.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);

        displayMessages(ownerFirebaseUID, collabFirebaseUID);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sendButton:
                message = chatEditText.getText().toString().trim();

                if (!message.equals("")) {

                    if (Flag.equals(false)) {
                        sendMessage(ownerFirebaseUID, collabFirebaseUID, message);
                    } else {
                        String fileName = message;
                        String[] splitParts = fileName.split(Pattern.quote("."));
                        String extension = splitParts[1];
                        String[] photoType = new String[]{"jpg", "png", "gif", "jpeg"};
                        String[] fileType = new String[]{"doc", "docx", "html", "htm", "odt", "pdf", "xls", "xlsx", "ods", "ppt", "pptx", "txt"};
                        String[] textType = new String[]{"jpg", "png", "gif", "jpeg", "doc", "docx", "html", "htm", "odt", "pdf", "xls", "xlsx", "ods", "ppt", "pptx", "txt"};

                        for (String imageExtension : photoType) {
                            if (extension.contentEquals(imageExtension)) {
                                sendImage(ownerFirebaseUID, collabFirebaseUID, message);
                            }
                        }

                        for (String fileExtension : fileType) {
                            if (extension.contentEquals(fileExtension)) {

                                sendFile(ownerFirebaseUID, collabFirebaseUID, message);
                            }
                        }

                    }
                } else {
                    Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

                chatEditText.setText("");
                break;

            case R.id.attachFileChat:
                attachFile();
                break;
        }
    }


    private void attachFile() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FROM_GALLERY);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    attachmentFile = String.valueOf(uri);
                    ContentResolver cR = getApplicationContext().getContentResolver();
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    type = mime.getExtensionFromMimeType(cR.getType(uri));
                    Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    chatEditText.setText(cursor.getString(nameIndex));
                    Flag = true;
                    cursor.close();
                }
                break;

        }
    }


    private void displayMessages(final String fromUID, final String toUID) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("users").child(ownerFirebaseUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.child("conversations").getChildren()) {

                        collabUID = snapshot.getKey().toString();

                        chatLocation = snapshot.child("location").getValue().toString();

                        databaseReference.child("conversations").child(chatLocation).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshop : dataSnapshot.getChildren()) {

                                    ChatMessage chatMessage = postSnapshop.getValue(ChatMessage.class);

                                    if (chatMessage.getToID().equals(fromUID) && chatMessage.getFromID().equals(toUID) ||
                                            chatMessage.getToID().equals(toUID) && chatMessage.getFromID().equals(fromUID)) {

                                        chatMessageList.add(chatMessage);

                                    }

                                    dataAdapterMessages = new DataAdapterMessages(ChatActivity.this, chatMessageList, ChatActivity.this);
                                    recyclerViewMessages.setAdapter(dataAdapterMessages);
                                    dataAdapterMessages.notifyDataSetChanged();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(final String senderFirebaseId, String receiverFirebaseId, String message) {

        try {

            if (receiverFirebaseId != "") {
                receiverFirebaseId = collabFirebaseUID;
            }
            long time = System.currentTimeMillis();

            final HashMap<String, Object> sendNewMesaageMap = new HashMap<>();
            sendNewMesaageMap.put("type", "text");
            sendNewMesaageMap.put("content", message);
            sendNewMesaageMap.put("fromID", ownerFirebaseUID);
            sendNewMesaageMap.put("toID", receiverFirebaseId);
            sendNewMesaageMap.put("timestamp", time);
            sendNewMesaageMap.put("isRead", false);

            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            final String finalReceiverFirebaseId = receiverFirebaseId;
            final String finalReceiverFirebaseId1 = receiverFirebaseId;

            try {
                databaseReference.child("users").child(ownerFirebaseUID).child("conversations").child(receiverFirebaseId).addListenerForSingleValueEvent(new ValueEventListener() {  //Check Convo tree
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            //Tree exists.
                            HashMap<String, Object> oldLoc = new HashMap<String, Object>();
                            oldLoc.put("Snapshot", dataSnapshot.getValue());
                            oldLoc.put("newLoc", oldLoc.get("Snapshot").toString().replace("{location=", ""));
                            String finalLoc = oldLoc.get("newLoc").toString().replace("}", "");
                            DatabaseReference oldTree = firebaseDatabase.getReference();
                            oldTree.child("conversations").child(finalLoc).push().setValue(sendNewMesaageMap);
                            displayMessages(ownerFirebaseUID, collabFirebaseUID);

                        } else {
                            //Tree doesn't exist.
                            DatabaseReference messLoc = firebaseDatabase.getReference();
                            String messLocKey = messLoc.push().getKey();
                            messLoc.child("conversations").child(messLocKey).push().setValue(sendNewMesaageMap);
                            HashMap<String, Object> simpleDict = new HashMap<>();
                            simpleDict.put("location", messLocKey);

                            //double update:
                            DatabaseReference firstCopy = firebaseDatabase.getReference();
                            firstCopy.child("users").child(senderFirebaseId).child("conversations").child(finalReceiverFirebaseId1).updateChildren(simpleDict);

                            DatabaseReference secondCopy = firebaseDatabase.getReference();
                            secondCopy.child("users").child(finalReceiverFirebaseId1).child("conversations").child(senderFirebaseId).updateChildren(simpleDict);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception ex) {
                Log.e("DBRef error", ex.getMessage());
                System.out.println("DBREF:::::::::::::::::::::::::::::::::::::::::::::::::::::::" + databaseReference);
            }


        } catch (Exception ex) {
            Log.e("ExceptionChat", ex.getMessage());
        }


    }


    private void sendImage(final String senderFirebaseId, String receiverFirebaseId, final String message) {

        try {

            if (receiverFirebaseId != "") {
                receiverFirebaseId = collabFirebaseUID;
            }
            long time = System.currentTimeMillis();


            final HashMap<String, Object> sendNewMesaageMap = new HashMap<>();
            sendNewMesaageMap.put("type", "photo");
            sendNewMesaageMap.put("content", message);
            sendNewMesaageMap.put("fromID", ownerFirebaseUID);
            sendNewMesaageMap.put("toID", receiverFirebaseId);
            sendNewMesaageMap.put("timestamp", time);
            sendNewMesaageMap.put("isRead", false);

            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            final String finalReceiverFirebaseId = receiverFirebaseId;
            final String finalReceiverFirebaseId1 = receiverFirebaseId;

            try {
                databaseReference.child("users").child(ownerFirebaseUID).child("conversations").child(receiverFirebaseId).addListenerForSingleValueEvent(new ValueEventListener() {  //Check Convo tree
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            //Tree exists.
                            HashMap<String, Object> oldLoc = new HashMap<String, Object>();
                            oldLoc.put("Snapshot", dataSnapshot.getValue());
                            oldLoc.put("newLoc", oldLoc.get("Snapshot").toString().replace("{location=", ""));
                            String finalLoc = oldLoc.get("newLoc").toString().replace("}", "");
                            DatabaseReference oldTree = firebaseDatabase.getReference();
                            oldTree.child("conversations").child(finalLoc).push().setValue(sendNewMesaageMap);
                            imageStorage(message);
                            displayMessages(ownerFirebaseUID, collabFirebaseUID);

                        } else {
                            //Tree doesn't exist.
                            DatabaseReference messLoc = firebaseDatabase.getReference();
                            String messLocKey = messLoc.push().getKey();
                            messLoc.child("conversations").child(messLocKey).push().setValue(sendNewMesaageMap);
                            HashMap<String, Object> simpleDict = new HashMap<>();
                            simpleDict.put("location", messLocKey);

                            //double update:
                            DatabaseReference firstCopy = firebaseDatabase.getReference();
                            firstCopy.child("users").child(senderFirebaseId).child("conversations").child(finalReceiverFirebaseId1).updateChildren(simpleDict);

                            DatabaseReference secondCopy = firebaseDatabase.getReference();
                            secondCopy.child("users").child(finalReceiverFirebaseId1).child("conversations").child(senderFirebaseId).updateChildren(simpleDict);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception ex) {
                Log.e("DBRef error", ex.getMessage());
                System.out.println("DBREF:::::::::::::::::::::::::::::::::::::::::::::::::::::::" + databaseReference);
            }


        } catch (Exception ex) {
            Log.e("ExceptionChat", ex.getMessage());
        }

    }


    private void imageStorage(String message) {

        final String AllUserFiles = "AllUsersFileData";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();

        StorageReference mStorageRef;

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child(AllUserFiles).child(UID).child(message);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        displayMessages(ownerFirebaseUID, collabFirebaseUID);

                        // Get a URL to the uploaded content
                        Log.d("IMAGE UPLOAD", "SUCCESS");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Log.e("FAILED TO UPLOAD FILE ", "TO FIREBASE");
                    }
                });

    }


    private void sendFile(final String senderFirebaseId, String receiverFirebaseId, final String message) {

        try {

            if (receiverFirebaseId != "") {
                receiverFirebaseId = collabFirebaseUID;
            }
            long time = System.currentTimeMillis();


            final HashMap<String, Object> sendNewMesaageMap = new HashMap<>();
            sendNewMesaageMap.put("type", "file");
            sendNewMesaageMap.put("content", message);
            sendNewMesaageMap.put("fromID", ownerFirebaseUID);
            sendNewMesaageMap.put("toID", receiverFirebaseId);
            sendNewMesaageMap.put("timestamp", time);
            sendNewMesaageMap.put("isRead", false);

            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            final String finalReceiverFirebaseId = receiverFirebaseId;
            final String finalReceiverFirebaseId1 = receiverFirebaseId;

            try {
                databaseReference.child("users").child(ownerFirebaseUID).child("conversations").child(receiverFirebaseId).addListenerForSingleValueEvent(new ValueEventListener() {  //Check Convo tree
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            //Tree exists.
                            HashMap<String, Object> oldLoc = new HashMap<String, Object>();
                            oldLoc.put("Snapshot", dataSnapshot.getValue());
                            oldLoc.put("newLoc", oldLoc.get("Snapshot").toString().replace("{location=", ""));
                            String finalLoc = oldLoc.get("newLoc").toString().replace("}", "");
                            DatabaseReference oldTree = firebaseDatabase.getReference();
                            oldTree.child("conversations").child(finalLoc).push().setValue(sendNewMesaageMap);
                            imageStorage(message);
                            displayMessages(ownerFirebaseUID, collabFirebaseUID);

                        } else {
                            //Tree doesn't exist.
                            DatabaseReference messLoc = firebaseDatabase.getReference();
                            String messLocKey = messLoc.push().getKey();
                            messLoc.child("conversations").child(messLocKey).push().setValue(sendNewMesaageMap);
                            HashMap<String, Object> simpleDict = new HashMap<>();
                            simpleDict.put("location", messLocKey);

                            //double update:
                            DatabaseReference firstCopy = firebaseDatabase.getReference();
                            firstCopy.child("users").child(senderFirebaseId).child("conversations").child(finalReceiverFirebaseId1).updateChildren(simpleDict);

                            DatabaseReference secondCopy = firebaseDatabase.getReference();
                            secondCopy.child("users").child(finalReceiverFirebaseId1).child("conversations").child(senderFirebaseId).updateChildren(simpleDict);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception ex) {
                Log.e("DBRef error", ex.getMessage());
                System.out.println("DBREF:::::::::::::::::::::::::::::::::::::::::::::::::::::::" + databaseReference);
            }


        } catch (Exception ex) {
            Log.e("ExceptionChat", ex.getMessage());
        }
    }

}
