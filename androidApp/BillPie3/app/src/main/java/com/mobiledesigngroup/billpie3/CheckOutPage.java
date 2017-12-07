package com.mobiledesigngroup.billpie3;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CheckOutPage extends AppCompatActivity {
    private Map<String, Paybacks> paybackMap;
    private Map<String, Float> toPayFiltered;
    private Map<String, Float> toReceiveFiltered;
    private HashMap<String, User> userMap;
    private Float totalDue = 0f;
    private LinearLayout linearPay;
    private String actualUser = "user1";
    private String actualEvent = "event1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_page);

        setTitle("Check Out Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.linearPay = findViewById(R.id.linear_pay);

        this.userMap = (HashMap<String, User>) getIntent().getSerializableExtra("useM");

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

/*        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();
                for (DataSnapshot paybackSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(paybackSnapshot.getKey(), paybackSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "CheckOutPage: error while retrieving events", databaseError.toException());
            }
        });*/

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
        totalText.setText("$" + Float.toString(this.totalDue));
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
        if (!toPayFiltered.isEmpty()) {
            LinearLayout linearViewPay = createBeginCard("To pay for this event");

            for (Map.Entry<String, Float> payFilter: this.toPayFiltered.entrySet()) {

                final LinearLayout linearLast = new LinearLayout(this);
                linearLast.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                linearLast.setOrientation(LinearLayout.HORIZONTAL);

                // Create the textView
                TextView text = new TextView(this);

                // set properties
                text.setText(this.userMap.get(payFilter.getKey()).full_name);
                text.setTextColor(Color.BLACK);
                text.setTextSize(15);
                text.setGravity(Gravity.START);
                text.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
                TableLayout.LayoutParams textParams = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                textParams.setMargins(dpToPixel(22), 0, 0, 0);
                text.setLayoutParams(textParams);

                // Create the textView
                TextView text2 = new TextView(this);

                // set properties
                text2.setText("$" + Float.toString(payFilter.getValue()));
                text2.setTextColor(Color.BLACK);
                text2.setTextSize(15);
                text2.setGravity(Gravity.END);
                text2.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
                TableLayout.LayoutParams text2Params = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                text2Params.setMargins(dpToPixel(22), 0, 0, 0);
                text2.setLayoutParams(textParams);

                linearLast.addView(text);
                linearLast.addView(text2);
                linearViewPay.addView(linearLast);
            }
        }


        // Create Card for "To receive"
        if (!toReceiveFiltered.isEmpty()) {
            LinearLayout linearViewPay = createBeginCard("To receive");

            for (Map.Entry<String, Float> receiveFilter: this.toReceiveFiltered.entrySet()) {

                final LinearLayout linearLast = new LinearLayout(this);
                linearLast.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                linearLast.setOrientation(LinearLayout.HORIZONTAL);

                // Create the textView
                TextView text = new TextView(this);

                // set properties
                text.setText(this.userMap.get(receiveFilter.getKey()).full_name);
                text.setTextColor(Color.BLACK);
                text.setTextSize(15);
                text.setGravity(Gravity.START);
                text.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
                TableLayout.LayoutParams textParams = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                textParams.setMargins(dpToPixel(22), 0, 0, 0);
                text.setLayoutParams(textParams);

                // Create the textView
                TextView text2 = new TextView(this);

                // set properties
                text2.setText("$" + Float.toString(receiveFilter.getValue()));
                text2.setTextColor(Color.BLACK);
                text2.setTextSize(15);
                text2.setGravity(Gravity.END);
                text2.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
                TableLayout.LayoutParams text2Params = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                text2Params.setMargins(dpToPixel(22), 0, 0, 0);
                text2.setLayoutParams(textParams);

                linearLast.addView(text);
                linearLast.addView(text2);
                linearViewPay.addView(linearLast);
            }
        }

    }

    private LinearLayout createBeginCard(String title) {
        // create a new CardView
        CardView cardPayView = new CardView(this);

        // set properties
        CardView.LayoutParams cardPayParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardPayParams.setMargins(dpToPixel(2) ,0,dpToPixel(2),dpToPixel(8));
        cardPayView.setCardBackgroundColor(Color.WHITE);
        cardPayView.setLayoutParams(cardPayParams);
        cardPayView.setRadius(dpToPixel(2));

        this.linearPay.addView(cardPayView);

        // create a new View (Left blue rectangle)
        final View viewPay = new View(this);

        // set properties
        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(
                dpToPixel(20),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        viewPay.setBackground(getResources().getDrawable(R.drawable.rectangle_indigo));
        viewPay.setLayoutParams(viewParams);

        cardPayView.addView(viewPay);

        // create the first LinearLayout of the card
        final LinearLayout linearViewPay = new LinearLayout(this);

        // set properties
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearViewPay.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
        linearViewPay.setLayoutParams(linearFirstParams);
        linearViewPay.setOrientation(LinearLayout.VERTICAL);

        cardPayView.addView(linearViewPay);

        // Create the First textView
        TextView titleText = new TextView(this);

        // set properties
        titleText.setText(title);
        titleText.setTextColor(Color.BLACK);
        titleText.setTextSize(18);
        titleText.setTypeface(Typeface.create("@font/roboto_bold", Typeface.BOLD));
        TableLayout.LayoutParams titleTextParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        titleTextParams.setMargins(dpToPixel(22), 0, 0, dpToPixel(8));
        titleText.setLayoutParams(titleTextParams);

        linearViewPay.addView(titleText);

/*        final LinearLayout linearLast = new LinearLayout(this);
        linearLast.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLast.setOrientation(LinearLayout.HORIZONTAL);

        linearViewPay.addView(linearLast);*/

        return linearViewPay;
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}