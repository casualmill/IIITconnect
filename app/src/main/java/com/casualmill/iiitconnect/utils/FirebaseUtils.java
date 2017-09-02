package com.casualmill.iiitconnect.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.widget.Toast;

import com.casualmill.iiitconnect.activities.NavigationDrawerActivity;
import com.casualmill.iiitconnect.models.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by faztp on 20-Aug-17.
 */

public class FirebaseUtils {

    private static FirebaseUser sUser;

    // static class
    private FirebaseUtils(){}

    public static void Authenticate(final Context mContext, final String id, final String password) {

        //converting the roll number to uppercase
        String capsRollNumber = id.toUpperCase();

        if (!Patterns.EMAIL_ADDRESS.matcher(capsRollNumber).matches()) {
            // username
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            Query user_query = db.child("users").orderByChild("rollNumber").equalTo(capsRollNumber);
            user_query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // dataSnapshot returns list of users
                    if (dataSnapshot.getChildrenCount() > 0) {
                        // get the first element
                        UserInfo userInfo = dataSnapshot.getChildren().iterator().next().getValue(UserInfo.class);
                        if (userInfo != null)
                            Authenticate(mContext, userInfo.email, password);
                    } else {
                        Toast.makeText(mContext, "Roll Number does not exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(mContext, "Oops, something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(id, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    sUser = auth.getCurrentUser();
                    mContext.startActivity(new Intent(mContext, NavigationDrawerActivity.class));
                } else {
                    // If sign in fails, display a message to the user.
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(mContext, "Invalid Password! Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Email does not exist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
