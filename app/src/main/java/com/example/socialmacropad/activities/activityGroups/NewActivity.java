package com.example.socialmacropad.activities.activityGroups;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmacropad.models.Activity;
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
import com.google.android.material.textfield.TextInputLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        this.getSupportActionBar().hide();

        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        input = (TextInputLayout) findViewById(R.id.outlinedTextFieldInput);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        top = (TextView)findViewById(R.id.textViewTop);
        errorColour = (TextView)findViewById(R.id.textViewErrorColour);
        errorColour.setVisibility(View.GONE);


        //CARGAR VALORES DEL GRUPO SELECCIONADO
        top.setText("nombre_del_grupo" + " > " + getString(R.string.add_new_activity)); //nombre_del_grupo > Add new activity

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
            Activity newActivity = new Activity(nombre, entrada, color);

            //AÑADIR newActivity A LA BASE DE DATOS

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