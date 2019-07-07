package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.CreateProfileRequest;
import com.divya.sprxs.model.CreateProfileResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText firstNameTextView;
    private EditText lastNameTextView;
    private EditText emailTextView;
    private EditText confirmEmailTextView;
    private EditText passwordTextView;
    private EditText confirmPasswordTextView;
    private TextView termsTextView;
    private Button signupButton;
    private ProgressBar progressBar;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public final String firebasePassword = "ljsdlgkj&fefsd$%SDFsdf123£";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firstNameTextView = findViewById(R.id.firstNameTextView);
        lastNameTextView = findViewById(R.id.lastNameText);
        emailTextView = findViewById(R.id.emailTextView);
        confirmEmailTextView = findViewById(R.id.confirmEmailTextView);
        confirmPasswordTextView = findViewById(R.id.confirmPasswordTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        termsTextView = findViewById(R.id.termsTextView);
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);


        String text = "Privacy Policy";
        String text1 = "Terms and Conditions";
        String text2 = "Tokens and Collaboration Policy";
        SpannableString spannableString = new SpannableString(text);
        SpannableString spannableString1 = new SpannableString(text1);
        SpannableString spannableString2 = new SpannableString(text2);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        };

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        };

        spannableString.setSpan(clickableSpan, 1, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(clickableSpan1,1,14,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString2.setSpan(clickableSpan2,1,20,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTextView.setText(spannableString);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsTextView.setText(spannableString1);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsTextView.setText(spannableString2);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());



    }

    private void userSignup() {
        final String firstName = firstNameTextView.getText().toString().trim();
        final String lastName = lastNameTextView.getText().toString().trim();
        final String email_add = emailTextView.getText().toString().trim();
        final String confirmEmail = confirmEmailTextView.getText().toString();
        final String password = passwordTextView.getText().toString().trim();
        final String confirmPassword = confirmPasswordTextView.getText().toString().trim();

        if (firstName.isEmpty()) {
            firstNameTextView.setError("FirstName is required");
            firstNameTextView.requestFocus();
            return;
        } else if (lastName.isEmpty()) {
            lastNameTextView.setError("LastName is required");
            lastNameTextView.requestFocus();
            return;
        } else if (email_add.isEmpty()) {
            emailTextView.setError("Email is required");
            emailTextView.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_add).matches()) {
            emailTextView.setError("Enter a valid email");
            emailTextView.requestFocus();
            return;
        } else if (confirmEmail.isEmpty()) {
            confirmEmailTextView.setError("Confirm Email is required");
            confirmEmailTextView.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(confirmEmail).matches()) {
            confirmEmailTextView.setError("Enter a valid email");
            confirmEmailTextView.requestFocus();
            return;
        } else if (!email_add.contentEquals(confirmEmail)) {
            confirmEmailTextView.setError("Email Address does not match");
            confirmEmailTextView.requestFocus();
            return;
        } else if (password.isEmpty()) {
            passwordTextView.setError("Password required");
            passwordTextView.requestFocus();
            return;
        } else if (password.length() < 6) {
            passwordTextView.setError("Password too short, enter minimum 6 characters!");
            passwordTextView.requestFocus();
            return;
        } else if (confirmPassword.isEmpty()) {
            confirmPasswordTextView.setError("Confirm Password required");
            confirmPasswordTextView.requestFocus();
            return;
        } else if (!password.contentEquals(confirmPassword)) {
            confirmPasswordTextView.setError("Password did not match");
            confirmPasswordTextView.requestFocus();
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(email_add, firebasePassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                if (user != null) {
                                    final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    String UserUid = user.getUid();
                                    String email_add_firebase = emailTextView.getText().toString().trim();
                                    Call<CreateProfileResponse> call;
                                    progressBar.setVisibility(View.VISIBLE);

                                    call = RetrofitClient.getInstance().getApi().userSignup(new CreateProfileRequest(1, firstName, lastName, 0, 0, "0", email_add_firebase, password, confirmPassword, Boolean.TRUE, Boolean.FALSE, UserUid));
                                    call.enqueue(new Callback<CreateProfileResponse>() {
                                        @Override
                                        public void onResponse(Call<CreateProfileResponse> call, Response<CreateProfileResponse> response) {

                                            if (response.code() == 201) {
                                                CreateProfileResponse createProfileResponseResponse = response.body();
                                                editor.putString("token", createProfileResponseResponse.getToken());
                                                editor.putString("refresh_token", createProfileResponseResponse.getRefresh_token());
                                                editor.putString("email", email_add);
                                                editor.putString("firstname",firstName);
                                                editor.putString("surname", lastName);
                                                editor.apply();
                                                SharedPreferences sharedPreferences = getSharedPreferences("MySignup", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putBoolean("FirstSignup", true);
                                                editor.commit();
                                                editor.apply();
                                                openHome();
                                            } else {
                                                try {
                                                    final View successDialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.error_dialog, null);
                                                    final Dialog dialog = new Dialog(RegisterActivity.this);
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
                                                    final View successDialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.error_dialog, null);
                                                    final Dialog dialog = new Dialog(RegisterActivity.this);
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
                                        public void onFailure(Call<CreateProfileResponse> call, Throwable t) {
                                        }
                                    });
                                }

                            } else {
                                String message = "The email address is already in use by another account.";
                                if (task.getException().getMessage().equals(message)) {
                                    final View successDialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(RegisterActivity.this);
                                    dialog.setContentView(R.layout.error_dialog);
                                    TextView textView;
                                    textView = successDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText(message);
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
                                    Log.w("error", "createUserWithEmail:failure", task.getException());
                                    final View successDialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(RegisterActivity.this);
                                    dialog.setContentView(R.layout.error_dialog);
                                    TextView textView;
                                    textView = successDialogView.findViewById(R.id.dialogTextView);
                                    textView.setText("Authentication Failed\nPlease enter a correct credentials");
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
                                    updateUI(null);
                                }
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.e("UID:", user.getUid());
        } else {
            Log.e("Error:", "Invalid Firebase Uid");
        }
    }

    private void openHome() {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupButton:
                userSignup();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}