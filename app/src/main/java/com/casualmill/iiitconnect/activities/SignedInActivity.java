package com.casualmill.iiitconnect.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.casualmill.iiitconnect.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignedInActivity extends AppCompatActivity {

    Button button;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_in);

        button = findViewById(R.id.google_log_out);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(SignedInActivity.this,RegisterActivity.class));
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             mAuth.signOut();
            }
        });
    }
}
