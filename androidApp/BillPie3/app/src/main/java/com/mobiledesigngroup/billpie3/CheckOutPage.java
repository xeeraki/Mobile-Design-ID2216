package com.mobiledesigngroup.billpie3;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CheckOutPage extends AppCompatActivity {
    private Map<String, Paybacks> paybackMap;
    private Map<String, Float> toPayFiltered;
    private Map<String, Float> toReceiveFiltered;
    private Float totalDue = 0f;
    private String actualUser = "user1";
    private String actualEvent = "event1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_page);

        setTitle("Check Out Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("paybacks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                paybackMap = new HashMap<>();
                for (DataSnapshot paybackSnapshot: dataSnapshot.getChildren()) {
                    paybackMap.put(paybackSnapshot.getKey(), paybackSnapshot.getValue(Paybacks.class));
                }
                // To create a new when the data changes
                toPayFiltered = new HashMap<>();
                toReceiveFiltered = new HashMap<>();
                displayData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "CheckOutPage: error while retrieving events", databaseError.toException());
            }
        });
    }

    // Back button navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void displayData() {
        filterData();
        Log.w(TAG, "toPayFiltered: " + this.toPayFiltered.toString());
        Log.w(TAG, "toReceiveFiltered: " + this.toReceiveFiltered.toString());
        displayDyna();
    }

    private void filterData() {
        for (Map.Entry<String, Paybacks> payMap: paybackMap.entrySet()) {
            // TODO: EventPage give us the eventID !!!!!
            if (payMap.getValue().getEvent().equals(this.actualEvent)) {
                // First, check "To pay for this event"
                if ((!payMap.getValue().getPaid()) && payMap.getValue().getPayer().equals(this.actualUser)) {
                    if (this.toPayFiltered.containsKey(payMap.getValue().getReceiver())) {
                        this.toPayFiltered.put(payMap.getValue().getReceiver(),
                                this.toPayFiltered.get(payMap.getValue().getReceiver())
                                        + Float.parseFloat(payMap.getValue().getAmount()));
                    } else {
                        this.toPayFiltered.put(payMap.getValue().getReceiver(),
                                Float.parseFloat(payMap.getValue().getAmount()));
                    }
                }

                if ((!payMap.getValue().getPaid()) && payMap.getValue().getReceiver().equals(this.actualUser)) {
                    if (this.toReceiveFiltered.containsKey(payMap.getValue().getPayer())) {
                        this.toReceiveFiltered.put(payMap.getValue().getPayer(),
                                this.toReceiveFiltered.get(payMap.getValue().getPayer())
                                        + Float.parseFloat(payMap.getValue().getAmount()));
                    } else {
                        this.toPayFiltered.put(payMap.getValue().getPayer(),
                                Float.parseFloat(payMap.getValue().getAmount()));
                    }
                }
            }
        }
    }

    private void displayDyna() {
        for (Map.Entry<String, Float> totalPay: this.toPayFiltered.entrySet()) {
            this.totalDue += totalPay.getValue();
        }
        LinearLayout linearTotalDue = findViewById(R.id.linear_total_due);
        // Create text view for Total Due
        TextView totalText1 = new TextView(this);

        // set properties
        totalText1.setText("Total Due:");
        totalText1.setTextColor(Color.BLACK);
        totalText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        totalText1.setGravity(Gravity.START);
        totalText1.setTypeface(Typeface.create("@font/roboto", Typeface.BOLD));
        TableLayout.LayoutParams totalTextParams1 = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        totalTextParams1.setMargins(dpToPixel(22), 0, 0, 0);
        totalText1.setLayoutParams(totalTextParams1);

        // Create text view for Total Due, amount
        TextView totalText = new TextView(this);

        // set properties
        totalText.setText(Float.toString(this.totalDue) + "$");
        totalText.setTextColor(Color.BLACK);
        totalText.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
        totalText.setGravity(Gravity.END);
        totalText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams totalTextParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        totalTextParams.setMargins(dpToPixel(22), 0, 0, 0);
        totalText.setLayoutParams(totalTextParams);

        linearTotalDue.addView(totalText1);
        linearTotalDue.addView(totalText);

        // Create Card for "To pay for this event"
        for (Map.Entry<String, Float> payFilter: this.toPayFiltered.entrySet()) {

        }

        // Create Card for "To receive"
        for (Map.Entry<String, Float> receiveFilter: this.toReceiveFiltered.entrySet()) {

        }
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}