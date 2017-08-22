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
import com.casualmill.iiitconnect.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    //Buttons
    Button registerButton;

    //EditText Views
    EditText mUserName,mEmail,mRollNumber,mPassword;

    //TextViews
    TextView mAlreadyRegistered;

    //firebase variables
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        //firebase variables
        mAuth = FirebaseAuth.getInstance();

        //Registering details
        mUserName = findViewById(R.id.register_name);
        mEmail = findViewById(R.id.register_email);
        mRollNumber = findViewById(R.id.register_roll_number);
        mPassword = findViewById(R.id.register_password);

        //already registered TextView
        mAlreadyRegistered = findViewById(R.id.already_registered_text_view);
        //redirect to the login a page
        mAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginPageActivity.class));
            }
        });

        //Buttons
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(mEmail.getText().toString(),mPassword.getText().toString());
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(RegisterActivity.this,SignedInActivity.class));
                }
            }
        };

    }

    private void register(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //if registering is successful
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(RegisterActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //TODO: get the input from the radio button
                    //TODO: check that no fields are empty during registration
                    //acquiring the details of the user
                    String userID = user.getUid();
                    String userName = mUserName.getText().toString();
                    String Email = mEmail.getText().toString();
                    String rollNumber = mRollNumber.getText().toString().toUpperCase();
                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    UserInfo userInfo = new UserInfo(userName,Email,rollNumber);

                    //saving the user provided data to the firebase database
                    databaseReference.child("users").child(userID).setValue(userInfo);

                    //redirect to the signed in activity
                    startActivity(new Intent(RegisterActivity.this,SignedInActivity.class));
                }

                //if registering is failed
                else{
                    Toast.makeText(RegisterActivity.this,"Registering Failed! Please Try again",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }
}








