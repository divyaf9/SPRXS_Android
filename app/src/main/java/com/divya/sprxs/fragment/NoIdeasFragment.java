package com.divya.sprxs.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.divya.sprxs.R;


public class NoIdeasFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("My Ideas");
        getActivity().findViewById(R.id.helpImageView).setVisibility(View.INVISIBLE);
        return inflater.inflate(R.layout.fragment_no_ideas, container, false);
    }

}
