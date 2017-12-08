package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class AddEvent extends AppCompatActivity{
    DatabaseReference mDatabase;
    private Map<String, User> userMap;
    private Map<String, Boolean> dataFriendList;
    private Map<String, String> usedFriendList;
    private Map<String, Boolean> friendChosen;
    private EditText eventTitle;
    private String actualUser = "user1";

    private String[] friendList = {"Cassius", "Jiayao", "Adam"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        FloatingActionButton fabCheck = findViewById(R.id.fab_create);
        this.eventTitle = findViewById(R.id.eventTitle);

        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
                // Go to main activity
                finish();
            }
        });

        getFriends();
    }

    // Back button navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void addEvent(){
        DatabaseReference eventRef = mDatabase.child("events");
        String eventId = mDatabase.push().getKey();

        Event event = new Event(eventTitle.getText().toString(),
                new HashMap<String, Spending>(), friendChosen);
        eventRef.child(eventId).setValue(event);
    }

    private void getFriends(){

        DatabaseReference userReference = mDatabase.child("users").child(this.actualUser).child("friends");
        this.dataFriendList = new HashMap<>();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    dataFriendList.put(friendSnapshot.getKey(), friendSnapshot.getValue(Boolean.class));
                }
                createFriendList(dataFriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "GetFriends :error while retrieving friends", databaseError.toException());
            }
        };
        userReference.addValueEventListener(userListener);
    }

    private void createFriendList(final Map<String, Boolean> dataFriendList) {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(userSnapshot.getKey(), userSnapshot.getValue(User.class));
                }
                usedFriendList = new HashMap<>();

                for (Map.Entry<String, Boolean> dataF: dataFriendList.entrySet()) {
                    // Because when we create a user, there is a fake friend put at false
                    if (dataF.getValue()) {
                        // Do that because of the Dialog (inverse)
                        usedFriendList.put(userMap.get(dataF.getKey()).full_name, dataF.getKey());
                    }
                }

                Log.w(TAG, "userFriendList: " + usedFriendList.toString());
                createDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "CreateFriend DB :error while retrieving friends", databaseError.toException());
            }
        });
    }

    private void createDialog() {
        // Dialog friend choice
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.titlef)
                .items(usedFriendList.keySet().toArray(new String[usedFriendList.keySet().size()]))
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        friendChosen = new HashMap<>();
                        for (int i = 0; i < text.length; i++) {
                            friendChosen.put(usedFriendList.get(text[i]), true);
                        }
                        // TODO: Add to the listview page
                        // Add himself
                        friendChosen.put(actualUser, true);
                        Log.w(TAG, "friendChosen :" + friendChosen.toString());
                        return true;
                    }
                })
                .positiveText("Ok");

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });
    }
}