package com.divya.sprxs.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.divya.sprxs.activity.IdeaDetailsActivity;
import com.divya.sprxs.activity.LoggedIn;
import com.divya.sprxs.fragment.NoIdeasFragment;
import com.divya.sprxs.model.MyIdeasResponse;
import com.divya.sprxs.model.MyIdeasSummaryResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<MyIdeasSummaryResponse> myIdeasSummaryResponse;
    private CardView cardView;
    private Context context;


    public DataAdapter(FragmentActivity fragmentActivity, List<MyIdeasSummaryResponse> myIdeasSummaryResponse, Context context) {
        this.myIdeasSummaryResponse = myIdeasSummaryResponse;
        this.context = context;
    }


    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item, parent, false);
        cardView = v.findViewById(R.id.card_view);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final DataAdapter.ViewHolder holder, final int position) {
        final String IdeaId = myIdeasSummaryResponse.get(position).getIdea_id();
        final Long Time= myIdeasSummaryResponse.get(position).getDate_created_timestamp();
        holder.ideaNameText.setText(myIdeasSummaryResponse.get(position).getIdea_name().substring(0,  Math.min(myIdeasSummaryResponse.get(position).getIdea_name().length(), 15)));
        holder.ideaIdText.setText("#"+myIdeasSummaryResponse.get(position).getIdea_id());
        holder.collaboratorText.setText(myIdeasSummaryResponse.get(position).getNo_of_collaborators());
        holder.equityText.setText(myIdeasSummaryResponse.get(position).getTokens_owned());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IdeaDetailsActivity.class);
                intent.putExtra("myList",IdeaId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return (myIdeasSummaryResponse == null) ? 0 : myIdeasSummaryResponse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ideaNameText, ideaIdText, collaboratorText, equityText;
        private ImageView collaboratorImage, equityimage;

        public ViewHolder(View view) {
            super(view);
            ideaNameText = view.findViewById(R.id.ideaNameText);
            ideaIdText = view.findViewById(R.id.ideaIdText);
            collaboratorText = view.findViewById(R.id.collaboratorText);
            equityText = view.findViewById(R.id.equityText);
            collaboratorImage = view.findViewById(R.id.collaboratorImage);
            equityimage = view.findViewById(R.id.equityImage);
//              if(myIdeasSummaryResponse==null){
//                  Intent intent = new Intent(view.getContext(),LoggedIn.class);
//                  context.startActivity(intent);
//              }
        }
    }
}
