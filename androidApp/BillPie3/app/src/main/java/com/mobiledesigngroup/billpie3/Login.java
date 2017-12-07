package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;


/**
 * Created by adam on 2017-11-11.
 */
public class Login extends AppCompatActivity{
    private final ArrayList<User>users;
    private Button btnSignIn, btnSignUp;
    private EditText login, pass;

    public Login(){

        users = new ArrayList<>();
        users.add(new User("Jiayao","0000", "nb", "email", "Jiayao Yu"));
        users.add(new User("eric","0000", "nb", "email", "Eric Ren"));
        users.add(new User("cassius","0000", "nb", "email", "Cassius Garat"));
        users.add(new User("adam","0000", "nb", "email", "Adam Shafai"));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        login = findViewById(R.id.passwordSignIn);
        pass = findViewById(R.id.emailSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
            String username = login.getText().toString();
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
