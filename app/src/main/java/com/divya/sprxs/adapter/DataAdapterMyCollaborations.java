package com.divya.sprxs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.MyCollaborationsDetailsActivity;
import com.divya.sprxs.model.MyCollaborationsResponse;


public class DataAdapterMyCollaborations extends RecyclerView.Adapter<DataAdapterMyCollaborations.ViewHolder> {

    private MyCollaborationsResponse myCollaborationsResponse;
    private CardView cardViewMyCollaborations;
    private Context context;


    public DataAdapterMyCollaborations(FragmentActivity fragmentActivity, MyCollaborationsResponse myCollaborationsResponse, Context context) {
        this.myCollaborationsResponse = myCollaborationsResponse;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterMyCollaborations.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_my_collaborations, parent, false);
        cardViewMyCollaborations = v.findViewById(R.id.card_view_my_collaborations);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterMyCollaborations.ViewHolder holder, final int position) {

        holder.ideaNameMyCollaborations.setText(myCollaborationsResponse.getIdeas().get(position).getIdeaName());
        holder.ideaIdMyCollaborations.setText(myCollaborationsResponse.getIdeas().get(position).getIdeaId());
        holder.equityMyCollaborations.setText(String.valueOf(myCollaborationsResponse.getIdeas().get(position).getEquity()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyCollaborationsDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("IdeaIdMyCollab",myCollaborationsResponse.getIdeas().get(position).getIdeaId());
                intent.putExtra("IdeaNameMyCollab",myCollaborationsResponse.getIdeas().get(position).getIdeaName());
                intent.putExtra("IdeaDescMyCollab",myCollaborationsResponse.getIdeas().get(position).getIdeaDescription());
                intent.putExtra("OwnerNameMyCollab",myCollaborationsResponse.getIdeas().get(position).getOwnerFirstname()+" "+myCollaborationsResponse.getIdeas().get(position).getOwnerLastname());
                intent.putExtra("EquityMyCollab",myCollaborationsResponse.getIdeas().get(position).getEquity());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return myCollaborationsResponse.getIdeas().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ideaNameMyCollaborations, ideaIdMyCollaborations, equityMyCollaborations;

        public ViewHolder(View view) {
            super(view);
            ideaNameMyCollaborations = view.findViewById(R.id.ideaNameMyCollaborations);
            ideaIdMyCollaborations = view.findViewById(R.id.ideaIdMyCollaborations);
            equityMyCollaborations = view.findViewById(R.id.equityMyCollaborationsTextView);

        }
    }
}
