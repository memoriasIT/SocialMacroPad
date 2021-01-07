package com.example.socialmacropad.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.socialmacropad.R;

public class CommunicateActivity extends AppCompatActivity {

    private TextView connectionText, messagesView;
    private EditText messageBox;
    private Button sendButton, connectButton;

    private TextView top, groupName;

    private CommunicateViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup our activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        this.getSupportActionBar().hide();

        /*
        // Enable the back button in the action bar if possible
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup our ViewModel
        viewModel = ViewModelProviders.of(this).get(CommunicateViewModel.class);

        // This method return false if there is an error, so if it does, we should close.
        if (!viewModel.setupViewModel(getIntent().getStringExtra("device_name"), getIntent().getStringExtra("device_mac"))) {
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
        viewModel.getMessages().observe(this, message -> {
            if (TextUtils.isEmpty(message)) {
                message = getString(R.string.no_messages);
            }
            messagesView.setText(message);
        });
        viewModel.getMessage().observe(this, message -> {
            // Only update the message if the ViewModel is trying to reset it
            if (TextUtils.isEmpty(message)) {
                messageBox.setText(message);
            }
        });

        // Setup the send button click action
        sendButton.setOnClickListener(v -> viewModel.sendMessage(messageBox.getText().toString()));
        */

        top = (TextView)findViewById(R.id.textViewTop);
        groupName = (TextView)findViewById(R.id.textViewGroupName);
        //CARGAR VALORES DEL GRUPO SELECCIONADO
        top.setText("nombre_del_grupo"+ " > " + getString(R.string.activities));//nombre_grupo > Activities
        groupName.setText("Nombre_del_grupo");

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Button act1 = (Button)findViewById(R.id.btnNewActivity1);
        act1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        act1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog();
                return true;
            }
        });


        Button act2 = (Button)findViewById(R.id.btnNewActivity2);
        act2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        act2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog();
                return true;
            }
        });


        Button act3 = (Button)findViewById(R.id.btnNewActivity3);
        act3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        act3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog();
                return true;
            }
        });


        Button act4 = (Button)findViewById(R.id.btnNewActivity4);
        act4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        act4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog();
                return true;
            }
        });


        Button act5 = (Button)findViewById(R.id.btnNewActivity5);
        act5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        act5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog();
                return true;
            }
        });


        Button act6 = (Button)findViewById(R.id.btnNewActivity6);
        act6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Cuando no hay actividad asociada al boton (Añadir nueva)
                Intent intent = new Intent(CommunicateActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
        act6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editDialog();
                return true;
            }
        });


    }

    // Called when the ViewModel updates us of our connectivity status
    private void onConnectionStatus(CommunicateViewModel.ConnectionStatus connectionStatus) {
        switch (connectionStatus) {
            case CONNECTED:
                connectionText.setText(R.string.status_connected);
                messageBox.setEnabled(true);
                sendButton.setEnabled(true);
                connectButton.setEnabled(true);
                connectButton.setText(R.string.disconnect);
                connectButton.setOnClickListener(v -> viewModel.disconnect());
                break;

            case CONNECTING:
                connectionText.setText(R.string.status_connecting);
                messageBox.setEnabled(false);
                sendButton.setEnabled(false);
                connectButton.setEnabled(false);
                connectButton.setText(R.string.connect);
                break;

            case DISCONNECTED:
                connectionText.setText(R.string.status_disconnected);
                messageBox.setEnabled(false);
                sendButton.setEnabled(false);
                connectButton.setEnabled(true);
                connectButton.setText(R.string.connect);
                connectButton.setOnClickListener(v -> viewModel.connect());
                break;
        }
    }

    // Called when a button in the action bar is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // If the back button was pressed, handle it the normal way
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called when the user presses the back button
    @Override
    public void onBackPressed() {
        // Close the activity
        finish();
    }

    private void editDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_this_activity));
        builder.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(CommunicateActivity.this, EditActivity.class);
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
