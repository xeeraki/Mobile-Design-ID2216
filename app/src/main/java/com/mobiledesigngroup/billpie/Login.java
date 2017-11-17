package com.mobiledesigngroup.billpie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by adam on 2017-11-11.
 */
public class Login extends AppCompatActivity{
    private final ArrayList<User>users;

    public Login(){

        users = new ArrayList<>();
        users.add(new User("jiyayao","0000"));
        users.add(new User("eric","0000"));
        users.add(new User("cassius","0000"));
        users.add(new User("adam","0000"));

    }
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button btnLogin = findViewById(R.id.button);
        final EditText user = findViewById(R.id.username);
        final EditText pass = findViewById(R.id.password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
    String username = user.getText().toString();
    String password = pass.getText().toString();

    Intent intent = new Intent(getBaseContext(), MainActivity.class);
    login(username,password);
    startActivity(intent);
        }
        });


        }

    public User login(String username,String password){
        for(User user: users){
            if(user.getUsername().equals(username)&& user.getPassword().equals(password)){
            }
            return user;
        }
        return null;
    }


}
