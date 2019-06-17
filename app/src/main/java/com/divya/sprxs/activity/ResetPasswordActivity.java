
package com.divya.sprxs.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private Button resetPasswordButton;
    private EditText emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetPasswordButton = findViewById(R.id.ResetPasswordbutton);
        emailTextView = findViewById(R.id.emailTextView);
        resetPasswordButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        resetPasswordAPI();
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
            call = RetrofitClient.getInstance().getApi().resetPassword(new ResetPasswordRequest(loginEmail));

            call.enqueue(new Callback<ResetPasswordResponse>() {
                @Override
                public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {



                    if (response.code() == 201) {
                        ResetPasswordResponse resetPasswordResponse = response.body();
//                            Toast.makeText(ResetPasswordActivity.this, resetPasswordResponse.getResetPW_message(), Toast.LENGTH_LONG).show();

                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                            View successDialogView = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.success_dialog, null);
                            TextView textView;
                            textView = successDialogView.findViewById(R.id.dialogTextView);
                            textView.setText("If the account exists on our system, you will receive an email with your new password");
                            String positiveText = getString(android.R.string.ok);
                            builder.setPositiveButton(positiveText,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setView(successDialogView);
                            builder.show();

                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
//                            Toast.makeText(ResetPasswordActivity.this, jObjError.getString("getResetPW_message"), Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                            View errorDialogView = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.error_dialog, null);
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
                        } catch (Exception e) {
//                            Toast.makeText(ResetPasswordActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                            View errorDialogView = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.error_dialog, null);
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