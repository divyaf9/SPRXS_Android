package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

    private TextView blockchainStatus, attachmentStatus,ideaStatus;
    private TextView ideaName_IdeaDetails, ideaId_IdeaDetails, dateText_IdeaDetails, ideaDescriptionText_IdeaDetails;
    private List<MyIdeasResponse> myIdeasResponsedata = null;
    private  String IdeaId,IdeaName,IdeaDescription;


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
//        attachmentStatus = findViewById(R.id.attachmentStatus);
        ideaStatus = findViewById(R.id.ideaStatus);
        ideaName_IdeaDetails = findViewById(R.id.ideaName_IdeaDetails);
        ideaId_IdeaDetails = findViewById(R.id.ideaId_IdeaDetails);
        dateText_IdeaDetails = findViewById(R.id.dateText_IdeaDetails);
        ideaDescriptionText_IdeaDetails = findViewById(R.id.ideaDescriptionText_IdeaDetails);
        ideaDescriptionText_IdeaDetails.setMovementMethod(new ScrollingMovementMethod());

        myIdeas();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editIdeaButton:
                goToEditIdea();
                break;
        }
    }



    public void myIdeas() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
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

                             IdeaId = myIdeasResponsedata.get(i).getIdeaUniqueID();
                             IdeaName= myIdeasResponsedata.get(i).getIdeaName();
                             IdeaDescription= myIdeasResponsedata.get(i).getIdeaDescription();

                            blockchainStatus.setText(myIdeasResponsedata.get(i).getTokenId());
                            ideaName_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaName());
                            ideaId_IdeaDetails.setText("#" + myIdeasResponsedata.get(i).getIdeaUniqueID());
                            ideaDescriptionText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDescription());
                            dateText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDateCreated());
//                            attachmentStatus.setText(myIdeasResponsedata.get(i).getIdeaFilepath());
                            if(myIdeasResponsedata.get(i).isAllowSearch()==true){
                                ideaStatus.setText("Public");
                            } else{
                                ideaStatus.setText("Private");
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
                                    final View successDialogView = LayoutInflater.from(IdeaDetailsActivity.this).inflate(R.layout.error_dialog, null);
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
                                } catch (Exception e) {
//                                    Toast.makeText(IdeaDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    final View successDialogView = LayoutInflater.from(IdeaDetailsActivity.this).inflate(R.layout.error_dialog, null);
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
                        final View successDialogView = LayoutInflater.from(IdeaDetailsActivity.this).inflate(R.layout.error_dialog, null);
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
                    } catch (Exception e) {
                        final View successDialogView = LayoutInflater.from(IdeaDetailsActivity.this).inflate(R.layout.error_dialog, null);
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
                    }

                }
            }

            @Override
            public void onFailure(Call<List<MyIdeasResponse>> call, Throwable t) {
            }
        });

    }
    private void goToEditIdea() {
        Intent intent = new Intent(IdeaDetailsActivity.this, EditIdeaActivity.class);
        intent.putExtra("myList",IdeaId);
        intent.putExtra("myListIdeaName",IdeaName);
        intent.putExtra("myListIdeaDesc",IdeaDescription);
        startActivity(intent);
    }


}
