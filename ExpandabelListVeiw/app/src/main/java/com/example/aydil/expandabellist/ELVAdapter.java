package com.example.aydil.expandabellist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by Adeel Iftikhar on 3/2/2018.
 */

public class ELVAdapter extends BaseExpandableListAdapter {
    String [] groupNames = {"Sport","Computer","Food","Car","TV"};
    String [][] childNames = {
            {"Cricket","Hockey","Judo","Football","Boxing"},
            {"Laptop","Desktop","Note Book"},
            {"Ice Cream","Chicken"},
            {"BMW"},
            {"Samsung","GL"}
    };
    Context context;
    public ELVAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childNames.length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childNames[groupPosition][childPosition];
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
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
