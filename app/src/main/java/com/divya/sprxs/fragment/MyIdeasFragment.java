package com.divya.sprxs.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapter;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.EndlessRecyclerOnScrollListener;
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


public class MyIdeasFragment extends Fragment {

    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private LinearLayoutManager layoutManager;
    private  Boolean isScrolling=false;
    private  int currentItems,totalItems,scrolledOutItems;
    private BottomNavigationView bottomNavigationView;
    private EndlessRecyclerOnScrollListener scrollListener;

    private List<MyIdeasSummaryResponse> myIdeasSummaryResponsedata;
    private List<MyIdeasSummaryResponse> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_my_ideas, container, false);

        getActivity().setTitle("My Ideas");
        recyclerView = v.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
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
//                                    Toast.makeText(getActivity(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
                                    View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
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
//                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
                                    View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
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
                        public void onFailure(Call<RefreshTokenResponse> call, Throwable t) {
                        }
                    });

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        Toast.makeText(getActivity(), jObjError.getString("error"), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
                        View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
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
//                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
                        View errorDialogView = LayoutInflater.from(getActivity()).inflate(R.layout.error_dialog, null);
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
            public void onFailure(Call<List<MyIdeasSummaryResponse>> call, Throwable t) {
            }
        });
    }
}
