package com.divya.sprxs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.MyIdeasRequest;
import com.divya.sprxs.model.MyIdeasResponse;
import com.divya.sprxs.model.RefreshTokenResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class IdeaDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView blockchainStatus, attachmentStatus;
    private ToggleButton ideaStatus;
    private TextView ideaName_IdeaDetails, ideaId_IdeaDetails, dateText_IdeaDetails, ideaDescriptionText_IdeaDetails;
    private ImageButton editIdeaButton;
    public static final String MY_IDEA_ID = "MyIdPrefsFile";
    private List<MyIdeasResponse> myIdeasResponsedata = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.ic_edit_black_24dp);
        imageView.setId(R.id.editIdeaButton);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100, 100, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
        imageView.setOnClickListener(this);
        this.setTitle("Idea Details");
        blockchainStatus = findViewById(R.id.blockchainStatus);
        attachmentStatus = findViewById(R.id.attachmentStatus);
        ideaStatus = (ToggleButton)findViewById(R.id.toggleButton);
        ideaName_IdeaDetails = findViewById(R.id.ideaName_IdeaDetails);
        ideaId_IdeaDetails = findViewById(R.id.ideaId_IdeaDetails);
        dateText_IdeaDetails = findViewById(R.id.dateText_IdeaDetails);
        ideaDescriptionText_IdeaDetails = findViewById(R.id.ideaDescriptionText_IdeaDetails);
        ideaDescriptionText_IdeaDetails.setMovementMethod(new ScrollingMovementMethod());

        ideaStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(IdeaDetailsActivity.this, "sample", Toast.LENGTH_LONG).show();
            }
        });


        myIdeas();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editIdeaButton:
                goToEditIdea();
                break;
        }
    }

    private void goToEditIdea() {
        Intent intent = new Intent(IdeaDetailsActivity.this, EditIdeaActivity.class);
        startActivity(intent);
    }

    public void changeIdeaStatus() {

    }

    public void myIdeas() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        SharedPreferences idPrefs = getApplicationContext().getSharedPreferences(MY_IDEA_ID, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_IDEA_ID, MODE_PRIVATE).edit();
        Call<List<MyIdeasResponse>> call;
        call = RetrofitClient.getInstance().getApi().myIdeas(
                "Bearer " + token,
                new MyIdeasRequest("", "", ""));

        call.enqueue(new Callback<List<MyIdeasResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<MyIdeasResponse>> call, Response<List<MyIdeasResponse>> response) {

                if (response.code() == 200) {
                    myIdeasResponsedata = response.body();

                    final String ideaId = getIntent().getStringExtra("myList");

                    for (int i = 0; i < myIdeasResponsedata.size(); i++)
                        if (ideaId.contentEquals(myIdeasResponsedata.get(i).getIdeaUniqueID())) {

                            blockchainStatus.setText(myIdeasResponsedata.get(i).getTokenId());
                            ideaName_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaName());
                            ideaId_IdeaDetails.setText("#" + myIdeasResponsedata.get(i).getIdeaUniqueID());
                            editor.putString("ideaId", myIdeasResponsedata.get(i).getIdeaUniqueID());
                            editor.apply();
                            ideaDescriptionText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDescription());
                            dateText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDateCreated());
                            attachmentStatus.setText(myIdeasResponsedata.get(i).getIdeaFilepath());
                            if (myIdeasResponsedata.get(i).isAllowSearch() == true) {
                                ideaStatus.setSelected(true);
                            } else {
                                ideaStatus.setSelected(false);
                            }
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
                                myIdeas();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    Toast.makeText(IdeaDetailsActivity.this, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(IdeaDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(IdeaDetailsActivity.this, jObjError.getString("error"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(IdeaDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<MyIdeasResponse>> call, Throwable t) {
            }
        });

    }

}
