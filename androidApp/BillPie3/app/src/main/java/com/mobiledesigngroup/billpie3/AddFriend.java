package com.mobiledesigngroup.billpie3;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by adam on 2017-11-21.
 */

public class AddFriend extends AppCompatActivity{
    private EditText friendName, phone;
    private int userId;
    private Button btnSubmit, btnCancel;
    DatabaseReference mDatabase;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);

        mDatabase = FirebaseDatabase.getInstance().getReference("friends");
        friendName = (EditText) findViewById(R.id.nameText);
        phone = (EditText) findViewById(R.id.phoneText);
        btnSubmit = (Button) findViewById(R.id.btnSubmitFriend);
        btnCancel = (Button) findViewById(R.id.btnCancelFriend);
        addFriend();
    }
    public void addFriend(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =  mDatabase.push().getKey();
                String FriendName = friendName.getText().toString();
                String Phone = phone.getText().toString();
              // if(!TextField.isEmpty(friendName)){
                mDatabase.child(id).setValue(FriendName,Phone);
              /*      Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_LONG).show();
                }*/

            }
        });

    }
}
