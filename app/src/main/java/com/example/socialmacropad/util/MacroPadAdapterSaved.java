package com.example.socialmacropad.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.socialmacropad.R;
import com.example.socialmacropad.models.MacroPad;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MacroPadAdapterSaved extends ArrayAdapter<MacroPad> {

    private Context mContext;
    private List<MacroPad> macroPadList;
    private MacroPad currentPad;

    public MacroPadAdapterSaved(@NonNull Context context, ArrayList<MacroPad> list) {
        super(context, 0 , list);
        mContext = context;
        macroPadList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_saved,parent,false);

        currentPad = macroPadList.get(position);

        ImageButton back = (ImageButton) listItem.findViewById(R.id.delete);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog(v);
            }
        });

        TextView name = (TextView) listItem.findViewById(R.id.txtPadName);
        name.setText(currentPad.getName());

        TextView user = (TextView) listItem.findViewById(R.id.txtUser);
        user.setText("@"+currentPad.getCreatorUser());

        TextView description = (TextView) listItem.findViewById(R.id.txtDescription);
        description.setText(currentPad.getDescription());

        Button btn1_1 = (Button) listItem.findViewById(R.id.btn1_1);
        btn1_1.setText(insertPeriodically(currentPad.getAction1().getActionname(), "\n", 11));

        Button btn1_2 = (Button) listItem.findViewById(R.id.btn1_2);
        btn1_2.setText(insertPeriodically(currentPad.getAction2().getActionname(), "\n", 11));

        Button btn2_1 = (Button) listItem.findViewById(R.id.btn2_1);
        btn2_1.setText(insertPeriodically(currentPad.getAction3().getActionname(), "\n", 11));

        Button btn2_2 = (Button) listItem.findViewById(R.id.btn2_2);
        btn2_2.setText(insertPeriodically(currentPad.getAction4().getActionname(), "\n", 11));

        Button btn3_1 = (Button) listItem.findViewById(R.id.btn3_1);
        btn3_1.setText(insertPeriodically(currentPad.getAction5().getActionname(), "\n", 11));

        Button btn3_2 = (Button) listItem.findViewById(R.id.btn3_2);
        btn3_2.setText(insertPeriodically(currentPad.getAction6().getActionname(), "\n", 11));

        return listItem;
    }



    private static String insertPeriodically(
            String text, String insert, int period)
    {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length()/period)+1);

        int index = 0;
        String prefix = "";
        while (index < text.length())
        {
            // Don't put the insert in the very first iteration.
            // This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }

        Log.d("Test", builder.toString());
        return builder.toString();
    }


    private void warningDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle(v.getContext().getString(R.string.are_you_sure));
        builder.setMessage(v.getContext().getString(R.string.warning_delete_group));
        builder.setPositiveButton(v.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Delete from firebase
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                db.collection("users").document(UserID).collection("savedPads").document(currentPad.getPadId()).delete();
                Log.d("MacroPadAdapterSaved", String.valueOf(getPosition(currentPad)));
                macroPadList.remove(getPosition(currentPad));
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.show();
    }
}

