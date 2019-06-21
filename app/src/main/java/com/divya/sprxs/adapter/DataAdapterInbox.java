package com.divya.sprxs.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.model.ViewEventsResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if(viewEventsResponses.get(position).getEvent_type()==0){
            holder.imageView.setBackgroundResource(R.drawable.ic_icons8_notification_filled);
            holder.inboxTextView.setText("Notification");
        }else if (viewEventsResponses.get(position).getEvent_type()==1){
            holder.imageView.setBackgroundResource(R.drawable.ic_icons8_activity_feed_filled);
            holder.inboxTextView.setText("Activity");

        }
    }

    @Override
    public int getItemCount() {
        return (viewEventsResponses == null) ? 0 : viewEventsResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView message,inboxDate,inboxTextView;
        private ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.message);
//            inboxDate = view.findViewById(R.id.inboxDate);
            inboxTextView = view.findViewById(R.id.inboxTextView);
            imageView =view.findViewById(R.id.inboxImageView);
        }
    }
}
