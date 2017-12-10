package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

/**
 * Created by cassius on 06/12/17.
 */

public class SignIn extends AppCompatActivity {
    private Button btnSignIn, btnSignUp, btnReset;
    private EditText textEmail, textPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnReset = (Button) findViewById(R.id.btnResetSignIn);
        textEmail = (EditText) findViewById(R.id.emailSignIn);
        textPass = (EditText) findViewById(R.id.passwordSignIn);

        if (auth.getCurrentUser() != null) {
            auth.signOut();
            //startActivity(new Intent(SignIn.this, MainActivity.class));
            //finish();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, ResetPassword.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = textEmail.getText().toString();
                String password = textPass.getText().toString();

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignIn.this, "Wrong credentials, try again", Toast.LENGTH_LONG).show();
                                } else {
                                    getUserIdAndLogIn(email);
                                }
                            }
                        });
            }
        });
    }

    private void getUserIdAndLogIn(final String email) {
        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String retrievedId = "notfound";
                User retrievedUser = new User();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    retrievedUser = userSnapshot.getValue(User.class);
                    if (retrievedUser.email.equals(email)) {
                        retrievedId = userSnapshot.getKey();
                        break;
                    }
                }

                Log.w(TAG, "SIGNIN: USERID: " + retrievedId);

                if (retrievedId.equals("notfound")) {
                    Toast.makeText(SignIn.this, "User does not exist", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(SignIn.this, MainActivity.class);
                //intent.putExtra("userId", retrievedId);
                //intent.putExtra("userObject", retrievedUser);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "ADDFRIEND: error while retrieving users", databaseError.toException());
            }
        });
    }

}