package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by eric.
 */

public class Activity extends AppCompatActivity{
    private Map<String, User> userMap;
    private ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.userMap = new HashMap<>();


        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(userSnapshot.getKey(), userSnapshot.getValue(User.class));
                }
                Log.w(TAG, "LAAAAAAAAAAAAAAAAAAAAAAA: " + userMap.toString());
//                displayData();
//                Log.w(TAG, "LAAAAAAAAAAAAAAAAAAAAAAA_HIST: " + histData.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "History: error while retrieving events", databaseError.toException());
            }
        });
    }
}
