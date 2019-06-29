package com.divya.sprxs.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.EditIdeaActivity;
import com.divya.sprxs.adapter.DataAdapter;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.controller.SwipeController;
import com.divya.sprxs.controller.SwipeControllerActions;
import com.divya.sprxs.model.MyIdeasSummaryRequest;
import com.divya.sprxs.model.MyIdeasSummaryResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class MyIdeasFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private List<MyIdeasSummaryResponse> myIdeasSummaryResponsedata;
    SwipeController swipeController = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().findViewById(R.id.helpImageView).setVisibility(View.INVISIBLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_my_ideas, container, false);
        getActivity().setTitle("My Ideas");
        recyclerView = v.findViewById(R.id.recycler_view);
        ideasSummary();
        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void ideasSummary() {
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<List<MyIdeasSummaryResponse>> call;
        call = RetrofitClient.getInstance().getApi().ideasSummary(
                "Bearer " + token,
                new MyIdeasSummaryRequest(""));
        call.enqueue(new Callback<List<MyIdeasSummaryResponse>>() {
            @Override
            public void onResponse(Call<List<MyIdeasSummaryResponse>> call, Response<List<MyIdeasSummaryResponse>> response) {
                if (response.code() == 200) {
                    myIdeasSummaryResponsedata = response.body();
                    if (myIdeasSummaryResponsedata.size() == 0) {
                        NoIdeasFragment noIdeasFragment = new NoIdeasFragment();
                        FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
                        fragTransaction.replace(R.id.myIdeasFragment, noIdeasFragment);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                    } else {
                        recyclerView.setNestedScrollingEnabled(false);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(layoutManager);
                        dataAdapter = new DataAdapter(getActivity(), myIdeasSummaryResponsedata, getContext());
                        recyclerView.setAdapter(dataAdapter);
                        dataAdapter.notifyDataSetChanged();
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
                                ideasSummary();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    final View successDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(getContext());
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
                                } catch (Exception e) {
                                    final View successDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(getContext());
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
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        final View successDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getContext());
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
                    } catch (Exception e) {
                        final View successDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getContext());
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
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MyIdeasSummaryResponse>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(View v) {

    }





}
