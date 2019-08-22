package com.divya.sprxs.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.TransferEquityActivity;
import com.divya.sprxs.model.ShowEquityForIdeaResponse;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class DataAdapterCollaborators extends RecyclerView.Adapter<DataAdapterCollaborators.ViewHolder> {

    private List<ShowEquityForIdeaResponse> showEquityForIdeaResponses;
    private CardView cardViewCollaborator;
    private Context context;
    private Long id;
 ;


    public DataAdapterCollaborators(FragmentActivity fragmentActivity, List<ShowEquityForIdeaResponse> showEquityForIdeaResponses, Context context) {
        this.showEquityForIdeaResponses = showEquityForIdeaResponses;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         id = prefs.getLong("id", 0L);
    }

    @NonNull
    @Override
    public DataAdapterCollaborators.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_collaborators, parent, false);
        cardViewCollaborator = v.findViewById(R.id.card_view_collaborator);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterCollaborators.ViewHolder holder, final int position) {

        holder.nameCollaboratorTextView.setText(showEquityForIdeaResponses.get(position).getFirstname()+" "+showEquityForIdeaResponses.get(position).getLastname());
        holder.equityCollaboratorTextView.setText(String.valueOf(showEquityForIdeaResponses.get(position).getTokensOwned()));
        if(id.equals(showEquityForIdeaResponses.get(position).getProfileId())) {
            holder.identityCollaboratorTextView.setText("IDEA OWNER");
        }
        else{
            holder.identityCollaboratorTextView.setText("TRANSFER EQUITY");
            holder.identityCollaboratorTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, TransferEquityActivity.class);
                    intent.putExtra("CollabNameEquityTransfer",showEquityForIdeaResponses.get(position).getFirstname()+" "+showEquityForIdeaResponses.get(position).getLastname());
                    intent.putExtra("profileIdEquityTransfer",showEquityForIdeaResponses.get(position).getProfileId());
                    intent.putExtra("ideaIdEquityTransfer",showEquityForIdeaResponses.get(position).getIdeaUniqueId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (showEquityForIdeaResponses == null) ? 0 : showEquityForIdeaResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameCollaboratorTextView,equityCollaboratorTextView,identityCollaboratorTextView;

        public ViewHolder(View view) {
            super(view);
            nameCollaboratorTextView = view.findViewById(R.id.nameCollaboratorTextView);
            equityCollaboratorTextView = view.findViewById(R.id.equityCollaboratorTextView);
            identityCollaboratorTextView = view.findViewById(R.id.identityCollaboratorTextView);


        }
    }
}
