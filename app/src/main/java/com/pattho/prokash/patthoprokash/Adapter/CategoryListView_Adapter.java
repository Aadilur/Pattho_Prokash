package com.pattho.prokash.patthoprokash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pattho.prokash.patthoprokash.Activity.BookStore.StoreViewAllBooks;
import com.pattho.prokash.patthoprokash.R;

import java.util.HashMap;
import java.util.List;

public class CategoryListView_Adapter extends BaseExpandableListAdapter {

    Context context;
    List<String> categoryGroupList;
    HashMap<String ,List<String >> categoryListItem;

    public CategoryListView_Adapter(Context context, List<String> categoryGroupList, HashMap<String, List<String>> categoryListItem) {
        this.context = context;
        this.categoryGroupList = categoryGroupList;
        this.categoryListItem = categoryListItem;
    }

    @Override
    public int getGroupCount() {
        return categoryGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categoryListItem.get(categoryGroupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categoryListItem.get(categoryGroupList.get(groupPosition))
                .get(childPosition);
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
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vh_category_listview_group,null);
        }
            TextView textView = convertView.findViewById(R.id.textView1);
            ImageView ivIcon = convertView.findViewById(R.id.icon);

            textView.setText(group);

        if (isExpanded){
            ivIcon.setImageResource(R.drawable.arrow_down);
        }else ivIcon.setImageResource(R.drawable.arrow_right);



            return convertView;



    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String list = (String) getChild(groupPosition,childPosition);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.vh_category_listview,null);
        }
        TextView textView = convertView.findViewById(R.id.textView1);
        textView.setText(list);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreViewAllBooks.class);
                intent.putExtra("name",list);
                Toast.makeText(context, ""+list, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
