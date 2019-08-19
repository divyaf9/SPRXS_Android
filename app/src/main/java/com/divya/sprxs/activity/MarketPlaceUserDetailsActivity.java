package com.divya.sprxs.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.divya.sprxs.R;
import com.divya.sprxs.adapter.ExpandableListViewAdapter;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.model.InviteToCollaborateRequest;
import com.divya.sprxs.model.InviteToCollaborateResponse;
import com.divya.sprxs.model.MyIdeasRequest;
import com.divya.sprxs.model.MyIdeasResponse;
import com.divya.sprxs.model.RefreshTokenResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.RegisterActivity.MY_PREFS_NAME;

public class MarketPlaceUserDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameUserDetailsTextView, roleUserDetailsTextView, descUserDetailsTextView;
    private Button inviteToCollaborateButton;
    private String Name, FirstName, Role, Desc;
    private Long ProfileId;
    private List<MyIdeasResponse> myIdeasResponsedata = null;
    private ArrayList<String> Skills = new ArrayList<>();
    private ArrayList<String> Educations = new ArrayList<>();
    private ArrayList<String> Weblinks = new ArrayList<>();
    private ArrayList<String> IdeaName = new ArrayList<>();
    private ExpandableListView expandableListView;
    private int lastPosition = -1;
    private String sub;
    private InviteToCollaborateResponse inviteToCollaborateResponse;

    private ArrayList<String> arrayList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> hashMap = new HashMap<>();
    private ExpandableListViewAdapter expandableListViewAdapter;

    public static final Integer[] images = {
            R.drawable.ic_icons8_wrench,
            R.drawable.ic_icons8_graduation_cap,
            R.drawable.ic_public_black_24dp
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_user_details);
        myIdeas();
        this.setTitle("User Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        nameUserDetailsTextView = findViewById(R.id.nameUserDetailsTextView);
        roleUserDetailsTextView = findViewById(R.id.roleUserDetailsTextView);
        descUserDetailsTextView = findViewById(R.id.descUserDetailsTextView);
        descUserDetailsTextView.setMovementMethod(new ScrollingMovementMethod());
        inviteToCollaborateButton = findViewById(R.id.inviteToCollaborateButton);
        inviteToCollaborateButton.setOnClickListener(this);
        expandableListView = findViewById(R.id.expandableListView);

        ProfileId = getIntent().getLongExtra("ProfileId", 0L);
        Name = getIntent().getStringExtra("UserDetails");
        FirstName = getIntent().getStringExtra("firstName");
        Role = getIntent().getStringExtra("role");
        Desc = getIntent().getStringExtra("desc");
        Skills = getIntent().getStringArrayListExtra("skill");
        Educations = getIntent().getStringArrayListExtra("education");
        Weblinks = getIntent().getStringArrayListExtra("weblink");

        expandableListViewAdapter = new ExpandableListViewAdapter(this, arrayList, hashMap);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastPosition != -1
                        && groupPosition != lastPosition) {
                    expandableListView.collapseGroup(lastPosition);
                }
                lastPosition = groupPosition;
            }
        });

