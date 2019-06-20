package com.divya.sprxs.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import static com.divya.sprxs.activity.IdeaDetailsActivity.MY_IDEA_DETAILS;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class PublishIdea extends AppCompatActivity implements View.OnClickListener {

    private EditText ideaNamePublishTextView,collabSkillsTextView,ideaSynopsisTextView;
    private TextView ideaIdPublish;
    private Button publishButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_idea);

        this.setTitle("Make Idea Searchable");

        ideaNamePublishTextView = findViewById(R.id.ideaNamePublishTextView);
        collabSkillsTextView = findViewById(R.id.collabSkillsTextView);
        ideaSynopsisTextView = findViewById(R.id.ideaSynopsisTextView);
        ideaIdPublish = findViewById(R.id.ideaIdPublish);
        publishButton = findViewById(R.id.publishButton);
        publishButton.setOnClickListener(this);

        SharedPreferences idPrefs = getSharedPreferences(MY_IDEA_DETAILS, MODE_PRIVATE);
        final String ideaId = idPrefs.getString("ideaId", null);
        final String ideaName = idPrefs.getString("ideaName", null);
        final String ideaDescription = idPrefs.getString("ideaDescription", null);
        ideaIdPublish.setText("#" + ideaId);
        ideaNamePublishTextView.setText(ideaName);
        ideaSynopsisTextView.setText(ideaDescription);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.publishButton:
               publishIdea();
       }
    }

    private void publishIdea() {

        String ideaSynopsis = ideaSynopsisTextView.getText().toString().trim();
        String collabSkills = collabSkillsTextView.getText().toString().trim();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);

        Call<ListIdeaForCollaborationResponse> call;
        progressBar.setVisibility(View.VISIBLE);
        call = RetrofitClient.getInstance().getApi().publishIdea(
                "Bearer " + token,
                new ListIdeaForCollaborationRequest("",ideaSynopsis,collabSkills));
        call.enqueue(new Callback<ListIdeaForCollaborationResponse>() {
            @Override
            public void onResponse(Call<ListIdeaForCollaborationResponse> call, Response<ListIdeaForCollaborationResponse> response) {
                ListIdeaForCollaborationResponse listIdeaForCollaborationResponse = response.body();
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PublishIdea.this, R.style.Theme_AppCompat_DayNight_Dialog);
                    View successDialogView = LayoutInflater.from(PublishIdea.this).inflate(R.layout.success_dialog, null);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("Idea has been made Public" + listIdeaForCollaborationResponse.getMessage());
                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(PublishIdea.this, IdeaDetailsActivity.class);
                                    startActivity(intent);
                                }
                            });
                    builder.setView(successDialogView);
                    builder.show();
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
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PublishIdea.this, R.style.Theme_AppCompat_DayNight_Dialog);
                                    View errorDialogView = LayoutInflater.from(PublishIdea.this).inflate(R.layout.error_dialog, null);
                                    TextView textView;
                                    textView = errorDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText("Technical Error\nPlease try again later");
                                    String positiveText = getString(android.R.string.ok);
                                    builder.setPositiveButton(positiveText,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    builder.setView(errorDialogView);
                                    builder.show();
                                    progressBar.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PublishIdea.this, R.style.Theme_AppCompat_DayNight_Dialog);
                                    View errorDialogView = LayoutInflater.from(PublishIdea.this).inflate(R.layout.error_dialog, null);
                                    TextView textView;
                                    textView = errorDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText("Technical Error\nPlease try again later");
                                    String positiveText = getString(android.R.string.ok);
                                    builder.setPositiveButton(positiveText,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                    builder.setView(errorDialogView);
                                    builder.show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PublishIdea.this, R.style.Theme_AppCompat_DayNight_Dialog);
                        View errorDialogView = LayoutInflater.from(PublishIdea.this).inflate(R.layout.error_dialog, null);
                        TextView textView;
                        textView = errorDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("Please complete all the fields");
                        String positiveText = getString(android.R.string.ok);
                        builder.setPositiveButton(positiveText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.setView(errorDialogView);
                        builder.show();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PublishIdea.this, R.style.Theme_AppCompat_DayNight_Dialog);
                        View errorDialogView = LayoutInflater.from(PublishIdea.this).inflate(R.layout.error_dialog, null);
                        TextView textView;
                        textView = errorDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("Technical Error\nPlease try again later");
                        String positiveText = getString(android.R.string.ok);
                        builder.setPositiveButton(positiveText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.setView(errorDialogView);
                        builder.show();
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
