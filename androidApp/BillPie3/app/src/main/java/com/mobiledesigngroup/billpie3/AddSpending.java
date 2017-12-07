package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cassius on 07/12/17.
 */

public class AddSpending extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String eventId = "event1";
    private Map<String, String> payers;
    private String title, amount, due_date;

    // eventMembers should be passed from EventPage
    private ArrayList<User> eventMembers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public void addSpending() {
        DatabaseReference spendingRef = FirebaseDatabase.getInstance().getReference("events")
                .child(eventId).child("spendings");
        String spendingId = spendingRef.push().getKey();

        Spending newSpending = new Spending(title, due_date, amount, payers);
        spendingRef.child(spendingId).setValue(newSpending);
    }

    public void calculateSplit() {
        // retrieve who pays what (payers are not forced to share equally between them)
        // go through eventMembers and calculate what members owe to each other
        // insert a new payback in the DB for each reimbursement
    }

}
