package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.Context;
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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.LoginRequest;
import com.divya.sprxs.model.LoginResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button loginButton;
    private EditText emailTextView, passwordTextView;
    private TextView forgotPasswordTextView;
    private TextView signupTextView;
    private ProgressBar progressBar;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public final String firebasePassword = "ljsdlgkj&fefsd$%SDFsdf123£";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);
        emailTextView = findViewById(R.id.emailTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        signupTextView = findViewById(R.id.signupTextView);
        loginButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        String text = "Don't have an account? Sign Up";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                goToSignUp();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            }
        };

        spannableString.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTextView.setText(spannableString);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance());


    }

    private void userLogin() {

        final String loginEmail = emailTextView.getText().toString().trim();
        final String loginPassword = passwordTextView.getText().toString().trim();


        if (loginEmail.isEmpty()) {
            emailTextView.setError("Email is required");
            emailTextView.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            emailTextView.setError("Enter a valid email");
            emailTextView.requestFocus();
            return;
        } else if (loginPassword.isEmpty()) {
            passwordTextView.setError("Password required");
            passwordTextView.requestFocus();
            return;
        } else {

            mAuth.signInWithEmailAndPassword(loginEmail, firebasePassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                if (user != null) {
                                    final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    Call<LoginResponse> call;

                                    progressBar.setVisibility(View.VISIBLE);

                                    call = RetrofitClient.getInstance().getApi().userLogin(new LoginRequest(loginEmail, loginPassword));

                                    call.enqueue(new Callback<LoginResponse>() {
                                        @Override
                                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                                            LoginResponse loginResponse = response.body();
                                            if (response.code() == 200) {
//                                                if (loginResponse.getProfile_type().contentEquals("1")) {
                                                    editor.putString("token", loginResponse.getToken());
                                                    editor.putString("refresh_token", loginResponse.getRefresh_token());
                                                    editor.putString("email", loginResponse.getLogin_email());
                                                    editor.putString("firstname", loginResponse.getLogin_firstname());
                                                    editor.putString("surname", loginResponse.getLogin_surname());
                                                    editor.putLong("id",loginResponse.getId());
                                                    editor.commit();
                                                    editor.apply();
                                                    SharedPreferences sharedPreferences = getSharedPreferences("MyLogin.txt", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putBoolean("FirstLogin", true);
                                                    editor.commit();
                                                    editor.apply();
                                                    myHome();
//                                                } else if (loginResponse.getProfile_type().contentEquals("2")) {
//                                                    editor.putString("token", loginResponse.getToken());
//                                                    editor.apply();
//                                                    loggedIn();
//                                                }
                                            } else {
                                                try {
                                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                    final View errorDialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.error_dialog, null);
                                                    final Dialog dialog = new Dialog(LoginActivity.this);
                                                    dialog.setContentView(R.layout.error_dialog);
                                                    TextView textView;
                                                    textView = errorDialogView.findViewById(R.id.dialogTextView);
                                                    textView.setText(jObjError.getString("login_message"));
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
                                                    progressBar.setVisibility(View.GONE);
                                                } catch (Exception e) {
                                                    final View errorDialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.error_dialog, null);
                                                    final Dialog dialog = new Dialog(LoginActivity.this);
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
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                                        }
                                    });
                                }


                            } else {
                                final View errorDialogView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(LoginActivity.this);
                                dialog.setContentView(R.layout.error_dialog);
                                TextView textView;
                                textView = errorDialogView.findViewById(R.id.dialogTextView);
                                textView.setText(task.getException().getMessage());
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
                                progressBar.setVisibility(View.GONE);
                                updateUI(null);
                            }

                        }
                    });

        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                userLogin();
                break;
            case R.id.forgotPasswordTextView:
                goToForgetPassword();
                break;
        }
    }


    public void myHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        progressBar.setVisibility(View.GONE);
        finish();
    }

    public void goToForgetPassword() {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void loggedIn() {
        Intent intent = new Intent(LoginActivity.this, LoggedIn.class);
        startActivity(intent);
        finish();
    }

    public void goToSignUp() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void updateUI(FirebaseUser user) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();
        }
    }


}