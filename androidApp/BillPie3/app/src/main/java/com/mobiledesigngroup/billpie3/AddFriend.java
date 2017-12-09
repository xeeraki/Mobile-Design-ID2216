package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

/**
 * Created by adam on 2017-11-21.
 */

public class AddFriend extends AppCompatActivity{
    private EditText friendName, email;
    private String userId = "email";
    private Button btnSubmit, btnCancel;
    DatabaseReference mDatabase;
    private Map<String, String> friends;
    private String name,phone;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        friendName = (EditText) findViewById(R.id.nameText);
        email = (EditText) findViewById(R.id.phoneText);
        btnSubmit = (Button) findViewById(R.id.btnSubmitFriend);
        btnCancel = (Button) findViewById(R.id.btnCancelFriend);
        addFriend();
    }
    public void addFriend(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("users").child("user")
                        .child(userId).child("friends");
                String id =  friendRef.push().getKey();
                String FriendName = friendName.getText().toString();
                String Email = email.getText().toString();
                friendRef.child(id).setValue(FriendName,Email);
              // if(!TextField.isEmpty(friendName)){

              /*      Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_LONG).show();
                }*/

            }
        });

    }
}
