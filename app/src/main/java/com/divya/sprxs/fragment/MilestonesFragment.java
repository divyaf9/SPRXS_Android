package com.divya.sprxs.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.CreateMilestonesActivity;
import com.divya.sprxs.adapter.DataAdapterMilestones;
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

public class MilestonesFragment extends Fragment {

    private Button createMilestoneButton;
    private RecyclerView recyclerViewMilestone;
    private DataAdapterMilestones dataAdapterMilestones;
    private List<ViewMilestonesResponse> viewMilestonesResponsesList;
    private String ideaId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_milestone, container, false);

        getActivity().setTitle("Milestones");

        ideaId = getActivity().getIntent().getStringExtra("myList");

        createMilestoneButton = v.findViewById(R.id.createMilestoneButton);
        createMilestoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateMilestonesActivity.class);
                intent.putExtra("ideaIdMilestone", ideaId);
                startActivity(intent);
            }
        });

        recyclerViewMilestone = v.findViewById(R.id.recycler_view_milestones);
        recyclerViewMilestone.setNestedScrollingEnabled(false);

        viewMilestones();

        return v;
    }

    private void viewMilestones() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<List<ViewMilestonesResponse>> call;
        call = RetrofitClient.getInstance().getApi().viewMilestones(
                "Bearer " + token,
                new ViewMilestonesRequest(0, ideaId, 0L, ""));
        call.enqueue(new Callback<List<ViewMilestonesResponse>>() {
            @Override
            public void onResponse(Call<List<ViewMilestonesResponse>> call, Response<List<ViewMilestonesResponse>> response) {
                if (response.code() == 200) {
                    viewMilestonesResponsesList = response.body();
                    if (viewMilestonesResponsesList.size() == 0) {
                        NoMilestonesFragment noMilestonesFragment = new NoMilestonesFragment();
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.replace(R.id.milestoneFragment, noMilestonesFragment);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    } else {
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerViewMilestone.setLayoutManager(layoutManager);
                        dataAdapterMilestones = new DataAdapterMilestones(getActivity(), viewMilestonesResponsesList, getContext());
                        recyclerViewMilestone.setAdapter(dataAdapterMilestones);
                        dataAdapterMilestones.notifyDataSetChanged();
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
