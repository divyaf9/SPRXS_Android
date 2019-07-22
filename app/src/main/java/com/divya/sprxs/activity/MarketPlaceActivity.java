package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterMarketPlace;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.MarketPlaceResponse;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.Skills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class MarketPlaceActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerViewMarketPlace;
    private DataAdapterMarketPlace dataAdapterMarketPlace;
    private List<MarketPlaceResponse> marketPlaceResponseList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place);

        this.setTitle("MarketPlace");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerViewMarketPlace = findViewById(R.id.recycler_view_market_place);
        recyclerViewMarketPlace.setNestedScrollingEnabled(false);

        progressBar = findViewById(R.id.loadingPanel);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FD7E14"), PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        marketPlace();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_idea_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.searchIdeamenu);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                R.id.search_src_text)).setHint("Search skills");
        ((EditText) searchView.findViewById(
                R.id.search_src_text)).setHintTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<MarketPlaceResponse> filtermodelist = filter(marketPlaceResponseList, newText);
                dataAdapterMarketPlace.setfilter(filtermodelist);
                return true;
            }
        });
        return true;
    }

    private List<MarketPlaceResponse> filter(List<MarketPlaceResponse> pl, String query) {
        query = query.toLowerCase();
        final List<MarketPlaceResponse> filteredModeList = new ArrayList<>();
            for (MarketPlaceResponse model : pl) {
                for (int j = 0; j < model.getSkills().size(); j++) {
                    final String text = model.getSkills().get(j).getSkillName().toLowerCase();
                    if (text.startsWith(query)) {
                        filteredModeList.add(model);
                        break;
                    }
                }
            }
        return filteredModeList;
    }

    //for changing the text color of searchview
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }


    private void marketPlace() {

        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<List<MarketPlaceResponse>> call;
        call = RetrofitClient.getInstance().getApi().marketPlace(
                "Bearer " + token);
        call.enqueue(new Callback<List<MarketPlaceResponse>>() {
            @Override
            public void onResponse(Call<List<MarketPlaceResponse>> call, Response<List<MarketPlaceResponse>> response) {
                if (response.code() == 200) {
                    marketPlaceResponseList = response.body();
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MarketPlaceActivity.this);
                    recyclerViewMarketPlace.setLayoutManager(layoutManager);
                    dataAdapterMarketPlace = new DataAdapterMarketPlace(MarketPlaceActivity.this, marketPlaceResponseList, getApplicationContext());
                    recyclerViewMarketPlace.setAdapter(dataAdapterMarketPlace);
                    dataAdapterMarketPlace.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

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
                                marketPlace();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    final View successDialogView = LayoutInflater.from(MarketPlaceActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(MarketPlaceActivity.this);
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
                                    final View successDialogView = LayoutInflater.from(MarketPlaceActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(MarketPlaceActivity.this);
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
                        final View successDialogView = LayoutInflater.from(MarketPlaceActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MarketPlaceActivity.this);
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
                        final View successDialogView = LayoutInflater.from(MarketPlaceActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MarketPlaceActivity.this);
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
            public void onFailure(Call<List<MarketPlaceResponse>> call, Throwable t) {
            }
        });
    }
}
