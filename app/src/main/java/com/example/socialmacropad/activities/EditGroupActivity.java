package com.example.socialmacropad.activities;

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
import com.google.android.material.textfield.TextInputLayout;

public class EditGroupActivity extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout  description;
    RadioGroup colour;
    TextView top;
    TextView groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        this.getSupportActionBar().hide();

        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        description = (TextInputLayout) findViewById(R.id.outlinedTextFieldDescription);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        top = (TextView)findViewById(R.id.textViewTop);
        groupName = (TextView)findViewById(R.id.textViewGroupName);

        //CARGAR VALORES DEL GRUPO SELECCIONADO
        top.setText("nombre_del_seleccionado"+ getString(R.string.top_edit));//nombre_del_grupo > Edit
        groupName.setText("nombre_del_seleccionado");
        name.getEditText().setText( "nombre_del_seleccionado", TextView.BufferType.EDITABLE);
        description.getEditText().setText("descripcion_del_seleciconado", TextView.BufferType.EDITABLE);
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

        Button save = (Button)findViewById(R.id.saveGroup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });
    }

    private void validarDatos() { //Nombre obligatorio, descripcion opcional, color obligatorio
        String nombre = name.getEditText().getText().toString();

        if(nombre.length()>0){//ACTUALIZAR el grupo en la BD

            Toast.makeText(this, getString((R.string.updated_group)), Toast.LENGTH_LONG).show();
            onBackPressed();
        }else{
            name.setError(getString(R.string.error_name));
        }
    }


    private void warningDialog(Boolean delete) {//b:true-> delete    b:false->back
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
                    //SE ELIMINA EL GRUPO ACTUAL
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