package com.divya.sprxs.adapter;

import android.content.Context;
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
import com.divya.sprxs.model.ViewMilestonesResponse;

import java.util.List;

public class DataAdapterMilestonesRequest extends RecyclerView.Adapter<DataAdapterMilestonesRequest.ViewHolder> {

    private List<ViewMilestonesResponse> viewMilestonesResponseList;
    private CardView cardViewMilestonesRequest;
    private Context context;
    private int milestoneId;


    public DataAdapterMilestonesRequest(FragmentActivity fragmentActivity, List<ViewMilestonesResponse> viewMilestonesResponseList, Context context) {
        this.viewMilestonesResponseList = viewMilestonesResponseList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterMilestonesRequest.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_milestone_requests, parent, false);
        cardViewMilestonesRequest = v.findViewById(R.id.card_view_item_milestone_requests);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterMilestonesRequest.ViewHolder holder, final int position) {

        if(viewMilestonesResponseList.get(position).getApproval()==-1) {
            holder.statusMilestoneRequest.setText("Cancelled");
        } else if(viewMilestonesResponseList.get(position).getApproval()==0){
            holder.statusMilestoneRequest.setText("Rejected");
        } else if(viewMilestonesResponseList.get(position).getApproval()==1){
            holder.statusMilestoneRequest.setText("Completed");
            holder.milestoneRequestImageView.setImageResource(R.drawable.ic_icons8_ok);
        }else if(viewMilestonesResponseList.get(position).getApproval()==2){
            holder.statusMilestoneRequest.setText("Pending");
            holder.milestoneRequestImageView.setBackgroundResource(R.drawable.circle_background_orange);
            holder.milestoneRequestImageView.setImageResource(R.drawable.ic_icons8_person);
        }else if(viewMilestonesResponseList.get(position).getApproval()==3){
            holder.statusMilestoneRequest.setText("Active");
            holder.milestoneRequestImageView.setImageResource(R.drawable.imgpsh_mobile_save);
        }else if(viewMilestonesResponseList.get(position).getApproval()==4){
            holder.statusMilestoneRequest.setText("In Progress");
        }

        holder.titleMilestoneRequest.setText(viewMilestonesResponseList.get(position).getMilestoneName());
        holder.createdMilestoneRequestView.setText(viewMilestonesResponseList.get(position).getMilestoneDateCreated());
        holder.plannedMilestoneRequest.setText(viewMilestonesResponseList.get(position).getAgreedCompletionDate());
        holder.tokensMilestoneRequest.setText(String.valueOf(viewMilestonesResponseList.get(position).getOfferedTokens()));
        holder.descMilestoneRequestView.setText(viewMilestonesResponseList.get(position).getMilestoneDescription());

    }


    @Override
    public int getItemCount() {
        return (viewMilestonesResponseList == null) ? 0 : viewMilestonesResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleMilestoneRequest,statusMilestoneRequest,createdMilestoneRequestView,plannedMilestoneRequest,collaboratorMilestoneRequest,tokensMilestoneRequest,descMilestoneRequestView;
        ImageView milestoneRequestImageView,rejectMilestoneImageView,approveMilestoneImageView;

        public ViewHolder(View view) {
            super(view);

            titleMilestoneRequest = view.findViewById(R.id.titleMilestoneRequest);
            statusMilestoneRequest = view.findViewById(R.id.statusMilestoneRequest);
            createdMilestoneRequestView = view.findViewById(R.id.createdMilestoneRequestView);
            plannedMilestoneRequest = view.findViewById(R.id.plannedMilestoneRequest);
            collaboratorMilestoneRequest = view.findViewById(R.id.createdMilestoneRequestView);
            tokensMilestoneRequest = view.findViewById(R.id.tokensMilestoneRequest);
            descMilestoneRequestView = view.findViewById(R.id.descMilestoneRequestView);
            milestoneRequestImageView = view.findViewById(R.id.milestoneRequestImageView);
            approveMilestoneImageView = view.findViewById(R.id.approveMilestoneImageView);
            rejectMilestoneImageView = view.findViewById(R.id.rejectMilestoneImageView);

        }
    }
}
