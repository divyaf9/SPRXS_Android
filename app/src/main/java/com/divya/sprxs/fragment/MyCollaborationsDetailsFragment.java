package com.divya.sprxs.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.HomeActivity;
import com.divya.sprxs.activity.MyCollaborationsActivity;
import com.divya.sprxs.activity.MyCollaborationsDetailsActivity;

public class MyCollaborationsDetailsFragment extends Fragment {

    private TextView ideaIdMyCollaborationsDetails,ideaNameMyCollaborationsDetails,ideaDescMyCollaborationsDetails,ownerNameMyCollaborationsDetails,equityMyCollaborationsDetails;
    private String ideaId,ideaName,ideaDesc,ownerName,equity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_my_collaborations_details, container, false);

        getActivity().setTitle("My Collaborations Details");

        ideaIdMyCollaborationsDetails = v.findViewById(R.id.ideaIdMyCollaborationsDetails);
        ideaNameMyCollaborationsDetails = v.findViewById(R.id.ideaNameMyCollaborationsDetails);
        ideaDescMyCollaborationsDetails = v.findViewById(R.id.ideaDescMyCollaborationsDetails);
        ownerNameMyCollaborationsDetails = v.findViewById(R.id.ownerNameMyCollaborationsDetails);
        equityMyCollaborationsDetails = v.findViewById(R.id.equityMyCollaborationsDetails);

        ideaId = getActivity().getIntent().getStringExtra("IdeaIdMyCollab");
        ideaName = getActivity().getIntent().getStringExtra("IdeaNameMyCollab");
        ideaDesc = getActivity().getIntent().getStringExtra("IdeaDescMyCollab");
        ownerName = getActivity().getIntent().getStringExtra("OwnerNameMyCollab");
        equity = String.valueOf(getActivity().getIntent().getIntExtra("EquityMyCollab",0));
        ideaIdMyCollaborationsDetails.setText("Idea ID #"+ideaId);

        ideaNameMyCollaborationsDetails.setText(ideaName);
        ideaDescMyCollaborationsDetails.setText(ideaDesc);
        ownerNameMyCollaborationsDetails.setText(ownerName);
        equityMyCollaborationsDetails.setText(equity+"/100");
        ideaDescMyCollaborationsDetails.setMovementMethod(new ScrollingMovementMethod());

        return v;
    }


}
