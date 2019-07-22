package com.divya.sprxs.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.divya.sprxs.R;
import com.divya.sprxs.activity.MarketPlaceUserDetailsActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> arrayList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> hashMap = new HashMap<>();


    public ExpandableListViewAdapter(Context context, ArrayList<String> arrayList, HashMap<String, ArrayList<String>> hashMap) {
        this.context = context;
        this.arrayList = arrayList;
        this.hashMap = hashMap;

    }

    @Override
    public int getGroupCount() {
        return arrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.hashMap.get(this.arrayList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.arrayList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.hashMap.get(this.arrayList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_parent, null);
        }

        TextView textView = convertView.findViewById(R.id.list_parent);
        textView.setText(group);
        ImageView imageView = convertView.findViewById(R.id.list_parent_imageView);
        imageView.setImageResource(MarketPlaceUserDetailsActivity.images[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_child, null);
        }
        TextView textView = convertView.findViewById(R.id.list_child);
        textView.setText(child);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
