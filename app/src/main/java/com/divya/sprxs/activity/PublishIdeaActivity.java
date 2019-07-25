package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.ListIdeaForCollaborationRequest;
import com.divya.sprxs.model.ListIdeaForCollaborationResponse;
import com.divya.sprxs.model.RefreshTokenResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class PublishIdeaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ideaNamePublishTextView,collabSkillsTextView,ideaSynopsisTextView;
    private TextView ideaIdPublish;
    private Button publishButton,cancelPublishButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_idea);

        this.setTitle("Make Idea Searchable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ideaNamePublishTextView = findViewById(R.id.ideaNamePublishTextView);
        collabSkillsTextView = findViewById(R.id.collabSkillsTextView);
        ideaSynopsisTextView = findViewById(R.id.ideaSynopsisTextView);
        ideaIdPublish = findViewById(R.id.ideaIdPublish);
        publishButton = findViewById(R.id.publishButton);
        publishButton.setOnClickListener(this);
        cancelPublishButton = findViewById(R.id.cancelPublishButton);
        cancelPublishButton.setOnClickListener(this);

        final String IdeaId = getIntent().getStringExtra("myList");
        final String IdeaDesc = getIntent().getStringExtra("myListIdeaDesc");
        final String IdeaName = getIntent().getStringExtra("myListIdeaName");
        ideaIdPublish.setText("#" + IdeaId);
        ideaNamePublishTextView.setText(IdeaName);
        ideaSynopsisTextView.setText(IdeaDesc);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.publishButton:
               publishIdea();
               break;
           case R.id.cancelPublishButton:
               onSupportNavigateUp();
               break;
       }
    }

    private void publishIdea() {
        String ideaSynopsis = ideaSynopsisTextView.getText().toString().trim();
        String collabSkills = collabSkillsTextView.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String IdeaId = getIntent().getStringExtra("myList");


        Call<ListIdeaForCollaborationResponse> call;
        progressBar.setVisibility(View.VISIBLE);
        call = RetrofitClient.getInstance().getApi().publishIdea(
                "Bearer " + token,
                new ListIdeaForCollaborationRequest(IdeaId,ideaSynopsis,collabSkills));
        call.enqueue(new Callback<ListIdeaForCollaborationResponse>() {
            @Override
            public void onResponse(Call<ListIdeaForCollaborationResponse> call, Response<ListIdeaForCollaborationResponse> response) {
                ListIdeaForCollaborationResponse listIdeaForCollaborationResponse = response.body();
                if (response.code() == 200) {
                    final View successDialogView = LayoutInflater.from(PublishIdeaActivity.this).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(PublishIdeaActivity.this);
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("Idea has been made Public " + listIdeaForCollaborationResponse.getMessage());
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
                                publishIdea();
                            } else {
//                                try {
//                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    final View errorDialogView = LayoutInflater.from(PublishIdeaActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(PublishIdeaActivity.this);
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
//                                    progressBar.setVisibility(View.GONE);
//                                } catch (Exception e) {
//                                    final View errorDialogView = LayoutInflater.from(PublishIdeaActivity.this).inflate(R.layout.error_dialog, null);
//                                    final Dialog dialog = new Dialog(PublishIdeaActivity.this);
//                                    dialog.setContentView(R.layout.error_dialog);
//                                    TextView textView;
//                                    textView = errorDialogView.findViewById(R.id.dialogTextView);
//                                    textView.setText("Technical Error\nPlease try again later");
//                                    Button button;
//                                    button = errorDialogView.findViewById(R.id.okButton);
//                                    button.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    dialog.setContentView(errorDialogView);
//                                    dialog.show();
//                                    progressBar.setVisibility(View.GONE);
//                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        final View errorDialogView = LayoutInflater.from(PublishIdeaActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(PublishIdeaActivity.this);
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
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        final View errorDialogView = LayoutInflater.from(PublishIdeaActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(PublishIdeaActivity.this);
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
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListIdeaForCollaborationResponse> call, Throwable t) {
            }
        });
    }


}
