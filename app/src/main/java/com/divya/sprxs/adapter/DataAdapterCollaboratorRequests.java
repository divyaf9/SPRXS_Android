package com.divya.sprxs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.CollaboratorRequestsActivity;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.ApproveRejectCollaboratorRequestsRequest;
import com.divya.sprxs.model.ApproveRejectCollaboratorRequestsResponse;
import com.divya.sprxs.model.CollaboratorRequestsResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.RegisterActivity.MY_PREFS_NAME;

public class DataAdapterCollaboratorRequests extends RecyclerView.Adapter<DataAdapterCollaboratorRequests.ViewHolder> {

    private List<CollaboratorRequestsResponse> collaboratorRequestsResponseList = new ArrayList<>();
    private ApproveRejectCollaboratorRequestsResponse approveRejectCollaboratorRequestsResponse;
    private CardView cardViewCollaboratorRequests;
    private Context context;
    private Long profileId;
    private String ideaId,reason,value,name,collaboratorUid;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String ownerFirebaseId = user.getUid();

    public DataAdapterCollaboratorRequests(FragmentActivity fragmentActivity, List<CollaboratorRequestsResponse> collaboratorRequestsResponseList, Context context) {
        this.collaboratorRequestsResponseList = collaboratorRequestsResponseList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataAdapterCollaboratorRequests.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_collaborator_requests, parent, false);
        cardViewCollaboratorRequests = v.findViewById(R.id.card_view_collaborator);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (collaboratorRequestsResponseList.get(position).getCollabApproved() == 2) {
            profileId = collaboratorRequestsResponseList.get(position).getProfileId();
            ideaId = collaboratorRequestsResponseList.get(position).getIdeaId();
            reason = collaboratorRequestsResponseList.get(position).getCollabReason();
            value = collaboratorRequestsResponseList.get(position).getCollabValueadd();
            name = collaboratorRequestsResponseList.get(position).getFirstname() + " " + collaboratorRequestsResponseList.get(position).getLastname();
            holder.collaboratorNameRequestTextView.setText(collaboratorRequestsResponseList.get(position).getFirstname() + " " + collaboratorRequestsResponseList.get(position).getLastname());
            holder.ideaIdRequestTextView.setText(collaboratorRequestsResponseList.get(position).getIdeaId());
            if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 1) {
                holder.roleRequestTextView.setText("Developer");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 2) {
                holder.roleRequestTextView.setText("Creative");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 3) {
                holder.roleRequestTextView.setText("Musical");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 4) {
                holder.roleRequestTextView.setText("Accounting");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 5) {
                holder.roleRequestTextView.setText("Editorial");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 6) {
                holder.roleRequestTextView.setText("Marketing");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 7) {
                holder.roleRequestTextView.setText("Sales");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 8) {
                holder.roleRequestTextView.setText("Technical");
            } else if (collaboratorRequestsResponseList.get(position).getLkpWoiRole() == 9) {
                holder.roleRequestTextView.setText("Other");
            }
            collaboratorUid = collaboratorRequestsResponseList.get(position).getCollaboratorFirebaseUid();

