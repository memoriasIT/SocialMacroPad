package com.example.socialmacropad.activities.activityGroups;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.communication.CommunicateActivity;
import com.example.socialmacropad.models.Action;
import com.example.socialmacropad.models.MacroPad;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.example.socialmacropad.util.Constants.BLUE;
import static com.example.socialmacropad.util.Constants.GREEN;
import static com.example.socialmacropad.util.Constants.GREY;
import static com.example.socialmacropad.util.Constants.ORANGE;

public class EditActivity extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout  input;
    RadioGroup colour;
    TextView top;
    TextView activityName;
    private Action currAction;
    private MacroPad macroPad;
    private int pos;
    private String strnombre;
    private String strinput;
    private String strcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        this.getSupportActionBar().hide();

        // Get UI elements
        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        input = (TextInputLayout) findViewById(R.id.outlinedTextFieldInput);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        top = (TextView)findViewById(R.id.textViewTop);
        activityName = (TextView)findViewById(R.id.textViewActivityName);

        //CARGAR VALORES DE LA ACTIVIDAD SELECCIONADA
        String jsonCurrentGroup = null;
        String macropad = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonCurrentGroup = extras.getString("action");
            macropad = extras.getString("macropad");
            pos = extras.getInt("pos");
        }
        currAction = new Gson().fromJson(jsonCurrentGroup, Action.class);
        macroPad = new Gson().fromJson(macropad, MacroPad.class);


        top.setText(macroPad.getName() + " > " + currAction.getActionname() + getString(R.string.top_edit));//nombre_grupo > nombew_act > Edit
        activityName.setText(currAction.getActionname());
        name.getEditText().setText( currAction.getActionname(), TextView.BufferType.EDITABLE);
        input.getEditText().setText(currAction.getAction(), TextView.BufferType.EDITABLE);
        //check al radio button del color correspondiente



        // Set onClick Listeners
        ImageButton delete = (ImageButton)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog(true);
            }
        });

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog(false);
            }
        });

        Button save = (Button)findViewById(R.id.saveActivity);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

    }

    // Validar los inputs
    private void validarDatos() { //Nombre obligatorio, input obligatorio, color obligatorio
        strnombre = name.getEditText().getText().toString();
        strinput = input.getEditText().getText().toString();
        strcolor = null;

        RadioButton green = (RadioButton)findViewById(R.id.option_green);
        RadioButton blue = (RadioButton)findViewById(R.id.option_blue);
        RadioButton orange = (RadioButton)findViewById(R.id.option_orange);
        RadioButton grey = (RadioButton)findViewById(R.id.option_grey);
        if(green.isChecked()){
            strcolor = GREEN;
        }else if(blue.isChecked()){
            strcolor = BLUE;
        }else if(orange.isChecked()){
            strcolor = ORANGE;
        }else if(grey.isChecked()){
            strcolor = GREY;
        }

        boolean a = strnombre.length() > 0;
        boolean b = strinput.length() > 0;
        if(a && b){//ACTUALIZAR la actividad en la BD
            Map<String, Object> data = new HashMap<>();
            data.put("action"+pos, new Action(strinput, strnombre, strcolor));

            switch (pos){
                case 1:
                    macroPad.setAction1(new Action(strinput, strnombre, strcolor));
                    break;
                case 2:
                    macroPad.setAction2(new Action(strinput, strnombre, strcolor));
                    break;
                case 3:
                    macroPad.setAction3(new Action(strinput, strnombre, strcolor));
                    break;
                case 4:
                    macroPad.setAction4(new Action(strinput, strnombre, strcolor));
                    break;
                case 5:
                    macroPad.setAction5(new Action(strinput, strnombre, strcolor));
                    break;
                case 6:
                    macroPad.setAction6(new Action(strinput, strnombre, strcolor));
                    break;
            }

            FirebaseFirestore.getInstance().collection("macropad").document(macroPad.getPadId()).update(data);
            Toast.makeText(this, getString((R.string.updated_activity)), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(EditActivity.this, CommunicateActivity.class);
            intent.putExtra("currentGroup", new Gson().toJson(macroPad));
            startActivity(intent);
        }else{
            if(!a){
                name.setError(getString(R.string.error_name));
            }
            if(!b){
                input.setError(getString(R.string.error_input));
            }
        }
    }

    // Dialog para borrar la actividad o volver
    private void warningDialog(Boolean delete) {//b:true-> delete   b:false->back
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_you_sure));
        if(delete){
            builder.setMessage(getString(R.string.warning_delete_group));
        }else{
            builder.setMessage(getString(R.string.warning_back));
        }
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(delete){
                    //SE ELIMINA LA ACTIVIDAD ACTUAL
                    Map<String, Object> data = new HashMap<>();
                    data.put("action"+pos, new Action("null", "null", GREY));

                    switch (pos){
                        case 1:
                            macroPad.setAction1(new Action(strinput, strnombre, strcolor));
                            break;
                        case 2:
                            macroPad.setAction2(new Action(strinput, strnombre, strcolor));
                            break;
                        case 3:
                            macroPad.setAction3(new Action(strinput, strnombre, strcolor));
                            break;
                        case 4:
                            macroPad.setAction4(new Action(strinput, strnombre, strcolor));
                            break;
                        case 5:
                            macroPad.setAction5(new Action(strinput, strnombre, strcolor));
                            break;
                        case 6:
                            macroPad.setAction6(new Action(strinput, strnombre, strcolor));
                            break;
                    }

                    FirebaseFirestore.getInstance().collection("macropad").document(macroPad.getPadId()).update(data);
                    Intent intent = new Intent(EditActivity.this, CommunicateActivity.class);
                    intent.putExtra("currentGroup", new Gson().toJson(macroPad));
                    startActivity(intent);
                }else {
                    onBackPressed();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.show();
    }

}