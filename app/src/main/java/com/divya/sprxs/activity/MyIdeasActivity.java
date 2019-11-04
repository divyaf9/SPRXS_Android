package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapter;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.controller.SwipeController;
import com.divya.sprxs.model.MyIdeasSummaryRequest;
import com.divya.sprxs.model.MyIdeasSummaryResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class MyIdeasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private BottomNavigationView bottomNavigationView;
    private List<MyIdeasSummaryResponse> myIdeasSummaryResponsedata;
    private SwipeController swipeController = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ideas);


        setTitle("My Ideas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recycler_view);

        ideasSummary();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void ideasSummary() {

        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<List<MyIdeasSummaryResponse>> call;
        call = RetrofitClient.getInstance().getApi().ideasSummary(
                "Bearer " + token,
                new MyIdeasSummaryRequest(""));
        call.enqueue(new Callback<List<MyIdeasSummaryResponse>>() {
            @Override
            public void onResponse(Call<List<MyIdeasSummaryResponse>> call, Response<List<MyIdeasSummaryResponse>> response) {
                if (response.code() == 200) {
                    myIdeasSummaryResponsedata = response.body();
                    if (myIdeasSummaryResponsedata.size() == 0) {
//                        NoIdeasFragment noIdeasFragment = new NoIdeasFragment();
//                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
//                        fragTransaction.replace(R.id.myIdeasFragment, noIdeasFragment);
//                        fragTransaction.addToBackStack(null);
//                        fragTransaction.commit();
                        setContentView(R.layout.activity_no_ideas);

                    } else {
                        recyclerView.setHasFixedSize(false);
                        recyclerView.setNestedScrollingEnabled(false);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyIdeasActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        dataAdapter = new DataAdapter(MyIdeasActivity.this, myIdeasSummaryResponsedata, getApplicationContext());
                        recyclerView.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

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
                                ideasSummary();
                            } else {
                                final View errorDialogView = LayoutInflater.from(MyIdeasActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(MyIdeasActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MyIdeasActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MyIdeasActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MyIdeasActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MyIdeasActivity.this);
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
            public void onFailure(Call<List<MyIdeasSummaryResponse>> call, Throwable t) {
            }
        });
    }


}
