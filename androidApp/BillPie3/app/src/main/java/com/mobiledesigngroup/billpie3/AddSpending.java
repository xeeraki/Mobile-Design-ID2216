package com.mobiledesigngroup.billpie3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by cassius on 07/12/17. Modded by Jiayao on 08/12/17.
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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        EditText textTitle = findViewById(R.id.textTitle);
        title = textTitle.getText().toString();
        EditText textAmount = findViewById(R.id.textAmount);
        amount = textAmount.getText().toString();
        EditText textSpendingDescription = findViewById(R.id.textDescription);
        description = textSpendingDescription.getText().toString();

        final EditText textDate = findViewById(R.id.date);
        Calendar currentDate= Calendar.getInstance();
        int year=currentDate.get(Calendar.YEAR);
        int month=currentDate.get(Calendar.MONTH);
        int day=currentDate.get(Calendar.DAY_OF_MONTH);
        textDate.setText(day+"/"+month+"/"+year);

        textDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar currentDate= Calendar.getInstance();
                int year=currentDate.get(Calendar.YEAR);
                int month=currentDate.get(Calendar.MONTH);
                int day=currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(AddSpending.this, new DatePickerDialog.OnDateSetListener(){
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday){
//                        TODO: get date and time as spending date
                        selectedmonth+=1;
                        textDate.setText(selectedday+"/"+selectedmonth+"/"+selectedyear);
                    }
                }, year, month, day);
                datePicker.setTitle("Select date");
                datePicker.show();
            }
        });

        final EditText textDateDue = findViewById(R.id.dateDue);
        textDateDue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar currentDate=Calendar.getInstance();
                int year=currentDate.get(Calendar.YEAR);
                int month=currentDate.get(Calendar.MONTH);
                int day=currentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(AddSpending.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        TODO: get date and time as spending DUE date
                        month+=1;
                        textDateDue.setText(dayOfMonth+"/"+month+"/"+year);
                    }
                }, year, month, day+7);
                datePicker.setTitle("Select due date");
                datePicker.show();
            }
        });


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
