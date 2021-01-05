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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    TextInputLayout username;
    TextView reqUsername;
    TextInputLayout  password;
    TextView reqPassword;
    TextInputLayout  email;
    TextView reqEmail;
    TextInputLayout  phoneNumber;
    TextView reqPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        username = (TextInputLayout) findViewById(R.id.outlinedTextFieldUsername);
        reqUsername = (TextView)findViewById(R.id.reqUsername);
        password = (TextInputLayout) findViewById(R.id.outlinedTextFieldPassword);
        reqPassword = (TextView)findViewById(R.id.reqPassword);
        email = (TextInputLayout) findViewById(R.id.outlinedTextFieldEmail);
        reqEmail = (TextView)findViewById(R.id.reqEmail);
        phoneNumber = (TextInputLayout) findViewById(R.id.outlinedTextFieldPhone);
        reqPhone = (TextView)findViewById(R.id.reqPhone);

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
                reqUsername.setVisibility(View.GONE);
                reqEmail.setVisibility(View.GONE);
                reqPassword.setVisibility(View.GONE);
                reqPhone.setVisibility(View.GONE);
                validarDatos();
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
            Toast.makeText(this, getString((R.string.user_registred)), Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30 || nombre.length()<4) {
            username.setError(getString(R.string.req_username_password));
            return false;
        } else {
            username.setError(null);
        }
        return true;
    }

    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches()) {
            phoneNumber.setError(getString(R.string.req_phone));
            return false;
        } else {
            phoneNumber.setError(null);
        }
        return true;
    }

    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            email.setError(getString(R.string.req_email));
            return false;
        } else {
            email.setError(null);
        }
        return true;
    }

    private boolean esContrasenaValida(String contrasena) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(contrasena).matches() || contrasena.length() < 4 || contrasena.length() > 30) {
            password.setError(getString(R.string.req_username_password));
            return false;
        } else {
            password.setError(null);
        }
        return true;
    }
}