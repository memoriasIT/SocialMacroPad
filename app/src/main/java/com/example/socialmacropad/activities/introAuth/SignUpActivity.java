package com.example.socialmacropad.activities.introAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextInputLayout username;
    TextView reqUsername;
    TextInputLayout password;
    TextView reqPassword;
    TextInputLayout email;
    TextView reqEmail;
    TextInputLayout phoneNumber;
    TextView reqPhone;
    String nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        // Get ui elements
        username = (TextInputLayout) findViewById(R.id.outlinedTextFieldUsername);
        reqUsername = (TextView) findViewById(R.id.reqUsername);
        password = (TextInputLayout) findViewById(R.id.outlinedTextFieldPassword);
        reqPassword = (TextView) findViewById(R.id.reqPassword);
        email = (TextInputLayout) findViewById(R.id.outlinedTextFieldEmail);
        reqEmail = (TextView) findViewById(R.id.reqEmail);
        phoneNumber = (TextInputLayout) findViewById(R.id.outlinedTextFieldPhone);
        reqPhone = (TextView) findViewById(R.id.reqPhone);

        // set up On click listeners
        ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button signUp = (Button) findViewById(R.id.btn1_1);
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


//        Get Firebase instance for singing up users
        mAuth = FirebaseAuth.getInstance();
    }

    // validate inputs
    private void validarDatos() {
        nombre = username.getEditText().getText().toString();
        String contrasena = password.getEditText().getText().toString();
        String telefono = phoneNumber.getEditText().getText().toString();
        String correo = email.getEditText().getText().toString();

        boolean a = esNombreValido(nombre);
        boolean b = esTelefonoValido(telefono);
        boolean c = esCorreoValido(correo);
        boolean d = esContrasenaValida(contrasena);

        if (a && b && c && d) {
            // Registrar usuario en firebase
            registrarUsuario(correo, contrasena);

            Toast.makeText(this, getString((R.string.user_registred)), Toast.LENGTH_LONG).show();

            onBackPressed();
        }
    }

    // Add extra data to user in firestore and firebase
    private void addDataToRegister() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(nombre)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated. " + nombre);
                        }
                    }
                });
    }


    // Validate name
    private boolean esNombreValido(String nombre) {
        Pattern patron = Pattern.compile("^[a-zA-Z0-9]+$");
        if (!patron.matcher(nombre).matches() || nombre.length() > 30 || nombre.length() < 4) {
            username.setError(getString(R.string.req_username_password));
            return false;
        } else {
            username.setError(null);
        }
        return true;
    }

    // Validate phone
    private boolean esTelefonoValido(String telefono) {
        if (!Patterns.PHONE.matcher(telefono).matches()) {
            phoneNumber.setError(getString(R.string.req_phone));
            return false;
        } else {
            phoneNumber.setError(null);
        }
        return true;
    }

    // Validate email
    private boolean esCorreoValido(String correo) {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            email.setError(getString(R.string.req_email));
            return false;
        } else {
            email.setError(null);
        }
        return true;
    }

    // Validate password
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

    // Registra usuario en Firebase
    private void registrarUsuario(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:success");

                addDataToRegister();
            }
        });
    }

}