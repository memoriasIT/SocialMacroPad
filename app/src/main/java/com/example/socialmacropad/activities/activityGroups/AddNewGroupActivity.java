package com.example.socialmacropad.activities.activityGroups;


import androidx.annotation.NonNull;
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

import com.example.socialmacropad.activities.bottomNavActivities.MainContent;
import com.example.socialmacropad.models.Action;
import com.example.socialmacropad.models.GroupOfActivities;
import com.example.socialmacropad.models.MacroPad;
import com.example.socialmacropad.util.GroupAdapterHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.socialmacropad.util.Constants.BLUE;
import static com.example.socialmacropad.util.Constants.GREEN;
import static com.example.socialmacropad.util.Constants.GREY;
import static com.example.socialmacropad.util.Constants.ORANGE;

public class AddNewGroupActivity extends AppCompatActivity {

    TextInputLayout name;
    TextInputLayout  description;
    RadioGroup colour;
    TextView errorColour;
    private String TAG = AddNewGroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);
        this.getSupportActionBar().hide();

        // Get UI elements
        name = (TextInputLayout) findViewById(R.id.outlinedTextFieldName);
        description = (TextInputLayout) findViewById(R.id.outlinedTextFieldDescription);
        colour = (RadioGroup) findViewById(R.id.radioGroupColour);
        errorColour = (TextView)findViewById(R.id.textViewErrorColour);
        errorColour.setVisibility(View.GONE);


        // Set button onClickListeners
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

    // Ejecutado al pulsar guardar, valida la entrada
    private void validarDatos() { //Nombre obligatorio, descripcion opcional, color obligatorio
        String nombre = name.getEditText().getText().toString();
        String descripcion = description.getEditText().getText().toString();

        // Color RadioGroup
        RadioButton green = (RadioButton)findViewById(R.id.option_green);
        RadioButton blue = (RadioButton)findViewById(R.id.option_blue);
        RadioButton orange = (RadioButton)findViewById(R.id.option_orange);
        RadioButton grey = (RadioButton)findViewById(R.id.option_grey);

        boolean a = nombre.length()>0;
        boolean b = green.isChecked() || blue.isChecked() || orange.isChecked() || grey.isChecked();
        if(a && b){
            // Get color in string hex format
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

            // Prepare data for adding the group to the database
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Date currentDate = new Date();
            long epoch = currentDate.getTime() / 1000;
            MacroPad macropad = new MacroPad(user.getDisplayName(), user.getUid(), String.valueOf(epoch), nombre, descripcion, color,
                    new Action("null", "null", GREY), new Action("null", "null", GREY), new Action("null", "null", GREY),
                    new Action("null", "null", GREY), new Action("null", "null", GREY), new Action("null", "null", GREY));


            //AÑADIR newGroup A LA BASE DE DATOS
            saveMacroPadToFirestore(macropad);

            // Volver a la página principal
            Toast.makeText(this, getString((R.string.new_group_created)), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainContent.class);
            startActivity(intent);
        }else{
            // An error ocurred in validation
            if(!a){
                name.setError(getString(R.string.error_name));
            }
            if(!b){
                errorColour.setVisibility(View.VISIBLE);
            }
        }
    }

    // Warning al volver atrás para no perder los cambios
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

    // Save macropad to DB
    private void saveMacroPadToFirestore(MacroPad currentPad) {
        // Add macropad to main collection
        FirebaseFirestore.getInstance().collection("macropad").document(currentPad.getPadId()).set(currentPad);

        // Add reference to user macropads
        Map<String, Object> data = new HashMap<>();
        DocumentReference PadRef = FirebaseFirestore.getInstance().document("macropad/"+currentPad.getPadId().trim());
        data.put("padId", PadRef);

        Log.d(TAG, String.valueOf(data));

        String UserID = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("users").document(UserID).collection("pads").document(currentPad.getPadId()).set(data);
    }
}