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
import com.divya.sprxs.activity.SearchIdeaDetailsActivity;
import com.divya.sprxs.model.SearchIdeaResponse;

import java.util.ArrayList;
import java.util.List;

public class DataAdapterSearchIdea extends RecyclerView.Adapter<DataAdapterSearchIdea.ViewHolder> {

    private List<SearchIdeaResponse> searchIdeaResponse;
    private CardView cardViewSearchIdea;
    private Context context;


    public DataAdapterSearchIdea(FragmentActivity activity, List<SearchIdeaResponse> searchIdeaResponse, Context context) {
        this.searchIdeaResponse = searchIdeaResponse;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_search_ideas, parent, false);
        cardViewSearchIdea = v.findViewById(R.id.card_view_search_idea);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String IdeaId = searchIdeaResponse.get(position).getIdeaUniqueID();
        final String IdeaName = searchIdeaResponse.get(position).getIdeaName();
        final String IdeaDesc = searchIdeaResponse.get(position).getIdeaDescription();
//        final String Category = searchIdeaResponse.get(position).getLkpIdeaCat1();
        String Category= null;
        holder.ideaNameSearchText.setText(searchIdeaResponse.get(position).getIdeaName());
        holder.ideaIdSearchText.setText(searchIdeaResponse.get(position).getIdeaUniqueID());
        holder.dateSearchText.setText(searchIdeaResponse.get(position).getAndroidDate());
        if(searchIdeaResponse.get(position).getIdeaDescription().length()>=80) {
            holder.descSearchIdeaTextView.setText((searchIdeaResponse.get(position).getIdeaDescription()).substring(0, Math.min(searchIdeaResponse.get(position).getIdeaDescription().length(), 80))+"...");
        }else{
            holder.descSearchIdeaTextView.setText((searchIdeaResponse.get(position).getIdeaDescription()));
        }

        if (searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("1")) {
            Category = "Technology idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.technology);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if(searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("2")){
            Category="Lifestyle & Wellbeing idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.lifestyle);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if (searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("3")) {
            Category = "Food & Drink idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.collaborate);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if(searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("4")){
            Category="Gaming idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.gaming);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if (searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("5")) {
            Category = "Business & Finance idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.business);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if(searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("6")){
            Category="Art and Fashion idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.fashionart);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if (searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("7")) {
            Category = "Film idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.film);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if(searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("8")){
            Category="Media & Journalism idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.media);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if (searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("9")) {
            Category = "Theatre Idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.media);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if(searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("10")){
            Category="Music Idea";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.media);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else if(searchIdeaResponse.get(position).getLkpIdeaCat1().contentEquals("11")){
            Category="Other";
            holder.categorySearchTextView.setText(Category);
            holder.searchIdeaImageView.setImageResource(R.drawable.other);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            holder.searchIdeaImageView.setImageResource(R.drawable.other);
            holder.searchIdeaImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        final String finalCategory = Category;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), SearchIdeaDetailsActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             intent.putExtra("mySearchList",IdeaId);
             intent.putExtra("mySearchListIdea",IdeaName);
             intent.putExtra("mySearchListIdeaDesc",IdeaDesc);
             intent.putExtra("mySearchListCategory", finalCategory);
             context.startActivity(intent);
        }
        });
    }

    @Override
    public int getItemCount() {
        return (searchIdeaResponse == null) ? 0 : searchIdeaResponse.size();
    }

    public void setfilter(List<SearchIdeaResponse> listitem)
    {
        searchIdeaResponse=new ArrayList<>();
        searchIdeaResponse.addAll(listitem);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ideaNameSearchText,ideaIdSearchText,dateSearchText,categorySearchTextView,descSearchIdeaTextView;
        private ImageView searchIdeaImageView;


        public ViewHolder(View view) {
            super(view);
            ideaNameSearchText = view.findViewById(R.id.ideaNameSearchText);
            ideaIdSearchText = view.findViewById(R.id.ideaIdSearchText);
            dateSearchText = view.findViewById(R.id.dateSearchText);
            searchIdeaImageView = view.findViewById(R.id.searchIdeaImageView);
            categorySearchTextView = view.findViewById(R.id.categorySearchTextView);
            descSearchIdeaTextView = view.findViewById(R.id.descSearchIdeaTextView);

        }
    }
}
