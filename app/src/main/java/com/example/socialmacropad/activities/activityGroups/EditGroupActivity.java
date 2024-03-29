package com.example.socialmacropad.activities.activityGroups;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.bottomNavActivities.MainContent;
import com.example.socialmacropad.models.MacroPad;
import com.example.socialmacropad.util.Constants;
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

public class EditGroupActivity extends AppCompatActivity {
    MacroPad currentGroup;
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

        // Get UI elements
        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        description = (TextInputLayout) findViewById(R.id.outlinedTextFieldDescription);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        top = (TextView)findViewById(R.id.textViewTop);
        groupName = (TextView)findViewById(R.id.textViewGroupName);

        //CARGAR VALORES DEL GRUPO SELECCIONADO
        String jsonCurrentGroup = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonCurrentGroup = extras.getString("currentGroup");
        }
        currentGroup = new Gson().fromJson(jsonCurrentGroup, MacroPad.class);

        top.setText(currentGroup.getName() + getString(R.string.top_edit));//nombre_del_grupo > Edit
        groupName.setText(currentGroup.getName());
        name.getEditText().setText( currentGroup.getName(), TextView.BufferType.EDITABLE);
        description.getEditText().setText(currentGroup.getDescription(), TextView.BufferType.EDITABLE);
        switch (currentGroup.getColor()){
            case Constants.BLUE  : colour.check(R.id.option_blue); break;
            case Constants.GREEN : colour.check(R.id.option_green);break;
            case Constants.GREY : colour.check(R.id.option_grey); break;
            case Constants.ORANGE : colour.check(R.id.option_orange); break;
        }


        // Add OnClickListeners
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

    // Validate inputs
    private void validarDatos() { //Nombre obligatorio, descripcion opcional, color obligatorio
        String nombre = name.getEditText().getText().toString();
        String descripcion = description.getEditText().getText().toString();
        String color = null;

        RadioButton green = (RadioButton)findViewById(R.id.option_green);
        RadioButton blue = (RadioButton)findViewById(R.id.option_blue);
        RadioButton orange = (RadioButton)findViewById(R.id.option_orange);
        RadioButton grey = (RadioButton)findViewById(R.id.option_grey);
        if(green.isChecked()){
            color = GREEN;
        }else if(blue.isChecked()){
            color = BLUE;
        }else if(orange.isChecked()){
            color = ORANGE;
        }else if(grey.isChecked()){
            color = GREY;
        }
        if(nombre.length()>0){//ACTUALIZAR el grupo en la BD
            Map<String, Object> data = new HashMap<>();
            data.put("name", nombre);
            data.put("description", descripcion);
            data.put("color", color);
            FirebaseFirestore.getInstance().collection("macropad").document(currentGroup.getPadId()).update(data);

            // Volver a la activity principal
            Toast.makeText(this, getString((R.string.updated_group)), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainContent.class);
            startActivity(intent);
        }else{
            name.setError(getString(R.string.error_name));
        }
    }


    // Dialog para borrar la actividad o volver
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
                    // Delete from firebase
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    db.collection("users").document(UserID).collection("pads").document(currentGroup.getPadId()).delete();
                    db.collection("macropad").document(currentGroup.getPadId()).delete();

                    // Goto home activity
                    Intent intent = new Intent(getApplicationContext(), MainContent.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainContent.class);
                    startActivity(intent);
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