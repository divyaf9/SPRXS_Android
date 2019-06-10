package com.divya.sprxs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.divya.sprxs.R;
import com.divya.sprxs.api.RetrofitClient;
import com.divya.sprxs.fragment.MyIdeasFragment;
import com.divya.sprxs.model.MyIdeasRequest;
import com.divya.sprxs.model.MyIdeasResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.divya.sprxs.activity.LoginActivity.MY_PREFS_NAME;

public class IdeaDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    TextView blockchainStatus,attachmentStatus,ideaStatus;
    TextView ideaName_IdeaDetails,ideaId_IdeaDetails,dateText_IdeaDetails,ideaDescriptionText_IdeaDetails;
    ImageButton editIdeaButton;
    public static final String MY_IDEA_ID = "MyIdPrefsFile";
    private List<MyIdeasResponse> myIdeasResponsedata = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea_details);

        final String ideaId = getIntent().getStringExtra("myList");


        this.setTitle(ideaId);
        blockchainStatus = findViewById(R.id.blockchainStatus);
        attachmentStatus = findViewById(R.id.attachmentStatus);
        ideaStatus = findViewById(R.id.ideaStatus);
        ideaName_IdeaDetails = findViewById(R.id.ideaName_IdeaDetails);
        ideaId_IdeaDetails = findViewById(R.id.ideaId_IdeaDetails);
        dateText_IdeaDetails = findViewById(R.id.dateText_IdeaDetails);
        ideaDescriptionText_IdeaDetails = findViewById(R.id.ideaDescriptionText_IdeaDetails);
        ideaDescriptionText_IdeaDetails.setMovementMethod(new ScrollingMovementMethod());
        editIdeaButton = findViewById(R.id.editIdeaButton);
        editIdeaButton.setOnClickListener(this);
        myIdeas();
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.editIdeaButton:
               goToEditIdea();
                break;
        }
    }

    private void goToEditIdea() {
        Intent intent = new Intent(IdeaDetailsActivity.this,EditIdeaActivity.class);
        startActivity(intent);
    }

    public void myIdeas() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String token = prefs.getString("token", null);
        SharedPreferences idPrefs = getApplicationContext().getSharedPreferences(MY_IDEA_ID,MODE_PRIVATE);
        final SharedPreferences.Editor editor = getSharedPreferences(MY_IDEA_ID, MODE_PRIVATE).edit();
        Call<List<MyIdeasResponse>> call;
        call = RetrofitClient.getInstance().getApi().myIdeas(
                "Bearer " + token,
                new MyIdeasRequest("","",""));

        call.enqueue(new Callback<List<MyIdeasResponse>>() {
            @Override
            public void onResponse(Call<List<MyIdeasResponse>> call, Response<List<MyIdeasResponse>> response) {

                if (response.code() == 200) {
                    myIdeasResponsedata = response.body();

                    final String ideaId = getIntent().getStringExtra("myList");

                    for (int i = 0; i < myIdeasResponsedata.size(); i++) {

                        if (ideaId.contentEquals(myIdeasResponsedata.get(i).getIdeaUniqueID())) {
                            blockchainStatus.setText(myIdeasResponsedata.get(i).getTokenId());
                            ideaName_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaName());
                            ideaId_IdeaDetails.setText("#"+myIdeasResponsedata.get(i).getIdeaUniqueID());
                            editor.putString("ideaId","#"+myIdeasResponsedata.get(i).getIdeaUniqueID());
                            editor.apply();
                            ideaDescriptionText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDescription());
                            dateText_IdeaDetails.setText(myIdeasResponsedata.get(i).getIdeaDateCreated());
                            attachmentStatus.setText(myIdeasResponsedata.get(i).getIdeaFilepath());
                            if(myIdeasResponsedata.get(i).isAllowSearch()==true) {
                                ideaStatus.setText("PUBLIC");
                            }else{
                                ideaStatus.setText("PRIVATE");
                            }
                        }
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                    } catch (Exception e) {
                        Toast.makeText(IdeaDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<List<MyIdeasResponse>> call, Throwable t) {
            }
        });

    }

}
