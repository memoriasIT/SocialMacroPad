package com.example.socialmacropad.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.CommunicateActivity;
import com.example.socialmacropad.activities.EditGroupActivity;
import com.example.socialmacropad.models.GroupOfActivities;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapterHome extends ArrayAdapter<GroupOfActivities> {

    private Context mContext;
    private List<GroupOfActivities> groupsList = new ArrayList<>();
    private String TAG = GroupAdapterHome.class.getSimpleName();


    public GroupAdapterHome(@NonNull Context context, ArrayList<GroupOfActivities> list) {
        super(context, 0 , list);
        mContext = context;
        groupsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_groups_home,parent,false);

        GroupOfActivities currentGroup = groupsList.get(position);

        Button btnGroup = (Button) listItem.findViewById(R.id.btnNameGroup);
        btnGroup.setText(currentGroup.getName());
        btnGroup.setBackgroundColor(Color.parseColor(currentGroup.getColour()));
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommunicateActivity.class);
                mContext.startActivity(intent);
            }
        });

        ImageButton btnEditGroup = (ImageButton) listItem.findViewById(R.id.btnEditCurrentGroup);
        btnEditGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditGroupActivity.class);
                mContext.startActivity(intent);
            }
        });

        return listItem;
    }

}
