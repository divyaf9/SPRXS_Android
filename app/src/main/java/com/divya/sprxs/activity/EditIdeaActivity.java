package com.divya.sprxs.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.divya.sprxs.activity.IdeaDetailsActivity.MY_IDEA_ID;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class EditIdeaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ideaNameEditTextView, ideaDescriptionEditTextView, filenameEditTextView;
    private ImageView attachEditButton;
    private Button confirmEditButton;
    private TextView ideaIdEditTextView;
    private int mySpinnerValue;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idea);

        ideaNameEditTextView = findViewById(R.id.ideaNameEditTextView);
        ideaDescriptionEditTextView = findViewById(R.id.ideaDescriptionEditTextView);
        filenameEditTextView = findViewById(R.id.filenameEditTextView);
        ideaIdEditTextView = findViewById(R.id.ideaIdEditTextView);
        attachEditButton = findViewById(R.id.attachEditButton);
        attachEditButton.setOnClickListener(this);
        confirmEditButton = findViewById(R.id.confirmEditButton);
        confirmEditButton.setOnClickListener(this);
//        String ideaId = getIntent().getStringExtra("myList");
        SharedPreferences idPrefs = getSharedPreferences(MY_IDEA_ID, MODE_PRIVATE);
        final String ideaId = idPrefs.getString("ideaId", null);
        ideaIdEditTextView.setText(ideaId);


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
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        SharedPreferences idPrefs = getSharedPreferences(MY_IDEA_ID, MODE_PRIVATE);
        final String ideaId = idPrefs.getString("ideaId", null);


        Call<EditIdeaResponse> call;
        call = RetrofitClient.getInstance().getApi().editIdea(
                "Bearer " + token,
                new EditIdeaRequest(ideaId, mySpinnerValue, 2, 3, ideaName, ideaDescription, "", "", ""));
        call.enqueue(new Callback<EditIdeaResponse>() {
            @Override
            public void onResponse(Call<EditIdeaResponse> call, Response<EditIdeaResponse> response) {
                EditIdeaResponse editIdeaResponse = response.body();
                if (response.code() == 201) {
                    Toast.makeText(EditIdeaActivity.this, editIdeaResponse.getEditIdea_response(), Toast.LENGTH_SHORT).show();
                } else if (response.code() == 401) {
                    Call<RefreshTokenResponse> callrefresh;
                    callrefresh = RetrofitClient.getInstance().getApi().refreshToken(
                            "Bearer " + refresh_token);

                    callrefresh.enqueue(new Callback<RefreshTokenResponse>() {
                        @Override
                        public void onResponse(Call<RefreshTokenResponse> call, Response<RefreshTokenResponse> response) {
                            RefreshTokenResponse refreshTokenResponse = response.body();
                            editor.putString("token", refreshTokenResponse.getAccess_token());
                            editor.apply();
                            editIdea();
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(EditIdeaActivity.this, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EditIdeaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<EditIdeaResponse> call, Throwable t) {
            }
        });
    }

}
