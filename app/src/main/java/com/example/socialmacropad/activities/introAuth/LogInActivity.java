package com.example.socialmacropad.activities.introAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.example.socialmacropad.activities.bottomNavActivities.MainContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LogInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG = LogInActivity.class.getSimpleName();

    TextInputLayout email;
    TextInputLayout  password;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        this.getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        email = (TextInputLayout) findViewById(R.id.outlinedTextFieldEmail);
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
                error.setVisibility(View.GONE);
                validarDatos();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // If user is already registered go to home screen
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(LogInActivity.this, MainContent.class);
            startActivity(intent);
        }
    }

    private void validarDatos() {
        String correo = email.getEditText().getText().toString();
        String contrasena = password.getEditText().getText().toString();

        boolean a = esCorreoValido(correo);
        boolean b = esContrasenaValida(contrasena);

        if (a && b) {
            if(logIn(correo, contrasena)){ //Datos correctos  -> iniciar sesion
                // Go to home screen
                Intent intent = new Intent(LogInActivity.this, MainContent.class);
                startActivity(intent);

            }else{
                error.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean logIn(String email, String contrasena) {
        //Login Firebase
        mAuth.signInWithEmailAndPassword(email, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        return mAuth.getCurrentUser() != null;
    }

    private boolean esCorreoValido(String nombre) {
        // Aunque no lo parezca este es un buen regex porque cumple RFC 5322 lol --> emailregex.com
        Pattern patron = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");
        if (!patron.matcher(nombre).matches()) {
            email.setError(getString(R.string.non_valid_email));
            return false;
        } else {
            email.setError(null);
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