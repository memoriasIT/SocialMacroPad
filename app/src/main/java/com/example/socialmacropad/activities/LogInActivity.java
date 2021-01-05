package com.example.socialmacropad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {

    TextInputLayout username;
    TextInputLayout  password;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        this.getSupportActionBar().hide();

        username = (TextInputLayout) findViewById(R.id.outlinedTextFieldUsername);
        password = (TextInputLayout) findViewById(R.id.outlinedTextFieldPassword);
        error = (TextView)findViewById(R.id.textViewError);
        error.setVisibility(View.GONE);

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button logIn = (Button)findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });
    }

    private void validarDatos() {
        String nombre = username.getEditText().getText().toString();
        String contrasena = password.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esContrasenaValida(contrasena);

        if (a && b) {
            if(logIn(nombre, contrasena)){ //Datos correctos -> iniciar sesion

            }else{
                error.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean logIn(String nombre, String contrasena) { //login BD
        return false;
    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30 || nombre.length()<4) {
            username.setError(getString(R.string.non_valid_username));
            return false;
        } else {
            username.setError(null);
        }
        return true;
    }

    private boolean esContrasenaValida(String contrasena) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(contrasena).matches() || contrasena.length() < 4 || contrasena.length() > 30) {
            password.setError(getString(R.string.non_valid_password));
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }

}