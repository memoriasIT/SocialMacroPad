package com.example.socialmacropad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.getSupportActionBar().hide();

        TextInputLayout username = (TextInputLayout) findViewById(R.id.outlinedTextFieldUsername);
        //extraer el string con username.getEditText().getText().toString();
        TextInputLayout  password = (TextInputLayout) findViewById(R.id.outlinedTextFieldPassword);
        TextInputLayout  email = (TextInputLayout) findViewById(R.id.outlinedTextFieldEmail);
        TextInputLayout  phoneNumber = (TextInputLayout) findViewById(R.id.outlinedTextFieldPhone);

        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}