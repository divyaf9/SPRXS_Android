package com.divya.sprxs.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.CollaboratorsByIdeaForMilestoneRequest;
import com.divya.sprxs.model.CollaboratorsByIdeaForMilestoneResponse;
import com.divya.sprxs.model.CreateMilestonesRequest;
import com.divya.sprxs.model.CreateMilestonesResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ShowAvailableTokensToOfferRequest;
import com.divya.sprxs.model.ShowAvailableTokensToOfferResponse;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class CreateMilestonesActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText titleMilestone, descMilestone, dateMilestone, coinsMilestone;
    private TextView availableTokenMilestone;
    private ImageButton datePickerImageButton;
    private Spinner spinnerMilestone;
    private Button createMilestone, cancelMilestone, increaseCoinMilestone, decreaseCoinMilestone;
    private CreateMilestonesResponse createMilestonesResponse;
    private ShowAvailableTokensToOfferResponse showAvailableTokensToOfferResponse;
    private List<CollaboratorsByIdeaForMilestoneResponse> collaboratorsByIdeaForMilestoneResponses;
    List<String> collaboratorName = new ArrayList<>();
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int day, month, year, availableToken, mySpinnerValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_milestone);

        this.setTitle("Create Milestone");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleMilestone = findViewById(R.id.titleMilestone);
        descMilestone = findViewById(R.id.descMilestone);
        descMilestone.setMovementMethod(new ScrollingMovementMethod());
        dateMilestone = findViewById(R.id.dateMilestone);
        coinsMilestone = findViewById(R.id.coinsMilestone);
        availableTokenMilestone = findViewById(R.id.availableTokenMilestone);

        datePickerImageButton = findViewById(R.id.datePickerImageButton);
        datePickerImageButton.setOnClickListener(this);

        spinnerMilestone = findViewById(R.id.spinnerMilestone);

        createMilestone = findViewById(R.id.createMilestone);
        cancelMilestone = findViewById(R.id.cancelMilestone);
        increaseCoinMilestone = findViewById(R.id.increaseCoinMilestone);
        decreaseCoinMilestone = findViewById(R.id.decreaseCoinMilestone);
        createMilestone.setOnClickListener(this);
        cancelMilestone.setOnClickListener(this);
        increaseCoinMilestone.setOnClickListener(this);
        decreaseCoinMilestone.setOnClickListener(this);

        availableTokens();
        getCollaboratorsByIdeaForMilestone();

        collaboratorName.add(0,"Collaborators");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateMilestonesActivity.this, android.R.layout.simple_spinner_item, collaboratorName);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerMilestone.setAdapter(adapter);
        spinnerMilestone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mySpinnerValue = spinnerMilestone.getSelectedItemPosition();
                spinnerMilestone.setSelection(mySpinnerValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mySpinnerValue = 0;

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createMilestone:
                createMilestones();
                break;
            case R.id.cancelMilestone:
                onSupportNavigateUp();
                break;
            case R.id.increaseCoinMilestone:
                increaseCoin();
                break;
            case R.id.decreaseCoinMilestone:
                decreaseCoin();
                break;
            case R.id.datePickerImageButton:
                datePickerImage();
                break;
        }

    }


    private void createMilestones() {

        try {

            final String title = titleMilestone.getText().toString().trim();
            final String desc = descMilestone.getText().toString().trim();
            final String date = dateMilestone.getText().toString().trim();
            final String coins = coinsMilestone.getText().toString().trim();
            final int milestoneCoin = Integer.parseInt(coins);

            DateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            Date inputDate = inputDateFormat.parse(date);
            String outputDate = outputDateFormat.format(inputDate);

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            final String token = prefs.getString("token", null);
            final String refresh_token = prefs.getString("refresh_token", null);
            final String ideaId = getIntent().getStringExtra("ideaIdMilestone");


            Call<CreateMilestonesResponse> call;
            call = RetrofitClient.getInstance().getApi().createMilestones(
                    "Bearer " + token,
                    new CreateMilestonesRequest(0L, ideaId, title, desc, outputDate, milestoneCoin, false));
            call.enqueue(new Callback<CreateMilestonesResponse>() {
                @Override
                public void onResponse(Call<CreateMilestonesResponse> call, Response<CreateMilestonesResponse> response) {
                    if (response.code() == 201) {
                        createMilestonesResponse = response.body();
                        final View successDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.success_dialog, null);
                        final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
                        dialog.setContentView(R.layout.success_dialog);
                        TextView textView;
                        textView = successDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("You have created a new Milestone for Idea " + ideaId);
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

                        titleMilestone.getText().clear();
                        descMilestone.getText().clear();
                        dateMilestone.getText().clear();
                        spinnerMilestone.setSelection(0);

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
                                    final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
                            final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
                            final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
        final String ideaId = getIntent().getStringExtra("ideaIdMilestone");

        Call<ShowAvailableTokensToOfferResponse> call;
        call = RetrofitClient.getInstance().getApi().availableTokens(
                "Bearer " + token,
                new ShowAvailableTokensToOfferRequest(ideaId, ""));
        call.enqueue(new Callback<ShowAvailableTokensToOfferResponse>() {
            @Override
            public void onResponse(Call<ShowAvailableTokensToOfferResponse> call, Response<ShowAvailableTokensToOfferResponse> response) {
                if (response.code() == 200) {
                    showAvailableTokensToOfferResponse = response.body();
                    availableTokenMilestone.setText(String.valueOf(showAvailableTokensToOfferResponse.getAvailableTokens()));

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

                                final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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

    private void getCollaboratorsByIdeaForMilestone() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String ideaId = getIntent().getStringExtra("ideaIdMilestone");

        Call<List<CollaboratorsByIdeaForMilestoneResponse>> call;
        call = RetrofitClient.getInstance().getApi().getCollaboratorsByIdeaForMilestone(
                "Bearer " + token,
                new CollaboratorsByIdeaForMilestoneRequest(ideaId));
        call.enqueue(new Callback<List<CollaboratorsByIdeaForMilestoneResponse>>() {
            @Override
            public void onResponse(Call<List<CollaboratorsByIdeaForMilestoneResponse>> call, Response<List<CollaboratorsByIdeaForMilestoneResponse>> response) {
                if (response.code() == 200) {
                    collaboratorsByIdeaForMilestoneResponses = response.body();
                    for (int i = 0; i < collaboratorsByIdeaForMilestoneResponses.size(); i++) {
                        collaboratorName.add(collaboratorsByIdeaForMilestoneResponses.get(i).getFirstname() + " " + collaboratorsByIdeaForMilestoneResponses.get(i).getSurname());
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
                                getCollaboratorsByIdeaForMilestone();
                            } else {

                                final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(CreateMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(CreateMilestonesActivity.this);
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
            public void onFailure(Call<List<CollaboratorsByIdeaForMilestoneResponse>> call, Throwable t) {
            }
        });
    }


    private void increaseCoin() {

        final String coins = coinsMilestone.getText().toString().trim();
        int milestoneCoin = Integer.parseInt(coins);
        final String availableCoin = availableTokenMilestone.getText().toString().trim();
        availableToken = Integer.parseInt(availableCoin);

        if (milestoneCoin < availableToken) {
            milestoneCoin = milestoneCoin + 5;
            coinsMilestone.setText(String.valueOf(milestoneCoin));
        } else {
            Toast.makeText(CreateMilestonesActivity.this, "You dont have enough coins to offer", Toast.LENGTH_LONG).show();
        }
    }

    private void decreaseCoin() {

        final String coins = coinsMilestone.getText().toString().trim();
        int milestoneCoin = Integer.parseInt(coins);

        if (milestoneCoin > 0) {
            milestoneCoin = milestoneCoin - 5;
            coinsMilestone.setText(String.valueOf(milestoneCoin));
        }
    }

    private void datePickerImage() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(CreateMilestonesActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                String dateString = format.format(calendar.getTime());

                dateMilestone.setText(dateString);
            }
        }, year,month,day);
        datePickerDialog.show();
    }
}
