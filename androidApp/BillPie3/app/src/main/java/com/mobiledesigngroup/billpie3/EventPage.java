package com.mobiledesigngroup.billpie3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by cassius on 28/11/17.
 */

public class EventPage extends Fragment {

    private DatabaseReference mDatabase;
    private String eventId = "event1";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_page, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.eventPageListView);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        final ArrayList<String> spendings = new ArrayList<>();

        DatabaseReference spendingReference = mDatabase.child("events").child(eventId).child("spendings");

        ValueEventListener spendingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot spendingSnapshot: dataSnapshot.getChildren()) {
                    Spending retrievedSpending = spendingSnapshot.getValue(Spending.class);
                    spendings.add(retrievedSpending.getTitle());
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spendings);
                    listView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "EventPage:error while retrieving spendings", databaseError.toException());
            }

        };

        spendingReference.addValueEventListener(spendingListener);

        return view;
    }
}
