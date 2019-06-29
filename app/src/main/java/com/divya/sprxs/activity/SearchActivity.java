package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.DataAdapterSearchIdea;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.RefreshTokenResponse;
import com.divya.sprxs.model.SearchIdeaRequest;
import com.divya.sprxs.model.SearchIdeaResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class SearchActivity extends AppCompatActivity {


    private SearchView searchView;
    private RecyclerView recyclerViewSearchIdea;
    private DataAdapterSearchIdea dataAdapterSearchIdea;
    private List<SearchIdeaResponse> searchIdeasResponsesData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        this.setTitle("Search Ideas");
//        searchView = findViewById(R.id.searchIdea);
        recyclerViewSearchIdea = findViewById(R.id.recycler_view_search_idea);
        searchIdea();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search_idea_menu,menu);
//        MenuItem menuItem = menu.findItem(R.id.searchIdeamenu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menuItem.getActionView();
////        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                dataAdapterSearchIdea.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_idea_menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.searchIdeamenu);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.colorWhite));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override            public boolean onQueryTextChange(String newText) {
                final  List<SearchIdeaResponse> filtermodelist=filter(searchIdeasResponsesData,newText);
                dataAdapterSearchIdea.setfilter(filtermodelist);
                return true;
            }
        });
        return true;
    }
    private List<SearchIdeaResponse> filter(List<SearchIdeaResponse> pl,String query)
    {
        query=query.toLowerCase();
        final List<SearchIdeaResponse> filteredModeList=new ArrayList<>();
        for (SearchIdeaResponse model:pl)
        {
            final String text=model.getIdeaName().toLowerCase();
            if (text.startsWith(query))
            {
                filteredModeList.add(model);
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


    private void searchIdea() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<List<SearchIdeaResponse>> call;
        call = RetrofitClient.getInstance().getApi().searchIdea(
                "Bearer " + token,
                new SearchIdeaRequest( 0L, "", "", ""));
        call.enqueue(new Callback<List<SearchIdeaResponse>>() {
            @Override
            public void onResponse(Call<List<SearchIdeaResponse>> call, Response<List<SearchIdeaResponse>> response) {
                if (response.code() == 200) {
                    searchIdeasResponsesData = response.body();
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerViewSearchIdea.setLayoutManager(layoutManager);
                    dataAdapterSearchIdea = new DataAdapterSearchIdea(SearchActivity.this, searchIdeasResponsesData, getApplicationContext());
                    recyclerViewSearchIdea.setAdapter(dataAdapterSearchIdea);
                    dataAdapterSearchIdea.notifyDataSetChanged();


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
                                searchIdea();
                            } else {
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                    final View successDialogView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(getApplicationContext());
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
                                    final View successDialogView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.error_dialog, null);
                                    final Dialog dialog = new Dialog(getApplicationContext());
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
                        final View successDialogView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getApplicationContext());
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
                        final View successDialogView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(getApplicationContext());
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
            public void onFailure(Call<List<SearchIdeaResponse>> call, Throwable t) {
            }
        });
    }
    }

