package com.example.socialmacropad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout username;
    TextInputLayout  password;
    TextInputLayout  email;
    TextInputLayout  phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        username = (TextInputLayout) findViewById(R.id.outlinedTextFieldUsername);
        password = (TextInputLayout) findViewById(R.id.outlinedTextFieldPassword);
        email = (TextInputLayout) findViewById(R.id.outlinedTextFieldEmail);
        phoneNumber = (TextInputLayout) findViewById(R.id.outlinedTextFieldPhone);

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button signUp = (Button)findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

        email.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                esCorreoValido(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void validarDatos() {
        String nombre = username.getEditText().getText().toString();
        String contrasena = password.getEditText().getText().toString();
        String telefono = phoneNumber.getEditText().getText().toString();
        String correo = email.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esTelefonoValido(telefono);
        boolean c = esCorreoValido(correo);
        boolean d = esContrasenaValida(contrasena);

        if (a && b && c && d) {
            //INSERTAR EN LA BD UN USUARIO CON LOS DATOS
            Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
        }
    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30) {
            username.setError("Nombre inválido");
            return false;
        } else {
            username.setError(null);
        }
        return true;
    }

    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches()) {
            phoneNumber.setError("Teléfono inválido");
            return false;
        } else {
            phoneNumber.setError(null);
        }
        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            email.setError("Correo electrónico inválido");
            return false;
        } else {
            email.setError(null);
        }
        return true;
    }

    private boolean esContrasenaValida(String contrasena) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(contrasena).matches() || contrasena.length() < 8 || contrasena.length() > 30) {
            password.setError("Contraseña inválida");
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }
}