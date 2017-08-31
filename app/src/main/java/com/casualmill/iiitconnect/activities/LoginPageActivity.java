package com.casualmill.iiitconnect.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.casualmill.iiitconnect.R;
import com.casualmill.iiitconnect.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPageActivity extends AppCompatActivity {

    //Firebase Variables
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    //EditText views
    EditText mEmail,mPassword;

    //Buttons
    Button mLoginButton;

    //TextViews
    TextView mNotRegistered;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        mAuth = FirebaseAuth.getInstance();

        //EditText Views
        mEmail = findViewById(R.id.roll_number_login);
        mPassword = findViewById(R.id.password_login);

        //login button
        mLoginButton = findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(mEmail.getText().toString(),mPassword.getText().toString());
            }
        });

        mNotRegistered = findViewById(R.id.not_registered_text_view);
        //redirect to MainAvtivity(registration page)
        mNotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPageActivity.this,RegisterActivity.class));
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(LoginPageActivity.this,HomeActivity.class));
                }
            }
        };

    }

    private void login(String email,String password) {
        FirebaseUtils.Authenticate(this, email, password);
    }

}
