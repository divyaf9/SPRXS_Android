
package com.divya.sprxs.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.LoginRequest;
import com.divya.sprxs.model.LoginResponse;
import com.divya.sprxs.model.ResetPasswordRequest;
import com.divya.sprxs.model.ResetPasswordResponse;

import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Button resetPasswordButton,cancelButton;
    private EditText emailTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);
        resetPasswordButton = findViewById(R.id.ResetPasswordbutton);
        cancelButton = findViewById(R.id.cancelButton);
        emailTextView = findViewById(R.id.emailTextView);
        resetPasswordButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);


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
            case R.id.ResetPasswordbutton:
                resetPasswordAPI();
                break;
            case R.id.cancelButton:
                onSupportNavigateUp();
                break;
        }

    }

    private void resetPasswordAPI(){
        String loginEmail = emailTextView.getText().toString().trim();

        if (loginEmail.isEmpty()) {
            emailTextView.setError("Email is required");
            emailTextView.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            emailTextView.setError("Enter a valid email");
            emailTextView.requestFocus();
            return;
        }
        else {
            Call<ResetPasswordResponse> call;
            progressBar.setVisibility(View.VISIBLE);

            call = RetrofitClient.getInstance().getApi().resetPassword(new ResetPasswordRequest(loginEmail));

            call.enqueue(new Callback<ResetPasswordResponse>() {
                @Override
                public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {



                    if (response.code() == 201) {
                        ResetPasswordResponse resetPasswordResponse = response.body();

                        final View successDialogView = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.success_dialog, null);
                        final Dialog dialog = new Dialog(ResetPasswordActivity.this);
                        dialog.setContentView(R.layout.success_dialog);
                        TextView textView;
                        textView = successDialogView.findViewById(R.id.dialogTextView);
                        textView.setText("If the account exists on our system, you will receive an email with your new password");
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

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            final View successDialogView = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(ResetPasswordActivity.this);
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
                            final View successDialogView = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.error_dialog, null);
                            final Dialog dialog = new Dialog(ResetPasswordActivity.this);
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
                public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}