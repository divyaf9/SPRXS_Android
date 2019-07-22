package com.divya.sprxs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.MarketPlaceUserDetailsActivity;
import com.divya.sprxs.model.MarketPlaceResponse;
import com.divya.sprxs.model.Skills;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataAdapterMarketPlace extends RecyclerView.Adapter<DataAdapterMarketPlace.ViewHolder> {

    private List<MarketPlaceResponse> marketPlaceResponseList;
    private List<Skills> skillsList;
    private CardView card_view_market_place;
    private Context context;
    private String Name, role;


    public DataAdapterMarketPlace(FragmentActivity fragmentActivity, List<MarketPlaceResponse> marketPlaceResponseList, Context context) {
        this.marketPlaceResponseList = marketPlaceResponseList;
        this.context = context;
    }


    @NonNull
    @Override
    public DataAdapterMarketPlace.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_market_place, parent, false);
        card_view_market_place = v.findViewById(R.id.card_view_market_place);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapterMarketPlace.ViewHolder holder, final int position) {
        if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 1) {
            holder.roleTextView.setText("Graphic and Design");
            role = "Graphic and Design";
        } else if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 2) {
            holder.roleTextView.setText("Music and Voice Over");
            role = "Music and Voice Over";
        } else if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 3) {
            holder.roleTextView.setText("Video and Animation");
            role = "Video and Animation";
        } else if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 4) {
            holder.roleTextView.setText("Digital Marketing");
            role = "Digital Marketing";
        } else if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 5) {
            holder.roleTextView.setText("Writing and Translation");
            role = "Writing and Translation";
        } else if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 6) {
            holder.roleTextView.setText("Programming and Tech");
            role = "Programming and Tech";
        } else if (marketPlaceResponseList.get(position).getPrimaryExpertise() == 7) {
            holder.roleTextView.setText("Other");
            role = "Other";
        }
        holder.marketPlaceNameTextView.setText(marketPlaceResponseList.get(position).getFirstname() + " " + marketPlaceResponseList.get(position).getSurname());
        final Long ProfileId = marketPlaceResponseList.get(position).getId();
        final String Name = marketPlaceResponseList.get(position).getFirstname() + " " + marketPlaceResponseList.get(position).getSurname();
        final String FirstName = marketPlaceResponseList.get(position).getFirstname();
        final String finalRole = role;
        final String Desc = marketPlaceResponseList.get(position).getProfileDesc();
        final Long UserId = marketPlaceResponseList.get(position).getId();
        String jsonFull = new Gson().toJson(marketPlaceResponseList);
        final ArrayList<String> skills = new ArrayList<String>();
        final ArrayList<String> educations = new ArrayList<String>();
        final ArrayList<String> weblinks = new ArrayList<String>();

        try {
            JSONArray json = new JSONArray(jsonFull);
            if (json.length() > 0) {
                for (int i = 0; i < json.length(); i++) {
                    if (json.getJSONObject(i).getLong("id") == UserId) {
                        JSONObject object = json.getJSONObject(i);
                        JSONArray skillsArray = object.getJSONArray("skills");
                        JSONArray educationsArray = object.getJSONArray("educations");
                        JSONArray weblinksArray = object.getJSONArray("weblinks");

                        if (skillsArray.length() > 0) {
                            for (int j = 0; j < skillsArray.length(); j++) {
                                JSONObject skillsObject = skillsArray.getJSONObject(j);
                                String skillName = skillsObject.getString("skillName");
                                skills.add(skillName);
                            }
                        }else{
                            skills.add("Please add skills to display");
                        }

                        if(educationsArray.length() > 0){
                            for(int k =0; k< educationsArray.length();k++){
                                JSONObject educationsObject = educationsArray.getJSONObject(k);
                                String major = educationsObject.getString("major");
                                educations.add(major);
                            }
                        }else{
                            educations.add("Please add educations to display");
                        }

                        if (weblinksArray.length() > 0) {
                            for (int l = 0; l < weblinksArray.length(); l++) {
                                JSONObject weblinksObject = weblinksArray.getJSONObject(l);
                                String url = weblinksObject.getString("url");
                                weblinks.add(url);
                            }
                        }
                        else {
                            weblinks.add("Please add weblinks to display");
                        }


                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MarketPlaceUserDetailsActivity.class);
                intent.putExtra("ProfileId",ProfileId);
                intent.putExtra("UserDetails", Name);
                intent.putExtra("firstName",FirstName);
                intent.putExtra("role", finalRole);
                intent.putExtra("desc", Desc);
                intent.putExtra("skill", skills);
                intent.putExtra("education", educations);
                intent.putExtra("weblink", weblinks);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (marketPlaceResponseList == null) ? 0 : marketPlaceResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView roleTextView, marketPlaceNameTextView;
        private ImageView inboxImageView;


        public ViewHolder(View view) {
            super(view);

            roleTextView = view.findViewById(R.id.roleTextView);
            marketPlaceNameTextView = view.findViewById(R.id.marketPlaceNameTextView);

        }
    }

    public void setfilter(List<MarketPlaceResponse> listitem)
    {
        marketPlaceResponseList = new ArrayList<>();
        marketPlaceResponseList.addAll(listitem);
        notifyDataSetChanged();
    }
}
