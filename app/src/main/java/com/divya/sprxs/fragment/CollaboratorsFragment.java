package com.divya.sprxs.fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterCollaborators;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.ShowAvailableTokensToOfferRequest;
import com.divya.sprxs.model.ShowAvailableTokensToOfferResponse;
import com.divya.sprxs.model.ShowEquityForIdeaRequest;
import com.divya.sprxs.model.ShowEquityForIdeaResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;


public class CollaboratorsFragment extends Fragment {

    private TextView ideaNameCollaboratorTextView, equityAvailable;
    private ImageView coinImage;
    private RecyclerView recyclerViewCollaborator;
    private DataAdapterCollaborators dataAdapterCollaborators;
    private List<ShowEquityForIdeaResponse> showEquityForIdeaResponses;
    private ShowAvailableTokensToOfferResponse showAvailableTokensToOfferResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_collaborator, container, false);

        getActivity().setTitle("Collaborators");

        ideaNameCollaboratorTextView = v.findViewById(R.id.ideaNameCollaboratorTextView);
        equityAvailable = v.findViewById(R.id.equityAvailable);
        coinImage = v.findViewById(R.id.coinImage);

        recyclerViewCollaborator = v.findViewById(R.id.recycler_view_collaborator);
        recyclerViewCollaborator.setHasFixedSize(true);

        ObjectAnimator animation = ObjectAnimator.ofFloat(coinImage, "rotationY", 0.5f, 360f);
        animation.setDuration(9000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();

        availableTokens();
        collaborators();
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void availableTokens() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String ideaId = getActivity().getIntent().getStringExtra("myList");

        Call<ShowAvailableTokensToOfferResponse> call;
        call = RetrofitClient.getInstance().getApi().availableTokens(
                "Bearer " + token,
                new ShowAvailableTokensToOfferRequest(ideaId, ""));
        call.enqueue(new Callback<ShowAvailableTokensToOfferResponse>() {
            @Override
            public void onResponse(Call<ShowAvailableTokensToOfferResponse> call, Response<ShowAvailableTokensToOfferResponse> response) {
                if (response.code() == 200) {
                    showAvailableTokensToOfferResponse = response.body();
                    equityAvailable.setText(String.valueOf(showAvailableTokensToOfferResponse.getAvailableTokens()));

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
                                availableTokens();
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
                    }
                }
            }

            @Override
            public void onFailure(Call<ShowAvailableTokensToOfferResponse> call, Throwable t) {
            }
        });
    }

    public void collaborators() {

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final String ideaId = getActivity().getIntent().getStringExtra("myList");
        final String ideaName = getActivity().getIntent().getStringExtra("myListIdeaName");

        ideaNameCollaboratorTextView.setText(ideaName);

        Call<List<ShowEquityForIdeaResponse>> call;
        call = RetrofitClient.getInstance().getApi().collaborators(
                "Bearer " + token,
                new ShowEquityForIdeaRequest(ideaId));
        call.enqueue(new Callback<List<ShowEquityForIdeaResponse>>() {
            @Override
            public void onResponse(Call<List<ShowEquityForIdeaResponse>> call, Response<List<ShowEquityForIdeaResponse>> response) {
                if (response.code() == 200) {
                    showEquityForIdeaResponses = response.body();

                    recyclerViewCollaborator.setHasFixedSize(false);
                    recyclerViewCollaborator.setNestedScrollingEnabled(false);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewCollaborator.setLayoutManager(layoutManager);
                    dataAdapterCollaborators = new DataAdapterCollaborators(getActivity(), showEquityForIdeaResponses, getContext());
                    recyclerViewCollaborator.setAdapter(dataAdapterCollaborators);
                    dataAdapterCollaborators.notifyDataSetChanged();

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
                                collaborators();
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
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ShowEquityForIdeaResponse>> call, Throwable t) {
            }
        });
    }


}
