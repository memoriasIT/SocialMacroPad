package com.example.socialmacropad.activities.communication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.activityGroups.EditActivity;
import com.example.socialmacropad.activities.activityGroups.NewActivity;
import com.example.socialmacropad.activities.bottomNavActivities.MainContent;
import com.example.socialmacropad.activities.introAuth.LogInActivity;
import com.example.socialmacropad.helper.EnhancedSharedPreferences;
import com.example.socialmacropad.models.Action;
import com.example.socialmacropad.models.MacroPad;
import com.google.gson.Gson;

public class CommunicateActivity extends AppCompatActivity {
    private EnhancedSharedPreferences sharedPreferences;
    private TextView connectionText, messagesView;
    private EditText messageBox;
    private Button sendButton, connectButton;
    private MacroPad currentGroup;

    private TextView top, groupName;

    private CommunicateViewModel viewModel;
    private String TAG = CommunicateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        this.getSupportActionBar().hide();


//        // Enable the back button in the action bar if possible
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(CommunicateViewModel.class);

        // This method return false if there is an error, so if it does, we should close.
        sharedPreferences = EnhancedSharedPreferences.getInstance(getApplication(), getString(R.string.sharedPreferencesKey));
        String macAddress = sharedPreferences.getString(getString(R.string.preference_last_connected_device_macAddress), "");
        String deviceName = sharedPreferences.getString(getString(R.string.preference_last_connected_device_name), "");
        Log.d(TAG, macAddress);
        Log.d(TAG, deviceName);
        if (!viewModel.setupViewModel(deviceName, macAddress)) {
            finish();
            return;
        }

        // Start observing the data sent to us by the ViewModel
        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));

        // Set up layout
        top = (TextView) findViewById(R.id.textViewTop);
        groupName = (TextView) findViewById(R.id.textViewGroupName);


        //CARGAR VALORES DEL GRUPO SELECCIONADO
        String jsonCurrentGroup = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonCurrentGroup = extras.getString("currentGroup");
        }
        currentGroup = new Gson().fromJson(jsonCurrentGroup, MacroPad.class);

        top.setText(currentGroup.getName() + " > " + getString(R.string.activities));//nombre_grupo > Activities
        groupName.setText(currentGroup.getName());

        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunicateActivity.this, MainContent.class);
                startActivity(intent);
            }
        });

        // Actions
        Button act1 = (Button) findViewById(R.id.btnNewActivity1);
        try {
            Action action1 = currentGroup.getAction1();
            if (!action1.getActionname().equals("null")) {
                act1.setText(action1.getActionname());
                act1.setBackgroundColor(Color.parseColor(action1.getColor()));
                act1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando hay actividad asociada al boton
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action1.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        viewModel.sendMessage(action1.getAction() );
                        toast.show();
                    }
                });
                act1.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editDialog(currentGroup.getAction1(), 1);
                        return true;
                    }
                });
            } else {
                act1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        intent.putExtra("actID", 1);
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}



        Button act2 = (Button) findViewById(R.id.btnNewActivity2);
        try {
            Action action2 = currentGroup.getAction2();
            if (!action2.getActionname().equals("null")) { // La actividad ha sido creada
                act2.setText(action2.getActionname());
                act2.setBackgroundColor(Color.parseColor(action2.getColor()));
                act2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando hay actividad asociada al boton
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action2.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                act2.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editDialog(currentGroup.getAction2(), 2);
                        return true;
                    }
                });

            } else { // La actividad aún no ha sido creada
                act2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        intent.putExtra("actID", 2);
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}



        Button act3 = (Button) findViewById(R.id.btnNewActivity3);
        try {
            Action action3 = currentGroup.getAction3();
            if (!action3.getActionname().equals("null")) { // La actividad ha sido creada
                act3.setText(action3.getActionname());
                act3.setBackgroundColor(Color.parseColor(action3.getColor()));
                act3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando hay actividad asociada al boton
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action3.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                act3.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editDialog(currentGroup.getAction3(), 3);
                        return true;
                    }
                });

            } else { // La actividad aún no ha sido creada
                act3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        intent.putExtra("actID", 3);
                        startActivity(intent);
                    }
                });

            }

        } catch (Exception e) {}



        Button act4 = (Button) findViewById(R.id.btnNewActivity4);
        try {
            Action action4 = currentGroup.getAction4();
            if (!action4.getActionname().equals("null")) { // La actividad ha sido creada
                act4.setText(action4.getActionname());
                act4.setBackgroundColor(Color.parseColor(action4.getColor()));
                act4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando hay actividad asociada al boton
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action4.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                act4.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editDialog(currentGroup.getAction4(), 4);
                        return true;
                    }
                });

            } else { // La actividad aún no ha sido creada
                act4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        intent.putExtra("actID", 4);
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}



        Button act5 = (Button) findViewById(R.id.btnNewActivity5);
        try {
            Action action5 = currentGroup.getAction5();
            if (!action5.getActionname().equals("null")) { // La actividad ha sido creada
                act5.setText(action5.getActionname());
                act5.setBackgroundColor(Color.parseColor(action5.getColor()));
                act5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando hay actividad asociada al boton
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action5.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                act5.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editDialog(currentGroup.getAction5(), 5);
                        return true;
                    }
                });

            } else { // La actividad aún no ha sido creada
                act5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        intent.putExtra("actID", 5);
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}



        Button act6 = (Button) findViewById(R.id.btnNewActivity6);
        try {
            Action action6 = currentGroup.getAction6();
            if (!action6.getActionname().equals("null")) { // La actividad ha sido creada
                act6.setText(action6.getActionname());
                act6.setBackgroundColor(Color.parseColor(action6.getColor()));
                act6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando hay actividad asociada al boton
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action6.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                act6.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        editDialog(currentGroup.getAction6(), 6);
                        return true;
                    }
                });

            } else { // La actividad aún no ha sido creada
                act6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        intent.putExtra("actID", 6);
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}



    }

    // Called when the ViewModel updates us of our connectivity status
    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {

    }

    // Called when a button in the action bar is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // If the back button was pressed, handle it the normal way
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        viewModel.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        viewModel.disconnect();
    }

    // Called when the user presses the back button
    @Override
    public void onBackPressed() {
        // Close the activity
        finish();
    }

    private void editDialog(Action action, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_this_activity));
        builder.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(CommunicateActivity.this, EditActivity.class);
                intent.putExtra("action", new Gson().toJson(action));
                intent.putExtra("pos", i);

                Log.d("End COMMUNICATEACT", "demo" + currentGroup.getPadId());
                intent.putExtra("macropad", new Gson().toJson(currentGroup));
                startActivity(intent);
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
