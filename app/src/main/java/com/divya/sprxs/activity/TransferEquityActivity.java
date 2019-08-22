package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.CreateMilestonesRequest;
import com.divya.sprxs.model.CreateMilestonesResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ShowAvailableTokensToOfferRequest;
import com.divya.sprxs.model.ShowAvailableTokensToOfferResponse;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class TransferEquityActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView titleEquityTransfer, descEquityTransfer, dateEquityTransfer, availableTokenEquityTransfer, collaboratorEquityTransfer;
    private EditText coinsEquityTransfer;
    private Button transferEquity, cancelEquityTransfer, increaseCoinEquityTransfer, decreaseCoinEquityTransfer;
    private Switch completeEquityTransferSwitch;
    private int availableToken, approval;
    private Long profileId;
    private String name;
    private ShowAvailableTokensToOfferResponse showAvailableTokensToOfferResponse;
    private CreateMilestonesResponse createMilestonesResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_equity);

        this.setTitle("Transfer Equity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleEquityTransfer = findViewById(R.id.titleEquityTransfer);
        descEquityTransfer = findViewById(R.id.descEquityTransfer);
        dateEquityTransfer = findViewById(R.id.dateEquityTransfer);
        availableTokenEquityTransfer = findViewById(R.id.availableTokenEquityTransfer);
        collaboratorEquityTransfer = findViewById(R.id.collaboratorEquityTransfer);
        coinsEquityTransfer = findViewById(R.id.coinsEquityTransfer);
        transferEquity = findViewById(R.id.transferEquity);
        transferEquity.setOnClickListener(this);
        cancelEquityTransfer = findViewById(R.id.cancelEquityTransfer);
        cancelEquityTransfer.setOnClickListener(this);
        increaseCoinEquityTransfer = findViewById(R.id.increaseCoinEquityTransfer);
        increaseCoinEquityTransfer.setOnClickListener(this);
        decreaseCoinEquityTransfer = findViewById(R.id.decreaseCoinEquityTransfer);
        decreaseCoinEquityTransfer.setOnClickListener(this);
        completeEquityTransferSwitch = findViewById(R.id.completeEquityTransferSwitch);

        name = getIntent().getStringExtra("CollabNameEquityTransfer");
        profileId = getIntent().getLongExtra("profileIdEquityTransfer", 0L);

        collaboratorEquityTransfer.setText(name);

        titleEquityTransfer.setFocusable(false);
        titleEquityTransfer.setEnabled(false);
        titleEquityTransfer.setCursorVisible(false);
        titleEquityTransfer.setKeyListener(null);

        descEquityTransfer.setFocusable(false);
        descEquityTransfer.setEnabled(false);
        descEquityTransfer.setCursorVisible(false);
        descEquityTransfer.setKeyListener(null);

        availableTokenEquityTransfer.setFocusable(false);
        availableTokenEquityTransfer.setEnabled(false);
        availableTokenEquityTransfer.setCursorVisible(false);
        availableTokenEquityTransfer.setKeyListener(null);

        collaboratorEquityTransfer.setFocusable(false);
        collaboratorEquityTransfer.setEnabled(false);
        collaboratorEquityTransfer.setCursorVisible(false);
        collaboratorEquityTransfer.setKeyListener(null);

        dateEquityTransfer.setFocusable(false);
        dateEquityTransfer.setEnabled(false);
        dateEquityTransfer.setCursorVisible(false);
        dateEquityTransfer.setKeyListener(null);

        completeEquityTransferSwitch.setChecked(true);
        completeEquityTransferSwitch.setFocusable(false);
        completeEquityTransferSwitch.setEnabled(false);
        completeEquityTransferSwitch.setCursorVisible(false);
        completeEquityTransferSwitch.setKeyListener(null);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        String dateString = format.format(calendar.getTime());
        dateEquityTransfer.setText(dateString);


        availableTokens();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transferEquity:
                createMilestones();
                break;
            case R.id.cancelEquityTransfer:
                onSupportNavigateUp();
                break;
            case R.id.increaseCoinEquityTransfer:
                increaseCoin();
                break;
            case R.id.decreaseCoinEquityTransfer:
                decreaseCoin();
                break;
//            case R.id.completeEquityTransferSwitch:
//                completeMilestone();
//                break;
        }
    }


