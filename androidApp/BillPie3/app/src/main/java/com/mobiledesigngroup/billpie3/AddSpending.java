package com.mobiledesigngroup.billpie3;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cassius on 07/12/17.
 */

public class AddSpending extends Activity {

    private DatabaseReference mDatabase;
    private String eventId = "event1";
    private Map<String, String> payers;
    private String title, amount, due_date;
    private String description;
//    private String amount;  // later mod to number format

    // eventMembers should be passed from EventPage
    private ArrayList<User> eventMembers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spending);
//        setTitle("Add Spending");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText textSpendingTitle = findViewById(R.id.textTitle);
        title = textSpendingTitle.getText().toString();
//        Button btnDateSpending = findViewById(R.id.btnDateSpending);
//        btnDateSpending.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                showDatePickerDialog(v);
//            }
//        });
        EditText textSpendingAmount = findViewById(R.id.textAmount);
        amount = textSpendingAmount.getText().toString();
        EditText textSpendingDescription = findViewById(R.id.textDescription);
        description = textSpendingDescription.getText().toString();

//        ImageButton btnDiscard = findViewById(R.id.btnDiscard);
//        btnDiscard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AddSpending.this, Events.class));  // go back to Event page
//            }
//        });
        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addSpending();
            }
        });
    }

//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//    }

    public void addSpending() {
        DatabaseReference spendingRef = FirebaseDatabase.getInstance().getReference("events")
                .child(eventId).child("spendings");
        String spendingId = spendingRef.push().getKey();

        Spending newSpending = new Spending(title, due_date, amount, payers);
        spendingRef.child(spendingId).setValue(newSpending);
    }

//    public void calculateSplit() {
//        // retrieve who pays what (payers are not forced to share equally between them)
//        // go through eventMembers and calculate what members owe to each other
//        // insert a new payback in the DB for each reimbursement
//    }

}
