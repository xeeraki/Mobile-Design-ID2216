package com.mobiledesigngroup.billpie3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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

public class AddSpending extends Activity {

    private DatabaseReference mDatabase;
    private String eventId = "event1";
    private Map<String, String> payers;
    private String title, amount, due_date;
    private String date; // date of spending
    private String description;
    private Map<String, User> userMap;
    private LinearLayout horizontalLinearPayedBy;
    private LinearLayout horizontalLinearSharedWith;
//    private String amount;  // later mod to number format

    // eventMembers should be passed from EventPage
    private ArrayList<String> eventMembers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_spendingv3);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        eventMembers = getIntent().getStringArrayListExtra("eventMembers");
        userMap = (HashMap<String, User>) getIntent().getSerializableExtra("userMap");

        horizontalLinearPayedBy = findViewById(R.id.horizontalLayoutPaidBy);
        horizontalLinearSharedWith = findViewById(R.id.horizontalLayoutSharedWith);

        displayMembers();

        EditText textTitle = findViewById(R.id.textTitle);
        title = textTitle.getText().toString();
        EditText textAmount = findViewById(R.id.textAmount);
        amount = textAmount.getText().toString();

//        fixed friends list rather than from firebase
        /*boolean[] boolPayers = new boolean[4];
        ToggleButton btnPayer1 = findViewById(R.id.tgBtnPayer1);
        boolPayers[0]=btnPayer1.isChecked();
        ToggleButton btnPayer2 = findViewById(R.id.tgBtnPayer2);
        boolPayers[1] = btnPayer2.isChecked();
        ToggleButton btnPayer3 = findViewById(R.id.tgBtnPayer3);
        boolPayers[2] = btnPayer3.isChecked();
        ToggleButton btnPayer4 = findViewById(R.id.tgBtnPayer4);
        boolPayers[3] = btnPayer4.isChecked();*/
//        String payers: boolean boolPayers[]

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
            linearLayout.addView(createToggleButton());
            linearLayout.addView(createTextNameMember(userMap.get(user).full_name));
            horizontalLinearPayedBy.addView(linearLayout);

            linearLayout = createVerticalLinearLayout();
            linearLayout.addView(createToggleButton());
            linearLayout.addView(createTextNameMember(userMap.get(user).full_name));
            horizontalLinearSharedWith.addView(linearLayout);
        }
    }

    private LinearLayout createVerticalLinearLayout() {
        LinearLayout linearFirst = new LinearLayout(AddSpending.this);
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                dpToPixel(75),
                dpToPixel(75)
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

    private ToggleButton createToggleButton() {
        ToggleButton toggleButton = new ToggleButton(AddSpending.this);

        toggleButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black_24dp));
        toggleButton.setFocusable(false);
        toggleButton.setFocusableInTouchMode(false);
        TableLayout.LayoutParams toggleParams = new TableLayout.LayoutParams(
                dpToPixel(55),
                dpToPixel(55),
                1.0f
        );
        toggleParams.gravity = Gravity.CENTER_HORIZONTAL;
        toggleButton.setLayoutParams(toggleParams);

        return toggleButton;
    }

    public void addSpending() {
        DatabaseReference spendingRef = FirebaseDatabase.getInstance().getReference("events")
                .child(eventId).child("spendings");
        String spendingId = spendingRef.push().getKey();

        Spending newSpending = new Spending(title, due_date, amount, payers);
        spendingRef.child(spendingId).setValue(newSpending);
    }


    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }

//    public void calculateSplit() {
//        // retrieve who pays what (payers are not forced to share equally between them)
//        // go through eventMembers and calculate what members owe to each other
//        // insert a new payback in the DB for each reimbursement
//    }

}
