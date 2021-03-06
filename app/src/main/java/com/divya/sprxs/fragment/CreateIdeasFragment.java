package com.divya.sprxs.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.HomeActivity;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.CreateIdeasRequest;
import com.divya.sprxs.model.CreateIdeasResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class CreateIdeasFragment extends Fragment implements View.OnClickListener {


    private ProgressBar progressBar;
    private Button submitButton;
    private Button dismissButton;
    private ImageView attachButton;
    private String attachmentFile = null;
    private EditText ideaNameTextView;
    private EditText ideaDescriptionTextView;
    private TextView fileNameTextView;
    private Spinner spinner;
    private int mySpinnerValue;
    private Uri uri = null;
    private static final int PICK_FROM_GALLERY = 1;
    private String type;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_create_ideas, container, false);

        ideaNameTextView = v.findViewById(R.id.ideaNameTextView);
        ideaDescriptionTextView = v.findViewById(R.id.ideaDescriptionTextView);
        fileNameTextView = v.findViewById(R.id.filenameTextView);
        submitButton = v.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        dismissButton = v.findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(this);
        attachButton = v.findViewById(R.id.attachButton);
        attachButton.setOnClickListener(this);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        ideaDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());

        progressBar = v.findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        getActivity().setTitle("Create Idea");
        getActivity().findViewById(R.id.helpImageView).setVisibility(View.INVISIBLE);

        List<String> categories = new ArrayList<>();
        categories.add(0, "I have a");
        categories.add(1, "Technology Idea");
        categories.add(2, "Lifestyle & Wellbeing Idea");
        categories.add(3, "Food & Drink Idea");
        categories.add(4, "Gaming Idea");
        categories.add(5, "Business & Finance Idea");
        categories.add(6, "Art and Fashion Idea");
        categories.add(7, "Film Idea");
        categories.add(8, "Media & Journalism Idea");
        categories.add(9, "Theatre Idea");
        categories.add(10, "Music Idea");
        categories.add(11, "Other");

        spinner = v.findViewById(R.id.textSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mySpinnerValue = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mySpinnerValue = 0;

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    attachmentFile = String.valueOf(uri);
//                    ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
//                    MimeTypeMap mime = MimeTypeMap.getSingleton();
//                     type = mime.getExtensionFromMimeType(cR.getType(uri));
                    Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null);
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    fileNameTextView.setText(cursor.getString(nameIndex));
                    cursor.close();
                }
        }
    }


    public void onClick(View v1) {

        switch (v1.getId()) {
            case R.id.submitButton:
                createIdea();
                break;
            case R.id.dismissButton:
                myHome();
                break;
            case R.id.attachButton:
                openActivity();
                break;
        }
    }


    private void openActivity() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }


    public void myHome() {
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }


    public void createIdea() {

        final String ideaName = ideaNameTextView.getText().toString().trim();
        final String ideaDescription = ideaDescriptionTextView.getText().toString().trim();
        final String fileName = fileNameTextView.getText().toString().trim();

        if (ideaName.isEmpty()) {
            ideaNameTextView.setError("This Field is required");
            ideaNameTextView.requestFocus();
            return;
        } else if (ideaDescription.isEmpty()) {
            ideaDescriptionTextView.setError("This Field is required");
            ideaDescriptionTextView.requestFocus();
            return;
        }
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String AllUserFiles = "AllUsersFileData";
        final String ideasFolder = "myIdeas";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String UID = user.getUid();

        if (!fileName.isEmpty()) {

            StorageReference mStorageRef;

            mStorageRef = FirebaseStorage.getInstance().getReference();

            StorageReference riversRef = mStorageRef.child(AllUserFiles).child(UID).child(ideasFolder).child(fileName);

            riversRef.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Log.d("IMAGE UPLOAD", "SUCCESS");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.e("FAILED TO UPLOAD FILE ", "TO FIREBASE");
                        }
                    });

            Log.d("IMAGE PATH ", uri.getPath());
        }


        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        Call<CreateIdeasResponse> call;
        progressBar.setVisibility(View.VISIBLE);
        call = RetrofitClient.getInstance().getApi().createIdea(
                "Bearer " + token,
                new CreateIdeasRequest(mySpinnerValue, 2, 3, ideaName, ideaDescription, "", fileName, ""));

        call.enqueue(new Callback<CreateIdeasResponse>() {
            @Override
            public void onResponse(Call<CreateIdeasResponse> call, Response<CreateIdeasResponse> response) {

                CreateIdeasResponse createIdeasResponse = response.body();

                if (response.code() == 201) {
                    final View successDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("Idea successfully minted on blockchain with ID " + createIdeasResponse.getIdea_ID());
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
                    ideaNameTextView.getText().clear();
                    ideaDescriptionTextView.getText().clear();
                    fileNameTextView.setText("");
                    spinner.setSelection(0);
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
                                createIdea();
                            } else {
//                                try {
                                final View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(getContext());
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

//                                } catch (Exception e) {
//                                    final View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
//                                    final Dialog dialog = new Dialog(getContext());
//                                    dialog.setContentView(R.layout.error_dialog);
//                                    TextView textView;
//                                    textView = errorDialogView.findViewById(R.id.dialogTextView);
//                                    textView.setText("Technical Error\nPlease try again later");
//                                    Button button;
//                                    button = errorDialogView.findViewById(R.id.okButton);
//                                    button.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                                    dialog.setContentView(errorDialogView);
//                                    dialog.show();
//                                    progressBar.setVisibility(View.GONE);
//                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        final View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getContext());
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
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        final View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getContext());
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
            public void onFailure(Call<CreateIdeasResponse> call, Throwable t) {
            }
        });
    }


}
