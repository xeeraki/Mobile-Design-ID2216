package com.mobiledesigngroup.billpie3;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class CreateEvent extends AppCompatActivity{
    private EditText amount, title, date;
    private Button btnSubmit, btnCancel;
    DatabaseReference mDatabase;
    private String eventId;
    private String userId = "user1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.create_event);


        mDatabase =  FirebaseDatabase.getInstance().getReference();

        //getFriends();

        title = (EditText) findViewById(R.id.titleText);
        // TODO : retrieve the selected friends and put them into a HashMap<String, String>
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
    }


/*    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //getFriends();

        title = (EditText) view.findViewById(R.id.titleText);
        // TODO : retrieve the selected friends and put them into a HashMap<String, String>
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        //addEvent();
        return view;
    }*/
    public void addEvent(){
        DatabaseReference eventRef = mDatabase.child("events");
        eventId = mDatabase.push().getKey();

        Event event = new Event(title.getText().toString(), new HashMap<String, Spending>(), new HashMap<String, Boolean>());
        eventRef.child(eventId).setValue(event);
    }

    public void getFriends(){

        DatabaseReference userReference = mDatabase.child("users").child(userId).child("friends");
        final ArrayList<String> friendList = new ArrayList<>();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    String retrievedFriend = friendSnapshot.getValue(String.class);
                    friendList.add(retrievedFriend);
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(CreateEvent.this, android.R.layout.simple_list_item_1, friendList);
                    // TODO : display the friends with a checkbox for each
                    //listView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "CreateEvent:error while retrieving friends", databaseError.toException());
            }

        };

        userReference.addValueEventListener(userListener);
    }

}

