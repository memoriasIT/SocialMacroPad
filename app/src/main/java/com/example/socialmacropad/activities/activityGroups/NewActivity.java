package com.example.socialmacropad.activities.activityGroups;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmacropad.activities.communication.CommunicateActivity;
import com.example.socialmacropad.models.Action;

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
import com.example.socialmacropad.models.MacroPad;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.example.socialmacropad.util.Constants.BLUE;
import static com.example.socialmacropad.util.Constants.GREEN;
import static com.example.socialmacropad.util.Constants.GREY;
import static com.example.socialmacropad.util.Constants.ORANGE;

public class NewActivity extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout  input;
    RadioGroup colour;
    TextView top;
    TextView errorColour;
    private String TAG = NewActivity.class.getSimpleName();
    private int actionID;//id dentro del macropad (1,2,3,4,5,6)
    private MacroPad currentGroup;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        this.getSupportActionBar().hide();

        // Get UI elements
        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        input = (TextInputLayout) findViewById(R.id.outlinedTextFieldInput);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        top = (TextView)findViewById(R.id.textViewTop);
        errorColour = (TextView)findViewById(R.id.textViewErrorColour);
        errorColour.setVisibility(View.GONE);


        //CARGAR VALORES DEL GRUPO SELECCIONADO
        String jsonCurrentGroup = null;
        actionID = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonCurrentGroup = extras.getString("currentGroup");
            actionID = extras.getInt("actID");
        }
        currentGroup = new Gson().fromJson(jsonCurrentGroup, MacroPad.class);
        top.setText(currentGroup.getName() + " > " + getString(R.string.add_new_activity)); //nombre_del_grupo > Add new activity

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog();
            }
        });

        Button save = (Button)findViewById(R.id.saveActivity);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorColour.setVisibility(View.GONE);//elimina el posible mensaje de error anterior
                validarDatos();
            }
        });

    }

    // Validate inputs
    private void validarDatos() { //Nombre obligatorio, input obligatorio, color obligatorio
        String nombre = name.getEditText().getText().toString();
        String entrada = input.getEditText().getText().toString();

        RadioButton green = (RadioButton)findViewById(R.id.option_green);
        RadioButton blue = (RadioButton)findViewById(R.id.option_blue);
        RadioButton orange = (RadioButton)findViewById(R.id.option_orange);
        RadioButton grey = (RadioButton)findViewById(R.id.option_grey);

        boolean a = nombre.length() > 0;
        boolean b = entrada.length() > 0;
        boolean c = green.isChecked() || blue.isChecked() || orange.isChecked() || grey.isChecked();
        if(a && b && c){//Añadir la nueva actividad a la BD
            String color = null;
            if(green.isChecked()){
                color = GREEN;
            }else if(blue.isChecked()){
                color = BLUE;
            }else if(orange.isChecked()){
                color = ORANGE;
            }else if(grey.isChecked()){
                color = GREY;
            }
            Action newAction= new Action(entrada, nombre, color);

            saveActionToFirestore(newAction, actionID, currentGroup, entrada, nombre, color);

            Toast.makeText(this, getString((R.string.new_activity_created)), Toast.LENGTH_LONG).show();
            onBackPressed();
        }else{
            if(!a){
                name.setError(getString(R.string.error_name));
            }
            if(!b){
                input.setError(getString(R.string.error_input));
            }
            if(!c){
                errorColour.setVisibility(View.VISIBLE);
            }
        }
    }

    // Save to DB
    private void saveActionToFirestore(Action newAction, int actionID, MacroPad currentGroup, String input, String nombre, String color) {
        db = FirebaseFirestore.getInstance();
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> data = new HashMap<>();
        //put newAction o DocumentReference?
        switch (actionID){
            case 1:
                data.put("action1", newAction);
                currentGroup.setAction1(new Action(input, nombre, color));
                break;
            case 2:
                data.put("action2", newAction);
                currentGroup.setAction2(new Action(input, nombre, color));
                break;
            case 3:
                data.put("action3", newAction);
                currentGroup.setAction3(new Action(input, nombre, color));
                break;
            case 4:
                data.put("action4", newAction);
                currentGroup.setAction4(new Action(input, nombre, color));
                break;
            case 5:
                data.put("action5", newAction);
                currentGroup.setAction5(new Action(input, nombre, color));
                break;
            case 6:
                data.put("action6", newAction);
                currentGroup.setAction6(new Action(input, nombre, color));
                break;
        }

        db.collection("macropad").document(currentGroup.getPadId()).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Success");
            }
        });

        Intent intent = new Intent(NewActivity.this, CommunicateActivity.class);
        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
        startActivity(intent);


    }

    // Dialog called when pressing back
    private void warningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.are_you_sure));
        builder.setMessage(getString(R.string.warning_back));
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onBackPressed();
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