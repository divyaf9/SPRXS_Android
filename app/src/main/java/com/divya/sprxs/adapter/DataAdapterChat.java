package com.divya.sprxs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.ChatActivity;
import com.divya.sprxs.model.ChatUserDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DataAdapterChat extends RecyclerView.Adapter<DataAdapterChat.ViewHolder> {

    private List<ChatUserDetails> chatUserDetails;
    private CardView cardViewChat;
    private Context context;

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = firebaseDatabase.getReference();


    public DataAdapterChat(FragmentActivity fragmentActivity, List<ChatUserDetails> chatUserDetails, Context context) {
        this.chatUserDetails = chatUserDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_chat, parent, false);
        cardViewChat = v.findViewById(R.id.card_view_chat);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterChat.ViewHolder holder, final int position) {
        holder.chatNameTextView.setText(chatUserDetails.get(position).getFullName());
        holder.chatMessage.setText((chatUserDetails.get(position).getChatMessage().get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("chatName", chatUserDetails.get(position).getFullName());
                intent.putExtra("collabFirebaseUID", chatUserDetails.get(position).getCollabFirebaseUID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (chatUserDetails == null) ? 0 : chatUserDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView chatMessage, chatNameTextView;
        private ImageView chatImageView;


        public ViewHolder(View view) {
            super(view);
            chatMessage = view.findViewById(R.id.chatMessage);
            chatNameTextView = view.findViewById(R.id.chatNameTextView);
            chatImageView = view.findViewById(R.id.chatImageView);
        }
    }
}
