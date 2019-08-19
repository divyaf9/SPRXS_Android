package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapter;
import com.divya.sprxs.adapter.DataAdapterCollaboratorRequests;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.fragment.NoIdeasFragment;
import com.divya.sprxs.model.CollaboratorRequestsRequest;
import com.divya.sprxs.model.CollaboratorRequestsResponse;
import com.divya.sprxs.model.RefreshTokenResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;
import static java.security.AccessController.getContext;

public class CollaboratorRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCollaboratorRequests;
    private DataAdapterCollaboratorRequests dataAdapterCollaboratorRequests;
    private List<CollaboratorRequestsResponse> collaboratorRequestsResponseList;
    private List<CollaboratorRequestsResponse> collaboratorRequestsResponseListFiltered = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborator_requests);

        this.setTitle("Collaborator Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewCollaboratorRequests = findViewById(R.id.recycler_view_collaborator_requests);

        getCollaboratorsByIdea();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CollaboratorRequestsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void getCollaboratorsByIdea() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String email = prefs.getString("email", null);
        Call<List<CollaboratorRequestsResponse>> call;
        call = RetrofitClient.getInstance().getApi().getCollaboratorsByProfile(
                "Bearer " + token,
                new CollaboratorRequestsRequest(email));
        call.enqueue(new Callback<List<CollaboratorRequestsResponse>>() {
            @Override
            public void onResponse(Call<List<CollaboratorRequestsResponse>> call, Response<List<CollaboratorRequestsResponse>> response) {
                if (response.code() == 200) {
                    collaboratorRequestsResponseList = response.body();
                    if (collaboratorRequestsResponseList.size() == 0) {
                        setContentView(R.layout.activity_no_collaborator_request);
                    } else {
//                            for (CollaboratorRequestsResponse model : collaboratorRequestsResponseList) {
//                                if (model.getCollabApproved() == 2) {
//                                    collaboratorRequestsResponseListFiltered.add(model);
//                                }
                        recyclerViewCollaboratorRequests.setHasFixedSize(false);
                        recyclerViewCollaboratorRequests.setNestedScrollingEnabled(false);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CollaboratorRequestsActivity.this);
                        recyclerViewCollaboratorRequests.setLayoutManager(layoutManager);
                        dataAdapterCollaboratorRequests = new DataAdapterCollaboratorRequests(CollaboratorRequestsActivity.this, collaboratorRequestsResponseList, CollaboratorRequestsActivity.this);
                        recyclerViewCollaboratorRequests.setAdapter(dataAdapterCollaboratorRequests);
                        dataAdapterCollaboratorRequests.notifyDataSetChanged();

                    }
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
                                getCollaboratorsByIdea();
                            } else {
                                final View errorDialogView = LayoutInflater.from(CollaboratorRequestsActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(CollaboratorRequestsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(CollaboratorRequestsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(CollaboratorRequestsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(CollaboratorRequestsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(CollaboratorRequestsActivity.this);
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
            public void onFailure(Call<List<CollaboratorRequestsResponse>> call, Throwable t) {
            }
        });

    }



}
