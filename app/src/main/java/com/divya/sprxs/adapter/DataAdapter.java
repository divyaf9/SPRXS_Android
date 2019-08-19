package com.divya.sprxs.adapter;

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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.divya.sprxs.R;
import com.divya.sprxs.activity.EditIdeaActivity;
import com.divya.sprxs.activity.IdeaDetailsActivity;
import com.divya.sprxs.activity.PublishIdeaActivity;
import com.divya.sprxs.fragment.IdeaDetailsFragment;
import com.divya.sprxs.model.MyIdeasSummaryResponse;

import java.util.ArrayList;
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
        final String IdeaName= myIdeasSummaryResponse.get(position).getIdea_name();
        final String IdeaDescription= myIdeasSummaryResponse.get(position).getIdeaDescription();
        holder.ideaNameText.setText(myIdeasSummaryResponse.get(position).getIdea_name());
//                .substring(0,  Math.min(myIdeasSummaryResponse.get(position).getIdea_name().length(), 15)));
//        holder.ideaIdText.setText("#"+myIdeasSummaryResponse.get(position).getIdea_id());
        holder.collaboratorText.setText(myIdeasSummaryResponse.get(position).getNo_of_collaborators());
        holder.dateText.setText(myIdeasSummaryResponse.get(position).getDate_created_android());
        holder.equityText.setText(myIdeasSummaryResponse.get(position).getTokens_owned());
        if(myIdeasSummaryResponse.get(position).isIdea_status()==true) {
            holder.statusImageView.setBackgroundResource(R.drawable.ic_public_black_24dp);
        }else
        {
            holder.statusImageView.setBackgroundResource(R.drawable.ic_vpn_lock_black_24dp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IdeaDetailsActivity.class);
                intent.putExtra("myList",IdeaId);
//                intent.putExtra("myListIdeaName",IdeaName);
//                intent.putExtra("myListIdeaDesc",IdeaDescription);
                context.startActivity(intent);
            }
        });

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.bottom_wraper));

        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.close();
                Intent intent = new Intent(v.getContext(), IdeaDetailsActivity.class);
                intent.putExtra("myList",IdeaId);
                intent.putExtra("myListIdeaName",IdeaName);
                context.startActivity(intent);
            }
        });

        holder.Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.swipeLayout.close();
                Intent intent = new Intent(view.getContext(), PublishIdeaActivity.class);
                intent.putExtra("myList",IdeaId);
                intent.putExtra("myListIdeaName",IdeaName);
                intent.putExtra("myListIdeaDesc",IdeaDescription);
                context.startActivity(intent);

            }
        });

        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.swipeLayout.close();
                Intent intent = new Intent(v.getContext(), EditIdeaActivity.class);
                intent.putExtra("myList",IdeaId);
                intent.putExtra("myListIdeaName",IdeaName);
                intent.putExtra("myListIdeaDesc",IdeaDescription);
                context.startActivity(intent);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {

        return (myIdeasSummaryResponse == null) ? 0 : myIdeasSummaryResponse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ideaNameText, ideaIdText, collaboratorText, equityText,dateText;
        private ImageView statusImageView;
        public SwipeLayout swipeLayout;
        public TextView Edit;
        public TextView Share;
        public ViewHolder(View view) {
            super(view);
            ideaNameText = view.findViewById(R.id.ideaNameText);
////            ideaIdText = view.findViewById(R.id.ideaIdText);
            collaboratorText = view.findViewById(R.id.collaboratorText);
            dateText = view.findViewById(R.id.dateText);
            statusImageView = view.findViewById(R.id.statusImageView);
            equityText = view.findViewById(R.id.equityText);
//            collaboratorImage = view.findViewById(R.id.collaboratorImage);
//            equityimage = view.findViewById(R.id.equityImage);
//            privateImageView = view.findViewById(R.id.privateImageView);
//            publicImageView = view.findViewById(R.id.publicImageView);

            Edit = (TextView) itemView.findViewById(R.id.Edit);
            Share = (TextView) itemView.findViewById(R.id.Share);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
        }
    }
}
