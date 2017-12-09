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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import static android.content.ContentValues.TAG;

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

    private int ID_generator = 50000;
//    private String amount;  // later mod to number format

    private ArrayList<String> eventMembers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        generalView = getLayoutInflater().inflate(R.layout.add_spendingv3, null);

        setContentView(R.layout.add_spendingv3);

        payers = new HashMap<>();
        nonPayers = new ArrayList<>();
        nbOfPayers = 0;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setTitle("Add a spending");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventMembers = getIntent().getStringArrayListExtra("eventMembers");
        userMap = (HashMap<String, User>) getIntent().getSerializableExtra("userMap");
        eventId = getIntent().getStringExtra("eventID");

        horizontalLinearPayedBy = findViewById(R.id.horizontalLayoutPaidBy);

        displayMembers();


        btnAddSpending = findViewById(R.id.btnSubmitSpending);

        btnAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textAmount = findViewById(R.id.textAmount);
                amount = textAmount.getText().toString();
                EditText textTitle = findViewById(R.id.textTitle);
                title = textTitle.getText().toString();
                getPayers();
                String idSpending = addSpending();
                addPaybacks(idSpending);
                finish();
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void displayMembers() {

        LinearLayout linearLayout;
        int index = 0;
        while (index < eventMembers.size()) {
            linearLayout = createVerticalLinearLayout();
            linearLayout.addView(createToggleButton(index));
            linearLayout.addView(createTextNameMember(userMap.get(eventMembers.get(index)).full_name));
            horizontalLinearPayedBy.addView(linearLayout);
            index++;
        }
    }

    private void getPayers() {
        ToggleButton toggleButton;
        int index = 0;
        for (String member: eventMembers) {
            Log.w(TAG, "EVENTMEMBER : " + member);
            toggleButton = findViewById(ID_generator + index);
            Log.w(TAG, "TOGGLEBUTTON : " + toggleButton.toString());
            if (toggleButton.isChecked()) {
                payers.put(member, "fakeamount");
                nbOfPayers ++;
            }
            else {
                nonPayers.add(member);
            }
            index++;
        }
        for (Map.Entry<String, String> payer: payers.entrySet()) {
            payers.put(payer.getKey(), Float.toString(Float.parseFloat(amount)/nbOfPayers));
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
                dpToPixel(75),
                dpToPixel(100)
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
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        paybackNameUser.setGravity(Gravity.CENTER_HORIZONTAL);
        paybackNameUser.setLayoutParams(paybackNameUserParams);

        return paybackNameUser;
    }

    public ToggleButton createToggleButton(int id) {
        ToggleButton toggleButton = new ToggleButton(AddSpending.this);

        toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.check));
        toggleButton.setFocusable(false);
        toggleButton.setFocusableInTouchMode(false);
        toggleButton.setTextOff("");
        toggleButton.setTextOn("");
        TableLayout.LayoutParams toggleParams = new TableLayout.LayoutParams(
                dpToPixel(35),
                dpToPixel(70)
        );
        Log.w(TAG, "PUTTING TAG : " + id);
        toggleButton.setId(ID_generator + id);
        toggleButton.setChecked(true);
        toggleButton.setChecked(false);
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
