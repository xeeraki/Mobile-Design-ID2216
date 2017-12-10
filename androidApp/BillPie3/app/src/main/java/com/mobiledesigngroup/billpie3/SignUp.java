package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by cassius on 06/12/17.
 */

public class SignUp extends AppCompatActivity {
    private Button btnSignIn, btnSignUp;
    private EditText textEmail, textPass, textFullName;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.btnAlreadyRegistered);
        btnSignUp = (Button) findViewById(R.id.btnSignUpSignUp);
        textFullName = (EditText) findViewById(R.id.fullNameSignUp);
        textEmail = (EditText) findViewById(R.id.emailSignUp);
        textPass = (EditText) findViewById(R.id.passwordSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = textEmail.getText().toString().trim();
                final String fullName = textFullName.getText().toString().trim();
                String password = textPass.getText().toString().trim();

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUp.this, "createUserWithEmailAndPassword:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    createUserAndStartApp(email, fullName);
                                }
                            }
                        });

            }
        });
    }

    private void createUserAndStartApp(String email, String fullName) {
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String userId = mDatabase.push().getKey();
        User newUser = new User("username", "password", "phoneNumber", email, fullName);
        mDatabase.child(userId).setValue(newUser);
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        //intent.putExtra("userId", userId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
