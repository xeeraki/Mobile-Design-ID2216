package com.mobiledesigngroup.billpie3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cassius on 07/12/17. Modded by Jiayao on 08/12/17.
 */

public class AddSpending extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String eventId = "event1";
    int nbOfPayers;
    private Button btnAddSpending;
    private Map<String, String> payers;
    private ArrayList<String> nonPayers;
    private String title, amount, due_date;
    View generalView;
    private String date; // date of spending
    private Map<String, User> userMap;
    private LinearLayout horizontalLinearPayedBy;
//    private String amount;  // later mod to number format

    private ArrayList<String> eventMembers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generalView = getLayoutInflater().inflate(R.layout.add_spendingv3, null);

        setContentView(R.layout.add_spendingv3);

        payers = new HashMap<>();
        nbOfPayers = 0;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        eventMembers = getIntent().getStringArrayListExtra("eventMembers");
        userMap = (HashMap<String, User>) getIntent().getSerializableExtra("userMap");
        eventId = getIntent().getStringExtra("eventID");

        horizontalLinearPayedBy = findViewById(R.id.horizontalLayoutPaidBy);

        displayMembers();

        EditText textAmount = findViewById(R.id.textAmount);
        amount = textAmount.getText().toString();
        btnAddSpending = findViewById(R.id.btnSubmitSpending);

        btnAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textTitle = findViewById(R.id.textTitle);
                title = textTitle.getText().toString();
                getPayers();
                String idSpending = addSpending();
                addPaybacks(idSpending);
            }
        });

        /*final EditText textDate = findViewById(R.id.date);
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
                        selectedmonth+=1;
                        date = selectedday+"/"+selectedmonth+"/"+selectedyear;
                        textDate.setText(selectedday+"/"+selectedmonth+"/"+selectedyear);
                    }
                }, year, month, day);
                datePicker.setTitle("Select date");
                datePicker.show();
            }
        });*/

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
                        month+=1;
                        due_date = dayOfMonth+"/"+month+"/"+year;
                        textDateDue.setText(""+due_date);
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

//        TODO: double-check save spending record to firebase
        //Button btnSave = findViewById(R.id.btnSave);
       /* btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addSpending();
            }
        });*/
    }

    private void displayMembers() {

        LinearLayout linearLayout;

        for (String user: eventMembers) {
            linearLayout = createVerticalLinearLayout();
            linearLayout.addView(createToggleButton(user));
            linearLayout.addView(createTextNameMember(userMap.get(user).full_name));
            horizontalLinearPayedBy.addView(linearLayout);
        }
    }

    private void getPayers() {
        ToggleButton toggleButton;
        for (String member: eventMembers) {
            toggleButton = generalView.findViewWithTag(member);
            if (toggleButton.isChecked()) {
                nbOfPayers ++;
            }
            else {
                nonPayers.add(member);
            }
        }
        for (String member: eventMembers) {
            payers.put(member, Float.toString(Float.parseFloat(amount)/nbOfPayers));
        }
    }

    private String addSpending() {
        DatabaseReference spendingRef = FirebaseDatabase.getInstance().getReference("events")
                .child(eventId).child("spendings");
        String spendingId = spendingRef.push().getKey();

        Spending newSpending = new Spending(title, due_date, amount, payers);
        spendingRef.child(spendingId).setValue(newSpending);
        return spendingId;
    }

    private void addPaybacks(String idSpending) {
        DatabaseReference paybackRef = FirebaseDatabase.getInstance().getReference("paybacks");
        String paybackId;
        int nbMembers = eventMembers.size();
        int nbNonPayers = nonPayers.size();
        float mustPay;
        for (String user: nonPayers) {
            for (Map.Entry<String, String> payer: payers.entrySet()) {
                paybackId = paybackRef.push().getKey();
                mustPay = (Float.parseFloat(payer.getValue()) - Float.parseFloat(amount)/nbMembers)/nbNonPayers;
                Paybacks newPayback = new Paybacks(Float.toString(mustPay), eventId, false, user, payer.getKey(), idSpending, "notpaid");
                paybackRef.child(paybackId).setValue(newPayback);
            }
        }
    }

    private LinearLayout createVerticalLinearLayout() {
        LinearLayout linearFirst = new LinearLayout(AddSpending.this);
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                dpToPixel(80),
                dpToPixel(80)
        );
        linearFirst.setLayoutParams(linearFirstParams);
        linearFirst.setOrientation(LinearLayout.VERTICAL);
        return linearFirst;
    }

    private TextView createTextNameMember(String text) {
        TextView paybackNameUser = new TextView(AddSpending.this);

        paybackNameUser.setText(text);
        paybackNameUser.setTextColor(Color.BLACK);
        paybackNameUser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        paybackNameUser.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams paybackNameUserParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        paybackNameUser.setGravity(Gravity.CENTER_HORIZONTAL);
        paybackNameUser.setLayoutParams(paybackNameUserParams);

        return paybackNameUser;
    }

    private ToggleButton createToggleButton(String id) {
        ToggleButton toggleButton = new ToggleButton(AddSpending.this);

        toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black_24dp));
        toggleButton.setFocusable(false);
        toggleButton.setFocusableInTouchMode(false);
        toggleButton.setTextOff("");
        toggleButton.setTextOn("");
        TableLayout.LayoutParams toggleParams = new TableLayout.LayoutParams(
                dpToPixel(50),
                dpToPixel(50),
                1.0f
        );
        toggleButton.setTag(id);
        toggleParams.gravity = Gravity.CENTER_HORIZONTAL;
        toggleButton.setLayoutParams(toggleParams);

        return toggleButton;
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }

}
