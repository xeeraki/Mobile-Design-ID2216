package com.mobiledesigngroup.billpie3.history;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobiledesigngroup.billpie3.Event;
import com.mobiledesigngroup.billpie3.R;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.SeekBar;


import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by eric.
 */

public class History extends Fragment {
    final private ArrayList<Event> eventsList = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myDbRef = database.getReference();

        myDbRef.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot eventSnap: dataSnapshot.getChildren()) {
                    eventsList.add(eventSnap.getValue(Event.class));
                    Log.w(TAG, "SNAP: " + eventSnap.getValue(Event.class));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("REMOVED");
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("ADDED");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("MOVED");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return view;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NotificationFragment.
     */
    public static History newInstance() {
        History fragment = new History();
        fragment.setRetainInstance(true);
        return fragment;
    }

    public History() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
