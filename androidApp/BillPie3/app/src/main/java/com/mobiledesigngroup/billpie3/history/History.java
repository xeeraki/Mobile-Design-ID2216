package com.mobiledesigngroup.billpie3.history;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiledesigngroup.billpie3.Event;
import com.mobiledesigngroup.billpie3.R;
import com.mobiledesigngroup.billpie3.Spending;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by eric.
 */

public class History extends Fragment {

    private ProgressBar progBar;
    private ScrollView scroll;
    private ArrayList<Event> eventList;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBar);
        this.scroll = view.findViewById(R.id.scroll_card);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();
        this.eventList = new ArrayList<>();

        myDbRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event retrievedEvent = eventSnapshot.getValue(Event.class);
                    eventList.add(retrievedEvent);
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                Log.w(TAG, "LAAAAAAAAAAAAAAAAAAAAAAA: " + eventList.toString());
                displayData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "History: error while retrieving events", databaseError.toException());
            }
        });

        return view;
    }

    private void displayData() {
        for (Event event: this.eventList) {
//            Log.w(TAG, event.getTitle());
            for (Map.Entry<String, Spending> spending: event.getSpendings().entrySet()) {
//                Log.w(TAG, spending.getValue().getTitle());
                for (Map.Entry<String, String> payer: spending.getValue().getPayers().entrySet()) {
                    if (payer.getKey().equals("user1")) {
//                        Log.w(TAG, payer.getValue());
                        Log.w(TAG, "Event Tile: " + event.getTitle() + ", " + "spending title"
                                + spending.getValue().getTitle() + ", "
                                + "user" + payer.getKey() + ", Amount: " + payer.getValue());
                    }
                }
            }
        }
    }
}
