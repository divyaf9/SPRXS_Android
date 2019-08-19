package com.divya.sprxs.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.divya.sprxs.R;

public class NoMilestonesFragment extends Fragment {

    private LottieAnimationView lottieAnimationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_no_milestones, container, false);

        getActivity().setTitle("Milestones");

        lottieAnimationView = v.findViewById(R.id.noMilestoneLottieFiles);

        return v;
    }

}
