package com.divya.sprxs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.EditMilestonesActivity;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.CancelMilestonesRequest;
import com.divya.sprxs.model.CancelMilestonesResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ViewMilestonesResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class DataAdapterMilestones extends RecyclerView.Adapter<DataAdapterMilestones.ViewHolder> {

    private List<ViewMilestonesResponse> viewMilestonesResponseList;
    private CancelMilestonesResponse cancelMilestonesResponse;
    private CardView cardViewMilestone;
    private Context context;
    private int milestoneId;


    public DataAdapterMilestones(FragmentActivity fragmentActivity, List<ViewMilestonesResponse> viewMilestonesResponseList, Context context) {
        this.viewMilestonesResponseList = viewMilestonesResponseList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterMilestones.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_milestone, parent, false);
        cardViewMilestone = v.findViewById(R.id.card_view_milestone);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterMilestones.ViewHolder holder, final int position) {

        if(viewMilestonesResponseList.get(position).getApproval()==-1) {
            holder.statusMilestone.setText("Cancelled");
            holder.deleteMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.editMilestoneImageView.setVisibility(View.INVISIBLE);
        } else if(viewMilestonesResponseList.get(position).getApproval()==0){
            holder.statusMilestone.setText("Rejected");
            holder.deleteMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.editMilestoneImageView.setVisibility(View.INVISIBLE);
        } else if(viewMilestonesResponseList.get(position).getApproval()==1){
            holder.statusMilestone.setText("Completed");
            holder.deleteMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.editMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.milestoneImageView.setImageResource(R.drawable.ic_icons8_ok);
        }else if(viewMilestonesResponseList.get(position).getApproval()==2){
            holder.statusMilestone.setText("Pending");
            holder.deleteMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.editMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.milestoneImageView.setBackgroundResource(R.drawable.circle_background_orange);
            holder.milestoneImageView.setImageResource(R.drawable.ic_icons8_person);
        }else if(viewMilestonesResponseList.get(position).getApproval()==3){
            holder.statusMilestone.setText("Active");
            holder.milestoneImageView.setImageResource(R.drawable.imgpsh_mobile_save);
        }else if(viewMilestonesResponseList.get(position).getApproval()==4){
            holder.statusMilestone.setText("Cancel Request");
            holder.milestoneImageView.setImageResource(R.drawable.ic_error_outline_black_24dp);
            holder.deleteMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.editMilestoneImageView.setVisibility(View.INVISIBLE);

        }

        holder.titleMilestone.setText(viewMilestonesResponseList.get(position).getMilestoneName());
        holder.createdMilestoneView.setText(viewMilestonesResponseList.get(position).getMilestoneDateCreated());
        holder.plannedMilestone.setText(viewMilestonesResponseList.get(position).getAgreedCompletionDate());
        holder.tokensMilestone.setText(String.valueOf(viewMilestonesResponseList.get(position).getOfferedTokens()));
        holder.descMilestoneView.setText(viewMilestonesResponseList.get(position).getMilestoneDescription());
        holder.collaboratorMilestone.setText(viewMilestonesResponseList.get(position).getFirstname()+" "+viewMilestonesResponseList.get(position).getSurname());

        holder.deleteMilestoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                milestoneId = viewMilestonesResponseList.get(position).getId();
                cancelMilestone();
            }
        });
        holder.editMilestoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditMilestonesActivity.class);
                intent.putExtra("ideaIdMilestoneAdapter",viewMilestonesResponseList.get(position).getIdeaId());
                intent.putExtra("idMilestoneAdapter",viewMilestonesResponseList.get(position).getId());
                intent.putExtra("approvalMilestoneAdapter",viewMilestonesResponseList.get(position).getApproval());
                intent.putExtra("titleMilestoneAdapter",viewMilestonesResponseList.get(position).getMilestoneName());
                intent.putExtra("DescMilestoneAdapter",viewMilestonesResponseList.get(position).getMilestoneDescription());
                intent.putExtra("DateMilestoneAdapter",viewMilestonesResponseList.get(position).getAgreedCompletionDate());
                intent.putExtra("TokensMilestoneAdapter",viewMilestonesResponseList.get(position).getOfferedTokens());
                intent.putExtra("CollabMilestoneAdapter",viewMilestonesResponseList.get(position).getFirstname()+" "+viewMilestonesResponseList.get(position).getSurname());
                context.startActivity(intent);            }
        });

    }

    private void cancelMilestone() {

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);

        Call<CancelMilestonesResponse> call;
        call = RetrofitClient.getInstance().getApi().cancelMilestones(
                "Bearer " + token,
                new CancelMilestonesRequest(milestoneId));
        call.enqueue(new Callback<CancelMilestonesResponse>() {
            @Override
            public void onResponse(Call<CancelMilestonesResponse> call, Response<CancelMilestonesResponse> response) {
                if (response.code() == 200) {
                    cancelMilestonesResponse = response.body();
                    final View successDialogView = LayoutInflater.from(context).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("Milestone successfully cancelled");
                    Button button;
                    button = successDialogView.findViewById(R.id.okButton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(successDialogView);
                    dialog.show();
                } else if (response.code() == 401) {
                    Call<RefreshTokenResponse> callrefresh;
                    callrefresh = RetrofitClient.getInstance().getApi().refreshToken(
                            "Bearer " + refresh_token);

                    callrefresh.enqueue(new Callback<RefreshTokenResponse>() {
                        @Override
                        public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                            if (response.code() == 200) {
                                RefreshTokenResponse refreshTokenResponse = response.body();
                                editor.putString("token", refreshTokenResponse.getAccess_token());
                                editor.apply();
                                cancelMilestone();
                            } else {
                                final View errorDialogView = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.error_dialog);
                                TextView textView;
                                textView = errorDialogView.findViewById(R.id.dialogTextView);
                                textView.setText("Technical Error.Please try again later");
                                Button button;
                                button = errorDialogView.findViewById(R.id.okButton);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setContentView(errorDialogView);
                                dialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        final View errorDialogView = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.error_dialog);
                        TextView textView;
                        textView = errorDialogView.findViewById(R.id.dialogTextView);
                        textView.setText(jObjError.getString("error"));
                        Button button;
                        button = errorDialogView.findViewById(R.id.okButton);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setContentView(errorDialogView);
                        dialog.show();
                    } catch (Exception e) {
                        final View errorDialogView = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.error_dialog);
                        TextView textView;
                        textView = errorDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("Technical Error.Please try again later");
                        Button button;
                        button = errorDialogView.findViewById(R.id.okButton);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setContentView(errorDialogView);
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CancelMilestonesResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return (viewMilestonesResponseList == null) ? 0 : viewMilestonesResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleMilestone,statusMilestone,createdMilestoneView,plannedMilestone,collaboratorMilestone,tokensMilestone,descMilestoneView;
        ImageView milestoneImageView,deleteMilestoneImageView,editMilestoneImageView;

        public ViewHolder(View view) {
            super(view);

            titleMilestone = view.findViewById(R.id.titleMilestone);
            statusMilestone = view.findViewById(R.id.statusMilestone);
            createdMilestoneView = view.findViewById(R.id.createdMilestoneView);
            plannedMilestone = view.findViewById(R.id.plannedMilestone);
            collaboratorMilestone = view.findViewById(R.id.collaboratorMilestone);
            tokensMilestone = view.findViewById(R.id.tokensMilestone);
            descMilestoneView = view.findViewById(R.id.descMilestoneView);
            milestoneImageView = view.findViewById(R.id.milestoneImageView);
            deleteMilestoneImageView = view.findViewById(R.id.deleteMilestoneImageView);
            editMilestoneImageView = view.findViewById(R.id.editMilestoneImageView);
        }
    }
}