//    private void completeMilestone(){
//
//        if (completeEquityTransferSwitch.isChecked()) {
//
//            final View completeMilestoneDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.complete_milestone, null);
//            final Dialog dialog = new Dialog(TransferEquityActivity.this);
//            dialog.setContentView(R.layout.complete_milestone);
//            Button completeMilestone, cancelCompleteMilestone;
//            completeMilestone = completeMilestoneDialogView.findViewById(R.id.completeMilestone);
//            completeMilestone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    approval = 1;
//                    dialog.dismiss();
//                }
//            });
//            cancelCompleteMilestone = completeMilestoneDialogView.findViewById(R.id.cancelCompleteMilestone);
//            cancelCompleteMilestone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.setContentView(completeMilestoneDialogView);
//            dialog.show();
//        }
//    }


    private void createMilestones() {

        try {

            final String title = titleEquityTransfer.getText().toString().trim();
            final String desc = descEquityTransfer.getText().toString().trim();
            final String date = dateEquityTransfer.getText().toString().trim();
            final String coins = coinsEquityTransfer.getText().toString().trim();
            final int milestoneCoin = Integer.parseInt(coins);

            DateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            Date inputDate = inputDateFormat.parse(date);
            String outputDate = outputDateFormat.format(inputDate);

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            final String token = prefs.getString("token", null);
            final String refresh_token = prefs.getString("refresh_token", null);
            final String ideaId = getIntent().getStringExtra("ideaIdEquityTransfer");


            Call<CreateMilestonesResponse> call;
            call = RetrofitClient.getInstance().getApi().createMilestones(
                    "Bearer " + token,
                    new CreateMilestonesRequest(profileId, ideaId, title, desc, outputDate, milestoneCoin, true));
            call.enqueue(new Callback<CreateMilestonesResponse>() {
                @Override
                public void onResponse(Call<CreateMilestonesResponse> call, Response<CreateMilestonesResponse> response) {
                    if (response.code() == 201) {
                        createMilestonesResponse = response.body();
                        final View successDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.success_dialog, null);
                        final Dialog dialog = new Dialog(TransferEquityActivity.this);
                        dialog.setContentView(R.layout.success_dialog);
                        TextView textView;
                        textView = successDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("Milestone successfully created");
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
                                    createMilestones();
                                } else {
                                    final View errorDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(TransferEquityActivity.this);
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
                            final View errorDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(TransferEquityActivity.this);
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
                            final View errorDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(TransferEquityActivity.this);
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
                public void onFailure(Call<CreateMilestonesResponse> call, Throwable t) {
                }
            });
        } catch (Exception ex) {

        }
    }

    private void availableTokens() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String ideaId = getIntent().getStringExtra("ideaIdEquityTransfer");

        Call<ShowAvailableTokensToOfferResponse> call;
        call = RetrofitClient.getInstance().getApi().availableTokens(
                "Bearer " + token,
                new ShowAvailableTokensToOfferRequest(ideaId, ""));
        call.enqueue(new Callback<ShowAvailableTokensToOfferResponse>() {
            @Override
            public void onResponse(Call<ShowAvailableTokensToOfferResponse> call, Response<ShowAvailableTokensToOfferResponse> response) {
                if (response.code() == 200) {
                    ShowAvailableTokensToOfferResponse showAvailableTokensToOfferResponse = response.body();
                    availableTokenEquityTransfer.setText(String.valueOf(showAvailableTokensToOfferResponse.getAvailableTokens()));

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
                                availableTokens();
                            } else {

                                final View errorDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(TransferEquityActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(TransferEquityActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(TransferEquityActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(TransferEquityActivity.this);
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
            public void onFailure(Call<ShowAvailableTokensToOfferResponse> call, Throwable t) {
            }
        });
    }


    private void increaseCoin() {

        final String coins = coinsEquityTransfer.getText().toString().trim();
        int milestoneCoin = Integer.parseInt(coins);
        final String availableCoin = availableTokenEquityTransfer.getText().toString().trim();
        availableToken = Integer.parseInt(availableCoin);

        if (milestoneCoin < availableToken) {
            milestoneCoin = milestoneCoin + 5;
            coinsEquityTransfer.setText(String.valueOf(milestoneCoin));
        } else {
            Toast.makeText(TransferEquityActivity.this, "You dont have enough coins to offer", Toast.LENGTH_LONG).show();
        }
    }

    private void decreaseCoin() {

        final String coins = coinsEquityTransfer.getText().toString().trim();
        int milestoneCoin = Integer.parseInt(coins);

        if (milestoneCoin > 0) {
            milestoneCoin = milestoneCoin - 5;
            coinsEquityTransfer.setText(String.valueOf(milestoneCoin));
        }
    }

}
