package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by adam on 2017-11-21.
 */

public class AddFriend extends AppCompatActivity{
    private EditText email;
    private Button btnSubmit;
    private String userId = "user1";
    DatabaseReference mDatabase;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);

        setTitle("Add friend");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //userId = getIntent().getStringExtra("userID");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        email = (EditText) findViewById(R.id.emailFriendInput);
        btnSubmit = (Button) findViewById(R.id.btnSubmitFriend);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailText = email.getText().toString();
                getUserIdByEmailAndAddFriend(emailText);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getUserIdByEmailAndAddFriend(final String emailTextVal) {
        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String retrievedId = "notfound";
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User retrievedUser = userSnapshot.getValue(User.class);
                    if (retrievedUser.email.equals(emailTextVal)) {
                        retrievedId = userSnapshot.getKey();
                        break;
                    }
                }

                Log.w(TAG, "ADDFRIEND: USERID: " + userId);

                if (retrievedId.equals("notfound")) {
                    Toast.makeText(AddFriend.this, "This email does not correspond to any user", Toast.LENGTH_LONG).show();
                    return;
                }

                DatabaseReference friendRef = FirebaseDatabase.getInstance().getReference("users")
                        .child(userId).child("friends");

                friendRef.child(retrievedId).setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "ADDFRIEND: error while retrieving users", databaseError.toException());
            }
        });
    }
}
