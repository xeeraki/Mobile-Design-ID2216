package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by cassius on 06/12/17.
 */

public class ResetPassword extends AppCompatActivity {

        private EditText textEmail;
        private Button btnReset, btnBack;
        private FirebaseAuth auth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.reset_password);

            textEmail = (EditText) findViewById(R.id.emailResetPassword);
            btnReset = (Button) findViewById(R.id.btnResetPassword);
            btnBack = (Button) findViewById(R.id.btnBackResetPassword);

            auth = FirebaseAuth.getInstance();

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email = textEmail.getText().toString().trim();

                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetPassword.this, "Please check your emails", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ResetPassword.this, "Failed to send a reset email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }

    }
