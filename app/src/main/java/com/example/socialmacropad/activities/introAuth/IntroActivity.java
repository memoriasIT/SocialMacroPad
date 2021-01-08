package com.example.socialmacropad.activities.introAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.socialmacropad.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class IntroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] languages = {"Select language","Spanish", "English"};
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = IntroActivity.class.getSimpleName();
    private int actualLanguage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException ignored){}
        setContentView(R.layout.activity_main);



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spin = (Spinner) findViewById(R.id.languageSpinner);
        spin.setAdapter(adapter);
        //spin.setOnItemSelectedListener(this);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View vies, int position, long id) {
                if(position!=actualLanguage){
                    actualLanguage = position;
                    setLanguage(position);
                    refresh();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) { }
        });

//        Get Firebase instance for singing up users
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    Log.d("Test", "HELP ME");
                    Log.d("Test", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    addUserToFirestore();

//                    Intent intent = new Intent(SignUpActivity.this, MainContent.class);
//                    startActivity(intent);
                }
            }
        };



        Button learnMore = (Button)findViewById(R.id.learnmore);
        learnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent code for open new activity through intent.
                Intent intent = new Intent(IntroActivity.this, LearnActivity.class);
                startActivity(intent);
            }
        });

        Button signUp = (Button)findViewById(R.id.btn1_1);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent code for open new activity through intent.
                Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        Button logIn = (Button)findViewById(R.id.login);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent code for open new activity through intent.
                Intent intent = new Intent(IntroActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setLanguage(int position) {//1->Spanish, 2->English
        if(position==1){
            Locale locale = new Locale("es");
            Locale.setDefault(locale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //refresh();
        }else if(position==2){
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
            //refresh();
        }

    }

    private void refresh() {
        Button learnMore = (Button)findViewById(R.id.learnmore);
        learnMore.setText(getString(R.string.teachMe));
        Button signUp = (Button)findViewById(R.id.btn1_1);
        signUp.setText(getString(R.string.signup));
        Button logIn = (Button)findViewById(R.id.login);
        logIn.setText(getString(R.string.login));
    }

    private void addUserToFirestore() {
//        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        FirebaseFirestore.getInstance().collection("users").document(UID).set(UID);

        Map<String, Object> data = new HashMap<>();
        String UserID = FirebaseAuth.getInstance().getUid();
        data.put("userId", UserID);

        Log.d(TAG, UserID);
        FirebaseFirestore.getInstance().collection("users").document(UserID).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error writing document", e);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Selected language: "+languages[position] ,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }
}