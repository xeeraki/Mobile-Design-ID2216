package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class AddEvent extends AppCompatActivity{
    DatabaseReference mDatabase;
    private String eventId;
    private Map<String, Boolean> dataFriendList;
    private EditText eventTitle;
    private String actualUser = "user1";
//    private ArrayList<String> friendList;

    private String[] friendList = {"Cassius", "Jiayao", "Adam"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        FloatingActionButton fabCheck = findViewById(R.id.fab_create);
        this.eventTitle = findViewById(R.id.eventTitle);

        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        // Dialog friend choice
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.titlef)
                .items(friendList)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        /**
                         * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected check box to actually be selected
                         * (or the newly unselected check box to be unchecked).
                         * See the limited multi choice dialog example in the sample project for details.
                         **/
                        for (int i = 0; i < which.length; i++) {
                            Log.w(TAG, "which i :" + i + ", value: " + which[i].toString());
                        }
                        for (int i = 0; i < text.length; i++) {
                            Log.w(TAG, "text i :" + i + ", value: " + text[i].toString());
                        }
                        return true;
                    }
                })
                .positiveText("Ok");

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });

        // TODO : retrieve the selected friends and put them into a HashMap<String, String>
        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: put on DB
            }
        });

        getFriends();
//        addEvent();

    }

    // Back button navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void addEvent(){
        DatabaseReference eventRef = mDatabase.child("events");
        eventId = mDatabase.push().getKey();

        Event event = new Event(eventTitle.getText().toString(),
                new HashMap<String, Spending>(), new HashMap<String, Boolean>());
        eventRef.child(eventId).setValue(event);
    }

    private void getFriends(){

        DatabaseReference userReference = mDatabase.child("users").child(this.actualUser).child("friends");
        this.dataFriendList = new HashMap<>();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "AddEvent:error while retrieving friends", databaseError.toException());
            }
        };
        userReference.addValueEventListener(userListener);
    }

}

