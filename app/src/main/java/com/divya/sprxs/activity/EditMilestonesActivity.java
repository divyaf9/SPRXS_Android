package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.CollaboratorsByIdeaForMilestoneRequest;
import com.divya.sprxs.model.CollaboratorsByIdeaForMilestoneResponse;
import com.divya.sprxs.model.CreateMilestonesRequest;
import com.divya.sprxs.model.CreateMilestonesResponse;
import com.divya.sprxs.model.EditMilestonesRequest;
import com.divya.sprxs.model.EditMilestonesResponse;
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

public class EditMilestonesActivity extends AppCompatActivity implements View.OnClickListener {

private EditText titleEditMilestone, descEditMilestone, dateEditMilestone, coinsEditMilestone;
private TextView availableTokenEditMilestone;
private ImageButton datePickerEditImageButton;
private Spinner spinnerEditMilestone;
private Switch completeEditMilestoneSwitch;
private Button createEditMilestone, cancelEditMilestone, increaseCoinEditMilestone, decreaseCoinEditMilestone;
private EditMilestonesResponse editMilestonesResponse;
private ShowAvailableTokensToOfferResponse showAvailableTokensToOfferResponse;
private List<CollaboratorsByIdeaForMilestoneResponse> collaboratorsByIdeaForMilestoneResponses;
private List<String> collaboratorName = new ArrayList<>();
private Calendar calendar;
private DatePickerDialog datePickerDialog;
private int day, month, year, availableToken, mySpinnerValue,approval;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_milestone);

        this.setTitle("Edit Milestones");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        titleEditMilestone = findViewById(R.id.titleEditMilestone);
        descEditMilestone = findViewById(R.id.descEditMilestone);
        descEditMilestone.setMovementMethod(new ScrollingMovementMethod());
        dateEditMilestone = findViewById(R.id.dateEditMilestone);
        coinsEditMilestone = findViewById(R.id.coinsEditMilestone);
        availableTokenEditMilestone = findViewById(R.id.availableTokenEditMilestone);

        datePickerEditImageButton = findViewById(R.id.datePickerEditImageButton);
        datePickerEditImageButton.setOnClickListener(this);

        spinnerEditMilestone = findViewById(R.id.spinnerEditMilestone);

        completeEditMilestoneSwitch = findViewById(R.id.completeEditMilestoneSwitch);
        completeEditMilestoneSwitch.setOnClickListener(this);

        createEditMilestone = findViewById(R.id.createEditMilestone);
        cancelEditMilestone = findViewById(R.id.cancelEditMilestone);
        increaseCoinEditMilestone = findViewById(R.id.increaseCoinEditMilestone);
        decreaseCoinEditMilestone = findViewById(R.id.decreaseCoinEditMilestone);
        createEditMilestone.setOnClickListener(this);
        cancelEditMilestone.setOnClickListener(this);
        increaseCoinEditMilestone.setOnClickListener(this);
        decreaseCoinEditMilestone.setOnClickListener(this);

        coinsEditMilestone.setFocusable(false);
        coinsEditMilestone.setEnabled(false);
        coinsEditMilestone.setCursorVisible(false);
        coinsEditMilestone.setKeyListener(null);

        spinnerEditMilestone.setFocusable(false);
        spinnerEditMilestone.setEnabled(false);

        increaseCoinEditMilestone.setFocusable(false);
        increaseCoinEditMilestone.setEnabled(false);
        increaseCoinEditMilestone.setCursorVisible(false);
        increaseCoinEditMilestone.setKeyListener(null);

        decreaseCoinEditMilestone.setFocusable(false);
        decreaseCoinEditMilestone.setEnabled(false);
        decreaseCoinEditMilestone.setCursorVisible(false);
        decreaseCoinEditMilestone.setKeyListener(null);

        final String title = getIntent().getStringExtra("titleMilestoneAdapter");
        final String desc = getIntent().getStringExtra("DescMilestoneAdapter");
        final String date = getIntent().getStringExtra("DateMilestoneAdapter");
        titleEditMilestone.setText(title);
        descEditMilestone.setText(desc);
        dateEditMilestone.setText(date);

        availableTokens();
        getCollaboratorsByIdeaForMilestone();

        collaboratorName.add(0,"Collaborators");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditMilestonesActivity.this, android.R.layout.simple_spinner_item, collaboratorName);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerEditMilestone.setAdapter(adapter);
        spinnerEditMilestone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mySpinnerValue = spinnerEditMilestone.getSelectedItemPosition();
                spinnerEditMilestone.setSelection(mySpinnerValue);
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
            case R.id.createEditMilestone:
                editMilestones();
                break;
            case R.id.cancelEditMilestone:
                onSupportNavigateUp();
                break;
            case R.id.increaseCoinEditMilestone:
                increaseCoin();
                break;
            case R.id.decreaseCoinEditMilestone:
                decreaseCoin();
                break;
            case R.id.datePickerEditImageButton:
                datePickerImage();
                break;
            case R.id.completeEditMilestoneSwitch:
                completeMilestone();
                break;
        }

    }


    private void completeMilestone(){

        if(completeEditMilestoneSwitch.isChecked()) {

            final View completeMilestoneDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.complete_milestone, null);
            final Dialog dialog = new Dialog(EditMilestonesActivity.this);
            dialog.setContentView(R.layout.complete_milestone);
            Button completeMilestone, cancelCompleteMilestone;
            completeMilestone = completeMilestoneDialogView.findViewById(R.id.completeMilestone);
            completeMilestone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approval = 1;
                    dialog.dismiss();
                }
            });
            cancelCompleteMilestone = completeMilestoneDialogView.findViewById(R.id.cancelCompleteMilestone);
            cancelCompleteMilestone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approval = getIntent().getIntExtra("approvalMilestoneAdapter", 0);
                    completeEditMilestoneSwitch.setChecked(false);
                    dialog.dismiss();
                }
            });
            dialog.setContentView(completeMilestoneDialogView);
            dialog.show();
        } else{
            approval = getIntent().getIntExtra("approvalMilestoneAdapter",0);
        }
    }

    private void editMilestones() {

        try {

            final String title = titleEditMilestone.getText().toString().trim();
            final String desc = descEditMilestone.getText().toString().trim();
            final String date = dateEditMilestone.getText().toString().trim();
            final String coins = coinsEditMilestone.getText().toString().trim();
            final int milestoneCoin = Integer.parseInt(coins);

            DateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            Date inputDate = inputDateFormat.parse(date);
            String outputDate = outputDateFormat.format(inputDate);

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            final String token = prefs.getString("token", null);
            final String refresh_token = prefs.getString("refresh_token", null);
            final String ideaId = getIntent().getStringExtra("ideaIdMilestoneAdapter");
            final int id = getIntent().getIntExtra("idMilestoneAdapter",0);

            Call<EditMilestonesResponse> call;
            call = RetrofitClient.getInstance().getApi().editMilestones(
                    "Bearer " + token,
                    new EditMilestonesRequest(id,approval,ideaId,title,desc,outputDate));
            call.enqueue(new Callback<EditMilestonesResponse>() {
                @Override
                public void onResponse(Call<EditMilestonesResponse> call, Response<EditMilestonesResponse> response) {
                    if (response.code() == 200) {
                        editMilestonesResponse = response.body();
                            final View successDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.success_dialog, null);
                            final Dialog dialog = new Dialog(EditMilestonesActivity.this);
                            dialog.setContentView(R.layout.success_dialog);
                            TextView textView;
                            textView = successDialogView.findViewById(R.id.dialogTextView);
                            textView.setText("You have edited a Milestone for Idea " + ideaId);
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
                                    editMilestones();
                                } else {
                                    final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                            final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                            final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                public void onFailure(Call<EditMilestonesResponse> call, Throwable t) {
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
        final String ideaId = getIntent().getStringExtra("ideaIdMilestoneAdapter");

        Call<ShowAvailableTokensToOfferResponse> call;
        call = RetrofitClient.getInstance().getApi().availableTokens(
                "Bearer " + token,
                new ShowAvailableTokensToOfferRequest(ideaId, ""));
        call.enqueue(new Callback<ShowAvailableTokensToOfferResponse>() {
            @Override
            public void onResponse(Call<ShowAvailableTokensToOfferResponse> call, Response<ShowAvailableTokensToOfferResponse> response) {
                if (response.code() == 200) {
                    showAvailableTokensToOfferResponse = response.body();
                    availableTokenEditMilestone.setText(String.valueOf(showAvailableTokensToOfferResponse.getAvailableTokens()));

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

                                final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
        final String ideaId = getIntent().getStringExtra("ideaIdMilestoneAdapter");

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

                                final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(EditMilestonesActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(EditMilestonesActivity.this);
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

        final String coins = coinsEditMilestone.getText().toString().trim();
        int milestoneCoin = Integer.parseInt(coins);
        final String availableCoin = availableTokenEditMilestone.getText().toString().trim();
        availableToken = Integer.parseInt(availableCoin);

        if (milestoneCoin < availableToken) {
            milestoneCoin = milestoneCoin + 5;
            coinsEditMilestone.setText(String.valueOf(milestoneCoin));
        } else {
            Toast.makeText(EditMilestonesActivity.this, "You dont have enough coins to offer", Toast.LENGTH_LONG).show();
        }
    }

    private void decreaseCoin() {

        final String coins = coinsEditMilestone.getText().toString().trim();
        int milestoneCoin = Integer.parseInt(coins);

        if (milestoneCoin > 0) {
            milestoneCoin = milestoneCoin - 5;
            coinsEditMilestone.setText(String.valueOf(milestoneCoin));
        }
    }

    private void datePickerImage() {

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(EditMilestonesActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                String dateString = format.format(calendar.getTime());

                dateEditMilestone.setText(dateString);
            }
        },year,month,day);
        datePickerDialog.show();
    }


}