//        Display newDisplay = getWindowManager().getDefaultDisplay();
//        int width = newDisplay.getWidth();
        int width = getApplicationContext().getResources().getDisplayMetrics().widthPixels;
        expandableListView.setIndicatorBounds(width - 180, width);

        nameUserDetailsTextView.setText(Name);
        roleUserDetailsTextView.setText(Role);
        descUserDetailsTextView.setText(Desc);

        initListData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initListData() {
        arrayList.add("SKILLS");
        arrayList.add("WEBLINKS");
        arrayList.add("EDUCATIONS");

        hashMap.put(arrayList.get(0), Skills);
        hashMap.put(arrayList.get(1), Weblinks);
        hashMap.put(arrayList.get(2), Educations);

        expandableListViewAdapter.notifyDataSetChanged();
        expandableListView.expandGroup(0);
    }

    private void inviteToCollaborate() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        final String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        Call<InviteToCollaborateResponse> call;
        call = RetrofitClient.getInstance().getApi().inviteToCollaborate(
                "Bearer " + token, new InviteToCollaborateRequest(ProfileId, sub));
        call.enqueue(new Callback<InviteToCollaborateResponse>() {
            @Override
            public void onResponse(Call<InviteToCollaborateResponse> call, Response<InviteToCollaborateResponse> response) {
                if (response.code() == 200) {
                    inviteToCollaborateResponse = response.body();
                    final View successDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.success_dialog, null);
                    final Dialog dialog = new Dialog(getApplicationContext());
                    dialog.setContentView(R.layout.success_dialog);
                    TextView textView;
                    textView = successDialogView.findViewById(R.id.dialogTextView);
                    textView.setText("Invite sent");
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
                                inviteToCollaborate();
                            } else {
//                                try {
//                                    JSONObject jObjError = new JSONObject(response.errorBody().string());
                                final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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
//                                    final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
//                                    final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
                        dialog.setContentView(R.layout.error_dialog);
                        TextView textView;
                        textView = errorDialogView.findViewById(R.id.dialogTextView);
//                        textView.setText("You have already requested "+Name+" to collaborate on your Idea "+sub);
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
                        final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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
            public void onFailure(Call<InviteToCollaborateResponse> call, Throwable t) {
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inviteToCollaborateButton:

                final View inviteToCollaborateView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.invite_to_collaborate, null);
                final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
                dialog.setContentView(R.layout.invite_to_collaborate);
                TextView inviteTitleTextView, nameInviteTextView;
                Button inviteButton, cancelInviteButton;
                final Spinner ideaInviteSpinner;
                inviteTitleTextView = inviteToCollaborateView.findViewById(R.id.inviteTitleTextView);
                nameInviteTextView = inviteToCollaborateView.findViewById(R.id.nameInviteTextView);
                inviteButton = inviteToCollaborateView.findViewById(R.id.inviteButton);
                cancelInviteButton = inviteToCollaborateView.findViewById(R.id.cancelInviteButton);
                ideaInviteSpinner = inviteToCollaborateView.findViewById(R.id.ideaInviteSpinner);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, IdeaName);
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                ideaInviteSpinner.setAdapter(adapter);
                ideaInviteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String IdeaId = parent.getSelectedItem().toString();
                        sub = IdeaId.substring(IdeaId.indexOf("#") + 1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });
                inviteTitleTextView.setText("Invite " + Name);
                nameInviteTextView.setText("Select an Idea that you would like " + FirstName + " to collaborate on");
                inviteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inviteToCollaborate();
                        dialog.dismiss();
                    }
                });
                cancelInviteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

                dialog.setContentView(inviteToCollaborateView);
                dialog.show();

                break;
        }
    }

    public void myIdeas() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", null);
        final String refresh_token = prefs.getString("refresh_token", null);
        final SharedPreferences.Editor editor = getSharedPreferences(LoginActivity.MY_PREFS_NAME, MODE_PRIVATE).edit();
        Call<List<MyIdeasResponse>> call;
        call = RetrofitClient.getInstance().getApi().myIdeas(
                "Bearer " + token,
                new MyIdeasRequest("", "", ""));

        call.enqueue(new Callback<List<MyIdeasResponse>>() {
            @Override
            public void onResponse(Call<List<MyIdeasResponse>> call, Response<List<MyIdeasResponse>> response) {

                if (response.code() == 200) {

                    myIdeasResponsedata = response.body();
//                    IdeaName.add(" ");
                    for (int i = 0; i < myIdeasResponsedata.size(); i++) {
                        final String Id = myIdeasResponsedata.get(i).getIdeaUniqueID();
                        final String Name = myIdeasResponsedata.get(i).getIdeaName();
                        IdeaName.add(Name + " - #" + Id);
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
                                final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
                                final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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
//                                    final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
//                                    final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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
                        final View errorDialogView = LayoutInflater.from(MarketPlaceUserDetailsActivity.this).inflate(R.layout.error_dialog, null);
                        final Dialog dialog = new Dialog(MarketPlaceUserDetailsActivity.this);
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

}

