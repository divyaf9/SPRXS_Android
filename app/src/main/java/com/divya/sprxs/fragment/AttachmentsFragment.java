package com.divya.sprxs.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterAttachments;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.IdeaFilesRequest;
import com.divya.sprxs.model.IdeaFilesResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class AttachmentsFragment extends Fragment {

    private ArrayList<String> attachmentsArrayList = new ArrayList<>();
    private List<IdeaFilesResponse> ideaFilesResponseList;
    private RecyclerView recyclerViewAttachments;
    private DataAdapterAttachments dataAdapterAttachments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_attachments, container, false);

        getActivity().setTitle("Attachments");

        recyclerViewAttachments = v.findViewById(R.id.recycler_view_attachments);

        ideaFiles();

        return v;
    }

    private void ideaFiles() {

        final String ideaId = getActivity().getIntent().getStringExtra("myList");

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);

        Call<List<IdeaFilesResponse>> call;
        call = RetrofitClient.getInstance().getApi().ideaFiles(
                "Bearer " + token,
                new IdeaFilesRequest(ideaId));
        call.enqueue(new Callback<List<IdeaFilesResponse>>() {
            @Override
            public void onResponse(Call<List<IdeaFilesResponse>> call, Response<List<IdeaFilesResponse>> response) {
                if (response.code() == 200) {
                    ideaFilesResponseList = response.body();
                    if (ideaFilesResponseList.size() == 1) {
                        NoAttachmentsFragment noAttachmentsFragments = new NoAttachmentsFragment();
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.replace(R.id.attachmentsFragment, noAttachmentsFragments);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    }else{
                        recyclerViewAttachments.setHasFixedSize(false);
                        recyclerViewAttachments.setNestedScrollingEnabled(false);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerViewAttachments.setLayoutManager(layoutManager);
                        dataAdapterAttachments = new DataAdapterAttachments(getActivity(), ideaFilesResponseList, getContext());
                        recyclerViewAttachments.setAdapter(dataAdapterAttachments);
                        dataAdapterAttachments.notifyDataSetChanged();
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
                                ideaFiles();
                            } else {
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
//                        textView.setText("Technical Error\nPlease try again later");
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
            public void onFailure(Call<List<IdeaFilesResponse>> call, Throwable t) {
            }
        });
    }

}
