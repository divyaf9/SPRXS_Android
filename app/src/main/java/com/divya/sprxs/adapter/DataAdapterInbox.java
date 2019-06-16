package com.divya.sprxs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.model.ViewEventsResponse;

import java.util.List;

public class DataAdapterInbox extends RecyclerView.Adapter<DataAdapterInbox.ViewHolder> {

    private List<ViewEventsResponse> viewEventsResponses;
    private CardView cardViewInbox;
    private Context context;


    public DataAdapterInbox(FragmentActivity fragmentActivity, List<ViewEventsResponse> viewEventsResponses, Context context) {
        this.viewEventsResponses = viewEventsResponses;
        this.context = context;
    }


    @NonNull
    @Override
    public DataAdapterInbox.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_inbox, parent, false);
        cardViewInbox = v.findViewById(R.id.card_view_inbox);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final DataAdapterInbox.ViewHolder holder, final int position) {
        holder.message.setText(viewEventsResponses.get(position).getDescription());

    }

    @Override
    public int getItemCount() {

        return (viewEventsResponses == null) ? 0 : viewEventsResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView message;


        public ViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
        }
    }
}
