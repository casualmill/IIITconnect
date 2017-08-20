package com.casualmill.iiitconnect.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.casualmill.iiitconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                    startActivity(new Intent(LoginPageActivity.this,SignedInActivity.class));
                }
            }
        };

    }

    private void login(String email,String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    startActivity(new Intent(LoginPageActivity.this,SignedInActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LoginPageActivity.this, "Login failed! Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
