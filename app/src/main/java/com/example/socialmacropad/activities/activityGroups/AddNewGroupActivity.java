package com.example.socialmacropad.activities.activityGroups;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;

import com.example.socialmacropad.models.GroupOfActivities;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.socialmacropad.util.Constants.BLUE;
import static com.example.socialmacropad.util.Constants.GREEN;
import static com.example.socialmacropad.util.Constants.GREY;
import static com.example.socialmacropad.util.Constants.ORANGE;

public class AddNewGroupActivity extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout  description;
    RadioGroup colour;
    TextView errorColour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);
        this.getSupportActionBar().hide();

        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        description = (TextInputLayout) findViewById(R.id.outlinedTextFieldDescription);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        errorColour = (TextView)findViewById(R.id.textViewErrorColour);
        errorColour.setVisibility(View.GONE);

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warningDialog();
            }
        });

        Button save = (Button)findViewById(R.id.saveGroup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorColour.setVisibility(View.GONE);//elimina el posible mensaje de error anterior 
                validarDatos();
            }
        });

    }

    private void validarDatos() { //Nombre obligatorio, descripcion opcional, color obligatorio
        String nombre = name.getEditText().getText().toString();
        String descripcion = description.getEditText().getText().toString();

        RadioButton green = (RadioButton)findViewById(R.id.option_green);
        RadioButton blue = (RadioButton)findViewById(R.id.option_blue);
        RadioButton orange = (RadioButton)findViewById(R.id.option_orange);
        RadioButton grey = (RadioButton)findViewById(R.id.option_grey);

        boolean a = nombre.length()>0;
        boolean b = green.isChecked() || blue.isChecked() || orange.isChecked() || grey.isChecked();
        if(a && b){
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
            GroupOfActivities newGroup = new GroupOfActivities(nombre, descripcion, color);

            //AÑADIR newGroup A LA BASE DE DATOS

            Toast.makeText(this, getString((R.string.new_group_created)), Toast.LENGTH_LONG).show();
            onBackPressed();
        }else{
            if(!a){
                name.setError(getString(R.string.error_name));
            }
            if(!b){
                errorColour.setVisibility(View.VISIBLE);
            }
        }
    }

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