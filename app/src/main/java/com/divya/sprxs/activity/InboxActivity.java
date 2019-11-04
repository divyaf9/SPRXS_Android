package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterInbox;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ViewEventsRequest;
import com.divya.sprxs.model.ViewEventsResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class InboxActivity extends AppCompatActivity {



    private RecyclerView recyclerViewInbox;
    private DataAdapterInbox dataAdapterInbox;
    private List<ViewEventsResponse> viewEventsResponsedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        setTitle("Inbox");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerViewInbox = findViewById(R.id.recycler_view_inbox);
        recyclerViewInbox.setNestedScrollingEnabled(false);
        viewEvent();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    public void viewEvent() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<List<ViewEventsResponse>> call;
        call = RetrofitClient.getInstance().getApi().viewEvent(
                "Bearer " + token,
                new ViewEventsRequest(0, "Both", "", "", "", ""));
        call.enqueue(new Callback<List<ViewEventsResponse>>() {
            @Override
            public void onResponse(Call<List<ViewEventsResponse>> call, Response<List<ViewEventsResponse>> response) {
                if (response.code() == 200) {
                    viewEventsResponsedata = response.body();
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(InboxActivity.this);
                    recyclerViewInbox.setLayoutManager(layoutManager);
                    dataAdapterInbox = new DataAdapterInbox(InboxActivity.this, viewEventsResponsedata, getApplicationContext());
                    recyclerViewInbox.setAdapter(dataAdapterInbox);
                    dataAdapterInbox.notifyDataSetChanged();

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
                                viewEvent();
                            } else {
                                final View errorDialogView = LayoutInflater.from(InboxActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(InboxActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(InboxActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(InboxActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(InboxActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(InboxActivity.this);
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
            public void onFailure(Call<List<ViewEventsResponse>> call, Throwable t) {
            }
        });
    }

}
