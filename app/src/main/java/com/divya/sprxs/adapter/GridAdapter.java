package com.divya.sprxs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.HomeScreenActivity;

public class GridAdapter extends BaseAdapter {


    Context context;
    private final String[] values;
    private final int[] images;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Context context, String[] values, int[] images) {
        this.context = context;
        this.values = values;
        this.images = images;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            view = new View(context);
            view = layoutInflater.inflate(R.layout.card_view_item_grid, null);
            view.setMinimumHeight(HomeScreenActivity.maxHeight/4-66);

            ImageView gridImageView = view.findViewById(R.id.gridImageView);
            TextView gridTextView = view.findViewById(R.id.gridTextView);

            gridImageView.setImageResource(images[position]);
            gridTextView.setText(values[position]);

        }

        return view;


    }
}
