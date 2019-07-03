package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.ListIdeaForCollaborationRequest;
import com.divya.sprxs.model.ListIdeaForCollaborationResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.RequestWorkOnIdeaRequest;
import com.divya.sprxs.model.RequestWorkOnIdeaResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class RequestToCollaborate extends AppCompatActivity implements View.OnClickListener {

    private TextView ideaIdRequestToCollaborateText;
    private EditText reasonText,valueText;
    private Button requestToWorkOnButton;
    private ProgressBar progressBar;
    private Spinner spinner;
    private int mySpinnerValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_to_collaborate);

        this.setTitle("Request to Work on an Idea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ideaIdRequestToCollaborateText = findViewById(R.id.ideaIdRequestToCollaborateText);
        reasonText = findViewById(R.id.reasonText);
        valueText = findViewById(R.id.valueText);
        requestToWorkOnButton = findViewById(R.id.requestToWorkOnButton);
        requestToWorkOnButton.setOnClickListener(this);

        final String IdeaId = getIntent().getStringExtra("myRequestList");
        ideaIdRequestToCollaborateText.setText("Idea #"+IdeaId);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Role");
        categories.add(1, "Developer");
        categories.add(2, "Creative");
        categories.add(3, "Musical");
        categories.add(4, "Accounting");
        categories.add(5, "Editorial");
        categories.add(6, "Marketing");
        categories.add(7, "Sales");
        categories.add(8, "Technical");
        categories.add(9,"Other");


        spinner = findViewById(R.id.textCollaborateSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mySpinnerValue = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mySpinnerValue = 0;

            }
        });
}

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.requestToWorkOnButton:
             requestWorkOnIdea();
             break;
     }
    }

    private void requestWorkOnIdea() {


        String request = reasonText.getText().toString().trim();
        String collabValue = valueText.getText().toString().trim();
        final String IdeaId = getIntent().getStringExtra("myRequestList");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);

        Call<RequestWorkOnIdeaResponse> call;
        progressBar.setVisibility(View.VISIBLE);
        call = RetrofitClient.getInstance().getApi().requestWorkOnIdea(
                "Bearer " + token,
                new RequestWorkOnIdeaRequest(0L,IdeaId,mySpinnerValue,1,1,request,collabValue));
        call.enqueue(new Callback<RequestWorkOnIdeaResponse>() {
            @Override
            public void onResponse(Call<RequestWorkOnIdeaResponse> call, Response<RequestWorkOnIdeaResponse> response) {

                if (response.code() == 200) {

                    RequestWorkOnIdeaResponse requestWorkOnIdeaResponse = response.body();
                    final View successDialogView = LayoutInflater.from(RequestToCollaborate.this).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(RequestToCollaborate.this);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("You have requested to work on Idea ID "+requestWorkOnIdeaResponse.getIdeaID());
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
                    progressBar.setVisibility(View.GONE);
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
                                requestWorkOnIdea();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    final View successDialogView = LayoutInflater.from(RequestToCollaborate.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(getApplicationContext());
                                    dialog.setContentView(R.layout.error_dialog);
                                    TextView textView;
                                    textView = successDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText("Technical Error\nPlease try again later");
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
                                    progressBar.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    final View successDialogView = LayoutInflater.from(RequestToCollaborate.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(getApplicationContext());
                                    dialog.setContentView(R.layout.error_dialog);
                                    TextView textView;
                                    textView = successDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText("Technical Error\nPlease try again later");
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
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        final View successDialogView = LayoutInflater.from(RequestToCollaborate.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getApplicationContext());
                        dialog.setContentView(R.layout.error_dialog);
                        TextView textView;
                        textView = successDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("Technical Error\nPlease try again later");
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
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        final View successDialogView = LayoutInflater.from(RequestToCollaborate.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(RequestToCollaborate.this);
                        dialog.setContentView(R.layout.error_dialog);
                        TextView textView;
                        textView = successDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("Technical Error\nPlease try again later");
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
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RequestWorkOnIdeaResponse> call, Throwable t) {
            }
        });
    }
    }

