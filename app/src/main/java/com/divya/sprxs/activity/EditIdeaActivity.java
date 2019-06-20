package com.divya.sprxs.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.EditIdeaRequest;
import com.divya.sprxs.model.EditIdeaResponse;
import com.divya.sprxs.model.RefreshTokenResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.IdeaDetailsActivity.MY_IDEA_DETAILS;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class EditIdeaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ideaNameEditTextView, ideaDescriptionEditTextView, filenameEditTextView;
    private ImageView attachEditButton;
    private Button confirmEditButton;
    private TextView ideaIdEditTextView;
    private int mySpinnerValue;
    private ProgressBar progressBar;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idea);

        this.setTitle("Edit Idea");
        ideaNameEditTextView = findViewById(R.id.ideaNameEditTextView);
        ideaDescriptionEditTextView = findViewById(R.id.ideaDescriptionEditTextView);
        filenameEditTextView = findViewById(R.id.filenameEditTextView);
        ideaIdEditTextView = findViewById(R.id.ideaIdEditTextView);
        attachEditButton = findViewById(R.id.attachEditButton);
        attachEditButton.setOnClickListener(this);
        confirmEditButton = findViewById(R.id.confirmEditButton);
        confirmEditButton.setOnClickListener(this);

//        SharedPreferences idPrefs = getSharedPreferences(MY_IDEA_DETAILS, MODE_PRIVATE);
//        final String IdeaId = idPrefs.getString("ideaId", null);
//        final String IdeaDesc = idPrefs.getString("ideaName", null);
//        final String ideaDescription = idPrefs.getString("ideaDescription", null);


        final String IdeaId = getIntent().getStringExtra("myList");
        final String IdeaDesc = getIntent().getStringExtra("myListIdeaDesc");
        final String IdeaName = getIntent().getStringExtra("myListIdeaName");
        ideaIdEditTextView.setText("#" + IdeaId);
        ideaNameEditTextView.setText(IdeaName);
        ideaDescriptionEditTextView.setText(IdeaDesc);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);


        List<String> categories = new ArrayList<>();
        categories.add(0, "I have a ");
        categories.add(1, "Technology idea");
        categories.add(2, "Lifestyle & Wellbeing idea");
        categories.add(3, "Food & Drink idea");
        categories.add(4, "Gaming idea");
        categories.add(5, "Business & Finance idea");
        categories.add(6, "Art and Fashion idea");
        categories.add(7, "Film,Theatre & Music idea");
        categories.add(8, "Media & Journalism idea");

        final Spinner spinner = findViewById(R.id.textSpinnerEdit);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmEditButton:
                editIdea();
        }

    }

    private void editIdea() {
        String ideaName = ideaNameEditTextView.getText().toString().trim();
        String ideaDescription = ideaDescriptionEditTextView.getText().toString().trim();
        String fileName = filenameEditTextView.getText().toString().trim();

        if (ideaName.isEmpty()) {
            ideaNameEditTextView.setError("This Field is required");
            ideaNameEditTextView.requestFocus();
            return;
        } else if (ideaDescription.isEmpty()) {
            ideaDescriptionEditTextView.setError("This Field is required");
            ideaDescriptionEditTextView.requestFocus();
            return;
        }

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        SharedPreferences idPrefs = getSharedPreferences(MY_IDEA_DETAILS, MODE_PRIVATE);
//        final String ideaId = idPrefs.getString("ideaId", null);
        final String IdeaId = getIntent().getStringExtra("myList");



        Call<EditIdeaResponse> call;
        progressBar.setVisibility(View.VISIBLE);
        call = RetrofitClient.getInstance().getApi().editIdea(
                "Bearer " + token,
                new EditIdeaRequest(IdeaId, mySpinnerValue, 2, 3, ideaName, ideaDescription, "", "", "", "", ""));
        call.enqueue(new Callback<EditIdeaResponse>() {
            @Override
            public void onResponse(Call<EditIdeaResponse> call, Response<EditIdeaResponse> response) {
                EditIdeaResponse editIdeaResponse = response.body();
                if (response.code() == 200) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditIdeaActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                    View successDialogView = LayoutInflater.from(EditIdeaActivity.this).inflate(R.layout.success_dialog, null);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("Idea has been edited with ID " + editIdeaResponse.getIdea_ID());
                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Intent intent = new Intent(EditIdeaActivity.this, IdeaDetailsActivity.class);
//                                    startActivity(intent);
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
                                editIdea();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditIdeaActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                                    View errorDialogView = LayoutInflater.from(EditIdeaActivity.this).inflate(R.layout.error_dialog, null);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(EditIdeaActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                                    View errorDialogView = LayoutInflater.from(EditIdeaActivity.this).inflate(R.layout.error_dialog, null);
                                    TextView textView;
                                    textView = errorDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText("Technical Error\nPlease try again later");
                                    String positiveText = getString(android.R.string.ok);
                                    builder.setPositiveButton(positiveText,
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    openIdeaDetails();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditIdeaActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                        View errorDialogView = LayoutInflater.from(EditIdeaActivity.this).inflate(R.layout.error_dialog, null);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditIdeaActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                        View errorDialogView = LayoutInflater.from(EditIdeaActivity.this).inflate(R.layout.error_dialog, null);
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
            public void onFailure(Call<EditIdeaResponse> call, Throwable t) {
            }
        });
    }

    public void openIdeaDetails() {
        Intent intent = new Intent(EditIdeaActivity.this, IdeaDetailsActivity.class);
        startActivity(intent);
    }


}
