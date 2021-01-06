package com.example.socialmacropad.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.google.android.material.textfield.TextInputLayout;

public class EditActivity extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout  input;
    RadioGroup colour;
    TextView top;
    TextView activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        this.getSupportActionBar().hide();

        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        input = (TextInputLayout) findViewById(R.id.outlinedTextFieldInput);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        top = (TextView)findViewById(R.id.textViewTop);
        activityName = (TextView)findViewById(R.id.textViewActivityName);

        //CARGAR VALORES DE LA ACTIVIDAD SELECCIONADA
        top.setText("nombre_del_grupo"+ " > " + "nombre_de_la_act" + getString(R.string.top_edit));//nombre_grupo > nombew_act > Edit
        activityName.setText("nombre_de_la_act");
        name.getEditText().setText( "nombre_de_la_act", TextView.BufferType.EDITABLE);
        input.getEditText().setText("input_del_seleciconado", TextView.BufferType.EDITABLE);
        //check al radio button del color correspondiente

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

    private void validarDatos() { //Nombre obligatorio, input obligatorio, color obligatorio
        String nombre = name.getEditText().getText().toString();
        String entrada = input.getEditText().getText().toString();

        boolean a = nombre.length() > 0;
        boolean b = entrada.length() > 0;
        if(a && b){//ACTUALIZAR la actividad en la BD

            Toast.makeText(this, getString((R.string.updated_activity)), Toast.LENGTH_LONG).show();
            onBackPressed();
        }else{
            if(!a){
                name.setError(getString(R.string.error_name));
            }
            if(!b){
                input.setError(getString(R.string.error_input));
            }
        }
    }

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