package com.divya.sprxs.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterMilestones;
import com.divya.sprxs.adapter.DataAdapterMilestonesRequest;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ViewMilestonesRequest;
import com.divya.sprxs.model.ViewMilestonesResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class MilestonesRequestFragment extends Fragment {

    private RecyclerView recyclerViewMilestoneRequest;
    private DataAdapterMilestonesRequest dataAdapterMilestonesRequest;
    private List<ViewMilestonesResponse> viewMilestonesResponsesList;
    private String ideaId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_milestones_request, container, false);

        getActivity().setTitle("Milestone Requests");

        ideaId = getActivity().getIntent().getStringExtra("IdeaIdMyCollab");

        recyclerViewMilestoneRequest = v.findViewById(R.id.recycler_view_milestones_requests);
        recyclerViewMilestoneRequest.setHasFixedSize(false);
        recyclerViewMilestoneRequest.setNestedScrollingEnabled(false);

        viewMilestones();

        return v;
    }


    private void viewMilestones() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final Long profileId = prefs.getLong("id",0L);
        Call<List<ViewMilestonesResponse>> call;
        call = RetrofitClient.getInstance().getApi().viewMilestones(
                "Bearer " + token,
                new ViewMilestonesRequest(0, ideaId, profileId, ""));
        call.enqueue(new Callback<List<ViewMilestonesResponse>>() {
            @Override
            public void onResponse(Call<List<ViewMilestonesResponse>> call, Response<List<ViewMilestonesResponse>> response) {
                if (response.code() == 200) {
                    viewMilestonesResponsesList = response.body();
                    if (viewMilestonesResponsesList.size() == 0) {
                        NoMilestoneRequestsFragment noMilestoneRequestsFragment = new NoMilestoneRequestsFragment();
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.replace(R.id.milestoneRequestsFragment, noMilestoneRequestsFragment);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    } else {
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerViewMilestoneRequest.setLayoutManager(layoutManager);
                        dataAdapterMilestonesRequest = new DataAdapterMilestonesRequest(getActivity(), viewMilestonesResponsesList, getContext());
                        recyclerViewMilestoneRequest.setAdapter(dataAdapterMilestonesRequest);
                        dataAdapterMilestonesRequest.notifyDataSetChanged();
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
                                viewMilestones();
                            } else {
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

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        final View errorDialogView = LayoutInflater.from(getContext()).inflate(R.layout.error_dialog, null);
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
            public void onFailure(Call<List<ViewMilestonesResponse>> call, Throwable t) {
            }
        });

    }

}
