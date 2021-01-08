package com.example.socialmacropad.activities.communication;

import android.content.DialogInterface;
import android.content.Intent;
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

        // Setup our Views
        //connectionText = findViewById(R.id.communicate_connection_text);
        //messagesView = findViewById(R.id.communicate_messages);
        //messageBox = findViewById(R.id.communicate_message);
        //sendButton = findViewById(R.id.communicate_send);
        //connectButton = findViewById(R.id.communicate_connect);

        // Start observing the data sent to us by the ViewModel
        viewModel.getConnectionStatus().observe(this, this::onConnectionStatus);
        viewModel.getDeviceName().observe(this, name -> setTitle(getString(R.string.device_name_format, name)));
//        viewModel.getMessages().observe(this, message -> {
//            if (TextUtils.isEmpty(message)) {
//                message = getString(R.string.no_messages);
//            }
//            messagesView.setText(message);
//        });
//        viewModel.getMessage().observe(this, message -> {
//             Only update the message if the ViewModel is trying to reset it
//            if (TextUtils.isEmpty(message)) {
//                messageBox.setText(message);
//            }
//        });

//         Setup the send button click action
//        sendButton.setOnClickListener(v -> viewModel.sendMessage(messageBox.getText().toString()));


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
                onBackPressed();
            }
        });


        Button act1 = (Button) findViewById(R.id.btnNewActivity1);
        try {
            Action action1 = currentGroup.getAction1();
            if (!action1.getActionname().equals("null")) {
                act1.setText(action1.getActionname());
                act1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action1.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        viewModel.sendMessage(action1.getAction() );
                        toast.show();
                    }
                });

            } else {
                act1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}
        act1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog(currentGroup.getAction1());
                return true;
            }
        });


        Button act2 = (Button) findViewById(R.id.btnNewActivity2);
        try {
            Action action2 = currentGroup.getAction2();
            if (!action2.getActionname().equals("null")) { // La actividad ha sido creada
                act2.setText(action2.getActionname());
                act2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action2.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else { // La actividad aún no ha sido creada
                act2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}
        act2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog(currentGroup.getAction2());
                return true;
            }
        });


        Button act3 = (Button) findViewById(R.id.btnNewActivity3);
        try {
            Action action3 = currentGroup.getAction3();
            if (!action3.getActionname().equals("null")) { // La actividad ha sido creada
                act3.setText(action3.getActionname());
                act3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action3.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else { // La actividad aún no ha sido creada
                act3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}
        act3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog(currentGroup.getAction3());
                return true;
            }
        });


        Button act4 = (Button) findViewById(R.id.btnNewActivity4);
        try {
            Action action4 = currentGroup.getAction4();
            if (!action4.getActionname().equals("null")) { // La actividad ha sido creada
                act4.setText(action4.getActionname());
                act4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action4.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else { // La actividad aún no ha sido creada
                act4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}
        act4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog(currentGroup.getAction4());
                return true;
            }
        });


        Button act5 = (Button) findViewById(R.id.btnNewActivity5);
        try {
            Action action5 = currentGroup.getAction5();
            if (!action5.getActionname().equals("null")) { // La actividad ha sido creada
                act5.setText(action5.getActionname());
                act5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action5.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else { // La actividad aún no ha sido creada
                act5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}
        act5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog(currentGroup.getAction5());
                return true;
            }
        });


        Button act6 = (Button) findViewById(R.id.btnNewActivity6);
        try {
            Action action6 = currentGroup.getAction6();
            if (!action6.getActionname().equals("null")) { // La actividad ha sido creada
                act6.setText(action6.getActionname());
                act6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Toast toast = Toast.makeText(getApplicationContext(), "Send" + action6.getAction() + " to bluetooth", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else { // La actividad aún no ha sido creada
                act6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                        Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                        intent.putExtra("currentGroup", new Gson().toJson(currentGroup));
                        startActivity(intent);
                    }
                });
            }

        } catch (Exception e) {}
        act6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog(currentGroup.getAction6());
                return true;
            }
        });


    }

    // Called when the ViewModel updates us of our connectivity status
    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
//                connectionText.setText(R.string.status_connected);
//                messageBox.setEnabled(true);
//                sendButton.setEnabled(true);
//                connectButton.setEnabled(true);
//                connectButton.setText(R.string.disconnect);
//                connectButton.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:
//                connectionText.setText(R.string.status_connecting);
//                messageBox.setEnabled(false);
//                sendButton.setEnabled(false);
//                connectButton.setEnabled(false);
//                connectButton.setText(R.string.connect);
                break;

            case DISCONNECTED:
//                connectionText.setText(R.string.status_disconnected);
//                messageBox.setEnabled(false);
//                sendButton.setEnabled(false);
//                connectButton.setEnabled(true);
//                connectButton.setText(R.string.connect);
//                connectButton.setOnClickListener(v -> viewModel.connect());
                break;
        }
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

    private void editDialog(Action action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_this_activity));
        builder.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(CommunicateActivity.this, EditActivity.class);
                intent.putExtra("action", new Gson().toJson(action));
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
