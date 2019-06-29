package com.divya.sprxs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.divya.sprxs.R;
import com.divya.sprxs.model.ListIdeaForCollaborationRequest;
import com.divya.sprxs.model.SearchIdeaResponse;

import java.io.LineNumberInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataAdapterSearchIdea extends RecyclerView.Adapter<DataAdapterSearchIdea.ViewHolder> {

    private List<SearchIdeaResponse> searchIdeaResponse;
    private List<SearchIdeaResponse> searchIdeaResponsesList;
    private CardView cardViewInbox;
    private Context context;


    public DataAdapterSearchIdea(FragmentActivity activity, List<SearchIdeaResponse> searchIdeaResponse, Context context) {
        this.searchIdeaResponse = searchIdeaResponse;
        searchIdeaResponsesList = new ArrayList<>(searchIdeaResponse);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_item_search_ideas, parent, false);
        cardViewInbox = v.findViewById(R.id.card_view_search_idea);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.ideaNameSearchText.setText(searchIdeaResponse.get(position).getIdeaName());
        holder.dateSearchText.setText(searchIdeaResponse.get(position).getIdeaDatCreated());

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
//    public Filter getFilter() {
//
//        return searchIdeaResponseFilter;
//    }

//    Filter searchIdeaResponseFilter = new Filter() {pp
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<SearchIdeaResponse> filteredSearchIdeaResponse = new ArrayList<>();
//            if(constraint == null || constraint.length()== 0){
//                filteredSearchIdeaResponse.addAll(searchIdeaResponsesList);
//            }else{
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for(SearchIdeaResponse item : searchIdeaResponsesList){
//                    if (item.getIdeaName().toLowerCase().contains(filterPattern)){
//                        filteredSearchIdeaResponse.add(item);
//                    }
//                }
//            }
//
//            FilterResults results = new FilterResults();
//            results.values = filteredSearchIdeaResponse;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//            searchIdeaResponse.clear();
//            searchIdeaResponsesList.addAll((List)results.values);
//            notifyDataSetChanged();
//
//        }
//    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ideaNameSearchText,dateSearchText;


        public ViewHolder(View view) {
            super(view);
            ideaNameSearchText = view.findViewById(R.id.ideaNameSearchText);
            dateSearchText = view.findViewById(R.id.dateSearchText);

        }
    }
}
