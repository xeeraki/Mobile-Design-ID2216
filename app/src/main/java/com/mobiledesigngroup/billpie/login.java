package com.mobiledesigngroup.billpie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by adam on 2017-11-11.
 */
public class login extends AppCompatActivity{
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button btnLogin = findViewById(R.id.button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

    Intent intent = new Intent(getBaseContext(), MainActivity.class);
    startActivity(intent);

        }
        });


        }




}
