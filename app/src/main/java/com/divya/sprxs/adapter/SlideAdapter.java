package com.divya.sprxs.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.divya.sprxs.R;

public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.landing_page,
            R.drawable.protect,
            R.drawable.resources,
            R.drawable.xenial,
            R.drawable.split
    };


    public String[] slide_headings = {
            "Start your Innovation Journey",
            "Protect your Ideas",
            "Resources that can Sell",
            "Xenial Platform to Collaborate",
            "Split your Equity"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ConstraintLayout landingPageImageView = (ConstraintLayout) view.findViewById(R.id.landingPageImageView);
        TextView landingPageTextView = (TextView) view.findViewById(R.id.landingPageTextView);

        landingPageImageView.setBackgroundResource(slide_images[position]);
        landingPageTextView.setText(slide_headings[position]);

        String text = landingPageTextView.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.rgb(239, 132, 6)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        landingPageTextView.setText(spannableString);


        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);

    }
}
