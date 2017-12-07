package com.mobiledesigngroup.billpie3;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by eric.
 */

public class HistoryV2 extends Fragment {
    private ProgressBar progBar;
    private Map<String, Paybacks> paybacksMap;
    private Map<String, Paybacks> paybacksFiltered;
    private String actualUser = "user1"; //TODO: Change when the login is set up

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historyv2, container, false);
        this.progBar = view.findViewById(R.id.prog_hist);

        progBar.setVisibility(View.VISIBLE);

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("paybacks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paybacksFiltered = new HashMap<>();
                paybacksMap = new HashMap<>();

                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    paybacksMap.put(eventSnapshot.getKey(),
                            eventSnapshot.getValue(Paybacks.class));
                }
                progBar.setVisibility(View.INVISIBLE);
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
        filterPaybacks();
        Log.w(TAG, "PaybacksFiltered: " + this.paybacksMap.toString());
        displayDyna();
    }

    private void filterPaybacks() {
        for (Map.Entry<String, Paybacks> payMap: this.paybacksMap.entrySet()) {
            if (payMap.getValue().getPaid() && payMap.getValue().getPayer().equals(this.actualUser)) {
                this.paybacksFiltered.put(payMap.getKey(), payMap.getValue());
            }
        }
    }

    private void displayDyna() {

    }
}
