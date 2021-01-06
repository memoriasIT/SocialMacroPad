package com.example.socialmacropad.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialmacropad.R;
import com.example.socialmacropad.models.MacroPad;

import java.util.ArrayList;
import java.util.List;

public class MacroPadAdapter extends ArrayAdapter<MacroPad> {

    private Context mContext;
    private List<MacroPad> macroPadList = new ArrayList<>();


    public MacroPadAdapter(@NonNull Context context, ArrayList<MacroPad> list) {
        super(context, 0 , list);
        mContext = context;
        macroPadList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_discover,parent,false);

        MacroPad currentPad = macroPadList.get(position);


        TextView name = (TextView) listItem.findViewById(R.id.txtPadName);
        name.setText(currentPad.getName());

        TextView release = (TextView) listItem.findViewById(R.id.txtUser);
        release.setText(currentPad.getCreatorUser());

        Button btn1_1 = (Button) listItem.findViewById(R.id.btn1_1);
        btn1_1.setText(currentPad.getAction1().getActionName());

        Button btn1_2 = (Button) listItem.findViewById(R.id.btn1_2);
        btn1_2.setText(currentPad.getAction2().getActionName());

        Button btn2_1 = (Button) listItem.findViewById(R.id.btn2_1);
        btn2_1.setText(currentPad.getAction3().getActionName());

        Button btn2_2 = (Button) listItem.findViewById(R.id.btn2_2);
        btn2_2.setText(currentPad.getAction4().getActionName());

        Button btn3_1 = (Button) listItem.findViewById(R.id.btn3_1);
        btn3_1.setText(currentPad.getAction5().getActionName());

        Button btn3_2 = (Button) listItem.findViewById(R.id.btn3_2);
        btn3_2.setText(currentPad.getAction6().getActionName());

        return listItem;
    }
}