            holder.acceptRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveCollaborator();

                }
            });

            holder.rejectRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rejectCollaborator();
                }
            });

            holder.profileRequestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View collaboratorRequest = LayoutInflater.from(context).inflate(R.layout.collaborator_requests, null);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.collaborator_requests);
                    TextView reasonRequestTextView, valueRequestTextView,nameRequestTextView;
                    nameRequestTextView = collaboratorRequest.findViewById(R.id.nameRequestTextView);
                    reasonRequestTextView = collaboratorRequest.findViewById(R.id.reasonRequestTextView);
                    valueRequestTextView = collaboratorRequest.findViewById(R.id.valueRequestTextView);
                    nameRequestTextView.setText(collaboratorRequestsResponseList.get(position).getFirstname()+" "+collaboratorRequestsResponseList.get(position).getLastname());
                    reasonRequestTextView.setText(collaboratorRequestsResponseList.get(position).getCollabReason());
                    valueRequestTextView.setText(collaboratorRequestsResponseList.get(position).getCollabValueadd());
                    Button button;
                    button = collaboratorRequest.findViewById(R.id.cancelRequestButton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(collaboratorRequest);
                    dialog.show();
                }
            });

        }
    }

    class Message {
        public HashMap<String,Object> location;
        private Message() {}
        public Message(HashMap<String,Object> location) {
            this.location = location;
        }
        public HashMap<String,Object> getLocation() {
            return location;
        }
    }

        public void sendNewMessage(final String senderFirebaseId, String receiverFirebaseId, String message){

        try {

            if (receiverFirebaseId != "") {
                receiverFirebaseId = collaboratorUid;
            }

            long time = System.currentTimeMillis();

            final HashMap<String,Object> sendNewMesaageMap = new HashMap<>();
            sendNewMesaageMap.put("type", "text");
            sendNewMesaageMap.put("content", message);
            sendNewMesaageMap.put("fromID", ownerFirebaseId);
            sendNewMesaageMap.put("toID", receiverFirebaseId);
            sendNewMesaageMap.put("timestamp", time);
            sendNewMesaageMap.put("isRead", false);

            final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            final String finalReceiverFirebaseId = receiverFirebaseId;
            final String finalReceiverFirebaseId1 = receiverFirebaseId;
            try{
                databaseReference.child("users").child(ownerFirebaseId).child("conversations").child(receiverFirebaseId).addListenerForSingleValueEvent(new ValueEventListener() {  //Check Convo tree
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() != null){
                        //Tree exists.
                        HashMap<String,Object> oldLoc = new HashMap<String, Object>();
                                oldLoc.put("Snapshot", dataSnapshot.getValue());
                                oldLoc.put("newLoc", oldLoc.get("Snapshot").toString().replace("{location=", ""));
                                String  finalLoc = oldLoc.get("newLoc").toString().replace("}", "");
                        DatabaseReference oldTree = firebaseDatabase.getReference();
                        oldTree.child("conversations").child(finalLoc).push().setValue(sendNewMesaageMap);
                    }
                    else{
                        //Tree doesn't exist.
                                DatabaseReference messLoc = firebaseDatabase.getReference();
                                String messLocKey = messLoc.push().getKey();
                                messLoc.child("conversations").child(messLocKey).push().setValue(sendNewMesaageMap);
                                HashMap<String, Object> simpleDict = new HashMap<>();
                                simpleDict.put("location", messLocKey);

                                //double update:
                                DatabaseReference firstCopy = firebaseDatabase.getReference();
                                firstCopy.child("users").child(senderFirebaseId).child("conversations").child(finalReceiverFirebaseId1).updateChildren(simpleDict);

                                DatabaseReference secondCopy = firebaseDatabase.getReference();
                                secondCopy.child("users").child(finalReceiverFirebaseId1).child("conversations").child(senderFirebaseId).updateChildren(simpleDict);

                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }catch (Exception ex){
                Log.e("DBRef error", ex.getMessage());
                System.out.println("DBREF:::::::::::::::::::::::::::::::::::::::::::::::::::::::"+databaseReference);
            }

        }catch (Exception ex){
            Log.e("ExceptionChat",ex.getMessage());
        }



    }

    private void approveCollaborator() {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<ApproveRejectCollaboratorRequestsResponse> call;
        call = RetrofitClient.getInstance().getApi().approveCollaborator(
                "Bearer " + token, new ApproveRejectCollaboratorRequestsRequest(profileId, ideaId, "Approve"));
        call.enqueue(new Callback<ApproveRejectCollaboratorRequestsResponse>() {
            @Override
            public void onResponse(Call<ApproveRejectCollaboratorRequestsResponse> call, Response<ApproveRejectCollaboratorRequestsResponse> response) {
                if (response.code() == 200) {
                    approveRejectCollaboratorRequestsResponse = response.body();
                    final View successDialogView = LayoutInflater.from(context).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("You have accepted the collaborator request for "+ideaId+" from "+name);

                    sendNewMessage(ownerFirebaseId,collaboratorUid,"Hi! I have accepted your request to collaborate. Lets make our idea into a reality.");

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
                                approveCollaborator();
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
            public void onFailure(Call<ApproveRejectCollaboratorRequestsResponse> call, Throwable t) {
            }
        });
    }

    private void rejectCollaborator() {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<ApproveRejectCollaboratorRequestsResponse> call;
        call = RetrofitClient.getInstance().getApi().approveCollaborator(
                "Bearer " + token, new ApproveRejectCollaboratorRequestsRequest(profileId, ideaId, "Reject"));
        call.enqueue(new Callback<ApproveRejectCollaboratorRequestsResponse>() {
            @Override
            public void onResponse(Call<ApproveRejectCollaboratorRequestsResponse> call, Response<ApproveRejectCollaboratorRequestsResponse> response) {
                if (response.code() == 200) {
                    approveRejectCollaboratorRequestsResponse = response.body();
                    Context fragmentActivity;
                    final View successDialogView = LayoutInflater.from(context).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("You have rejected the collaborator request for "+ideaId+" from "+name);
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
                                rejectCollaborator();
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
            public void onFailure(Call<ApproveRejectCollaboratorRequestsResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return (collaboratorRequestsResponseList == null) ? 0 : collaboratorRequestsResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView collaboratorNameRequestTextView, roleRequestTextView, ideaIdRequestTextView;
        private ImageButton acceptRequestButton, rejectRequestButton, profileRequestButton;


        public ViewHolder(final View view) {
            super(view);
            collaboratorNameRequestTextView = view.findViewById(R.id.collaboratorNameRequestTextView);
            roleRequestTextView = view.findViewById(R.id.roleRequestTextView);
            ideaIdRequestTextView = view.findViewById(R.id.ideaIdRequestTextView);
            acceptRequestButton = view.findViewById(R.id.acceptRequestButton);
            rejectRequestButton = view.findViewById(R.id.rejectRequestButton);
            profileRequestButton = view.findViewById(R.id.profileRequestButton);


        }
    }
}
