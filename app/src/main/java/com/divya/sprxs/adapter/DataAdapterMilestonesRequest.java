package com.divya.sprxs.adapter;

import android.app.Dialog;
import android.content.Context;
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
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.LodgeConsensusRequest;
import com.divya.sprxs.model.LodgeConsensusResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ViewMilestonesResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class DataAdapterMilestonesRequest extends RecyclerView.Adapter<DataAdapterMilestonesRequest.ViewHolder> {

    private List<ViewMilestonesResponse> viewMilestonesResponseList;
    private CardView cardViewMilestonesRequest;
    private Context context;
    private LodgeConsensusResponse lodgeConsensusResponse;
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

        if (viewMilestonesResponseList.get(position).getApproval() == -1) {
            holder.statusMilestoneRequest.setText("Cancelled");
            holder.approveMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.rejectMilestoneImageView.setVisibility(View.INVISIBLE);
        } else if (viewMilestonesResponseList.get(position).getApproval() == 0) {
            holder.statusMilestoneRequest.setText("Rejected");
            holder.approveMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.rejectMilestoneImageView.setVisibility(View.INVISIBLE);
        } else if (viewMilestonesResponseList.get(position).getApproval() == 1) {
            holder.statusMilestoneRequest.setText("Completed");
            holder.approveMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.rejectMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.milestoneRequestImageView.setImageResource(R.drawable.ic_icons8_ok);
        } else if (viewMilestonesResponseList.get(position).getApproval() == 2) {
            holder.statusMilestoneRequest.setText("Pending");
            holder.approveMilestoneImageView.setVisibility(View.VISIBLE);
            holder.rejectMilestoneImageView.setVisibility(View.VISIBLE);
            holder.milestoneRequestImageView.setBackgroundResource(R.drawable.circle_background_orange);
            holder.milestoneRequestImageView.setImageResource(R.drawable.ic_icons8_person);
        } else if (viewMilestonesResponseList.get(position).getApproval() == 3) {
            holder.statusMilestoneRequest.setText("Active");
            holder.approveMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.rejectMilestoneImageView.setVisibility(View.INVISIBLE);
            holder.milestoneRequestImageView.setImageResource(R.drawable.imgpsh_mobile_save);
        } else if (viewMilestonesResponseList.get(position).getApproval() == 4) {
            holder.statusMilestoneRequest.setText("Cancel Request");
            holder.milestoneRequestImageView.setImageResource(R.drawable.ic_error_outline_black_24dp);
            holder.approveMilestoneImageView.setVisibility(View.VISIBLE);
            holder.rejectMilestoneImageView.setVisibility(View.VISIBLE);
        }

        holder.titleMilestoneRequest.setText(viewMilestonesResponseList.get(position).getMilestoneName());
        holder.createdMilestoneRequestView.setText(viewMilestonesResponseList.get(position).getMilestoneDateCreated());
        holder.plannedMilestoneRequest.setText(viewMilestonesResponseList.get(position).getAgreedCompletionDate());
        holder.tokensMilestoneRequest.setText(String.valueOf(viewMilestonesResponseList.get(position).getOfferedTokens()));
        holder.descMilestoneRequestView.setText(viewMilestonesResponseList.get(position).getMilestoneDescription());

        milestoneId = viewMilestonesResponseList.get(position).getId();

        holder.approveMilestoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveLodgeConsensus();
            }
        });

        holder.rejectMilestoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectLodgeConsensus();
            }
        });

    }

    private void approveLodgeConsensus() {

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);

//                final String ideaId = getIntent().getStringExtra("IdeaNameMyCollab");


        Call<LodgeConsensusResponse> call;
        call = RetrofitClient.getInstance().getApi().lodgeConsensus(
                "Bearer " + token,
                new LodgeConsensusRequest(milestoneId, "", 1));
        call.enqueue(new Callback<LodgeConsensusResponse>() {
            @Override
            public void onResponse(Call<LodgeConsensusResponse> call, Response<LodgeConsensusResponse> response) {
                if (response.code() == 200) {
                    lodgeConsensusResponse = response.body();
                    final View successDialogView = LayoutInflater.from(context).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("You have successfully approved this Milestone");
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
                                approveLodgeConsensus();
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
            public void onFailure(Call<LodgeConsensusResponse> call, Throwable t) {
            }
        });

    }

    private void rejectLodgeConsensus() {

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);

//        final String ideaId = getIntent().getStringExtra("ideaIdMilestoneAdapter");

        Call<LodgeConsensusResponse> call;
        call = RetrofitClient.getInstance().getApi().lodgeConsensus(
                "Bearer " + token,
                new LodgeConsensusRequest(milestoneId, "", 0));
        call.enqueue(new Callback<LodgeConsensusResponse>() {
            @Override
            public void onResponse(Call<LodgeConsensusResponse> call, Response<LodgeConsensusResponse> response) {
                if (response.code() == 200) {
                    lodgeConsensusResponse = response.body();
                    final View successDialogView = LayoutInflater.from(context).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("You have successfully rejected this Milestone");
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
                                approveLodgeConsensus();
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
            public void onFailure(Call<LodgeConsensusResponse> call, Throwable t) {
            }
        });


    }

    @Override
    public int getItemCount() {
        return (viewMilestonesResponseList == null) ? 0 : viewMilestonesResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleMilestoneRequest, statusMilestoneRequest, createdMilestoneRequestView, plannedMilestoneRequest, collaboratorMilestoneRequest, tokensMilestoneRequest, descMilestoneRequestView;
        ImageView milestoneRequestImageView, rejectMilestoneImageView, approveMilestoneImageView;

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
