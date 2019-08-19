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
import com.divya.sprxs.adapter.DataAdapterMyCollaborations;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.MyCollaborationsResponse;
import com.divya.sprxs.model.RefreshTokenResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class MyCollaborationsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMyCollaborations;
    private DataAdapterMyCollaborations dataAdapterMyCollaborations;
    private MyCollaborationsResponse myCollaborationsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collaborations);

        this.setTitle("My Collaborations");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewMyCollaborations = findViewById(R.id.recycler_view_my_collaborations);
        recyclerViewMyCollaborations.setNestedScrollingEnabled(false);

        myCollaborations();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyCollaborationsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void myCollaborations() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final Long id = prefs.getLong("id", 0L);

        Call<MyCollaborationsResponse> call;
        call = RetrofitClient.getInstance().getApi().myCollaborations(
                "Bearer " + token,id);
        call.enqueue(new Callback<MyCollaborationsResponse>() {
            @Override
            public void onResponse(Call<MyCollaborationsResponse> call, Response<MyCollaborationsResponse> response) {
                if (response.code() == 200) {
                    myCollaborationsResponse = response.body();
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyCollaborationsActivity.this);
                    recyclerViewMyCollaborations.setLayoutManager(layoutManager);
                    dataAdapterMyCollaborations = new DataAdapterMyCollaborations(MyCollaborationsActivity.this, myCollaborationsResponse, getApplicationContext());
                    recyclerViewMyCollaborations.setAdapter(dataAdapterMyCollaborations);
                    dataAdapterMyCollaborations.notifyDataSetChanged();
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
                                myCollaborations();
                            } else {
                                final View errorDialogView = LayoutInflater.from(MyCollaborationsActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(MyCollaborationsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MyCollaborationsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MyCollaborationsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MyCollaborationsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MyCollaborationsActivity.this);
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
            public void onFailure(Call<MyCollaborationsResponse> call, Throwable t) {
            }
        });
    }
}
