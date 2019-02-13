package com.example.aydil.expandablelistview.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aydil.expandablelistview.MainActivity;
import com.example.aydil.expandablelistview.R;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    String[] parentName;
    String[][] childName;
    LayoutInflater myLayoutInflater;
    public ExpandableListViewAdapter(Context context,String[] parentViewArray, String[][] childViewArray) {
        this.context = context;
        parentName = parentViewArray;
        childName = childViewArray;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return parentName.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childName[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentName[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childName[groupPosition][childPosition];
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
        convertView = myLayoutInflater.inflate(R.layout.layout_for_adapter, null);
        TextView textViewParent = convertView.findViewById(R.id.parent_group_name);
        textViewParent.setText(parentName[groupPosition]);
        return textViewParent;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = myLayoutInflater.inflate(R.layout.layout_for_adapter, null);
        final TextView textViewChild = convertView.findViewById(R.id.child_group_name);
        textViewChild.setText(childName[groupPosition][childPosition]);
        textViewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, textViewChild.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return textViewChild;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
