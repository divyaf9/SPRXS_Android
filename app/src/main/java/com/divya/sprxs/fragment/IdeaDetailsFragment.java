package com.divya.sprxs.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

//import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.request.RequestOptions;
import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.MyIdeasRequest;
import com.divya.sprxs.model.MyIdeasResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class IdeaDetailsFragment extends Fragment  {

    private TextView blockchainStatus, attachmentStatus, ideaStatus;
    private TextView ideaName_IdeaDetails, ideaId_IdeaDetails, dateText_IdeaDetails, ideaDescriptionText_IdeaDetails;
    private List<MyIdeasResponse> myIdeasResponsedata = null;
    private String IdeaId, IdeaName, IdeaDescription;
    private ImageView coverPhotoImageView;
    private Glide GlideApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idea_details, container, false);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
//        ImageView imageView = new ImageView(actionBar.getThemedContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER);
//        imageView.setImageResource(R.drawable.ic_edit_black_24dp);
//        imageView.setId(R.id.editIdeaButton);
//        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(100, 100, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
//        layoutParams.rightMargin = 40;
//        imageView.setLayoutParams(layoutParams);
//        actionBar.setCustomView(imageView);
//        imageView.setOnClickListener(this);
//
        getActivity().setTitle("Idea Details");
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

//        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_idea);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().onBackPressed();
//            }
//        });


        blockchainStatus = v.findViewById(R.id.blockchainStatus);
        ideaStatus = v.findViewById(R.id.ideaStatus);
        ideaName_IdeaDetails = v.findViewById(R.id.ideaName_IdeaDetails);
        ideaId_IdeaDetails = v.findViewById(R.id.ideaId_IdeaDetails);
        dateText_IdeaDetails = v.findViewById(R.id.dateText_IdeaDetails);
        coverPhotoImageView = v.findViewById(R.id.coverPhotoImageView);
        ideaDescriptionText_IdeaDetails = v.findViewById(R.id.ideaDescriptionText_IdeaDetails);
        ideaDescriptionText_IdeaDetails.setMovementMethod(new ScrollingMovementMethod());
        myIdeas();
        return v;
    }

    public void myIdeas() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        Call<List<MyIdeasResponse>> call;
        call = RetrofitClient.getInstance().getApi().myIdeas(
                "Bearer " + token,
                new MyIdeasRequest("", "", ""));
        call.enqueue(new Callback<List<MyIdeasResponse>>() {
            @Override
            public void onResponse(Call<List<MyIdeasResponse>> call, Response<List<MyIdeasResponse>> response) {
                if (response.code() == 200) {
                    myIdeasResponsedata = response.body();
                    final String ideaId= getActivity().getIntent().getStringExtra("myList");
                    for (int i = 0; i < myIdeasResponsedata.size(); i++) {
                        if (ideaId.contentEquals(myIdeasResponsedata.get(i).getIdeaUniqueID())) {
                            IdeaId = myIdeasResponsedata.get(i).getIdeaUniqueID();
                            IdeaName = myIdeasResponsedata.get(i).getIdeaName();
                            IdeaDescription = myIdeasResponsedata.get(i).getIdeaDescription();
                            blockchainStatus.setText(myIdeasResponsedata.get(i).getTokenId());
                            ideaName_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaName());
                            ideaId_IdeaDetails.setText("#" + myIdeasResponsedata.get(i).getIdeaUniqueID());
                            ideaDescriptionText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDescription());
                            dateText_IdeaDetails.setText(myIdeasResponsedata.get(i).getAndroidDate());
                            String coverPhoto = myIdeasResponsedata.get(i).getCoverPhoto();
                            if (myIdeasResponsedata.get(i).isAllowSearch() == true) {
                                ideaStatus.setText("Public");
                            } else {
                                ideaStatus.setText("Private");
                            }

                            if(myIdeasResponsedata.get(i).getCoverPhoto()== (null)){
                                coverPhotoImageView.setImageResource(R.drawable.other);
                                coverPhotoImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                            }else if ((myIdeasResponsedata.get(i).getCoverPhoto()).equals("")){
                                coverPhotoImageView.setImageResource(R.drawable.other);
                                coverPhotoImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            } else{
                                final String AllUserFiles = "AllUsersFileData";
                                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String UID = user.getUid();
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                final StorageReference pathReference = storageReference.child(AllUserFiles).child(UID).child("coverPhoto").child(coverPhoto);
                                pathReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            GlideApp.with(getContext())
                                                    .load(task.getResult())
                                                    .into(coverPhotoImageView);
                                            coverPhotoImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                                        } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                            }

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
//                                try {
//                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                final View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(getActivity());
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
//                                } catch (Exception e) {
//                                    final View errorDialogView = LayoutInflater.from(IdeaDetailsActivity.this).inflate(R.layout.error_dialog, null);
//                                    final Dialog dialog = new Dialog(IdeaDetailsActivity.this);
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
                        final Dialog dialog = new Dialog(getActivity());
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
                        final View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getActivity());
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
            public void onFailure(Call<List<MyIdeasResponse>> call, Throwable t) {
            }
        });
    }

//    private void goToEditIdea() {
//        Intent intent = new Intent(getActivity(), EditIdeaActivity.class);
//        intent.putExtra("myList", IdeaId);
//        intent.putExtra("myListIdeaName", IdeaName);
//        intent.putExtra("myListIdeaDesc", IdeaDescription);
//        startActivity(intent);
//    }

}
