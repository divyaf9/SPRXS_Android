package com.divya.sprxs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private Button signupButton;
    private Boolean isFirebaseAuthValid;
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
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
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
//            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
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
//            checkEmail();
            //create user
            mAuth.createUserWithEmailAndPassword(email_add, firebasePassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("error", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                if (user != null) {
                                    String UserUid = user.getUid();
                                    String email_add_firebase = emailTextView.getText().toString().trim();
                                    Call<CreateProfileResponse> call;
                                    call = RetrofitClient.getInstance().getApi().userSignup(new CreateProfileRequest(1, firstName, lastName, 0, 0, "0", email_add_firebase, password, confirmPassword, Boolean.TRUE, Boolean.FALSE, UserUid));

                                    call.enqueue(new Callback<CreateProfileResponse>() {


                                        @Override
                                        public void onResponse(Call<CreateProfileResponse> call, Response<CreateProfileResponse> response) {

                                            CreateProfileResponse createProfileResponseResponse = response.body();

                                            if (response.code() == 201) {
                                                if (createProfileResponseResponse.getCreateProfile_response().contentEquals("PASS")) {
                                                    openHome();
                                                }
                                            } else {
                                                try {
                                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                                    Toast.makeText(RegisterActivity.this, jObjError.getString("error"), Toast.LENGTH_LONG).show();
                                                } catch (Exception e) {
                                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                                if (task.getException().getMessage().equals(message))
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                else
                                    Log.w("error", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                                updateUI(null);
                                isFirebaseAuthValid = Boolean.FALSE;
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
        }
    }
}