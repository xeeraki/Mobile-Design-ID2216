package com.mobiledesigngroup.billpie3;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

/**
 * Created by eric.
 */

public class History extends Fragment {
    private ProgressBar progBar;
    private Map<String, Paybacks> paybacksMap;

    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    private Map<String, User> userMap;
    private Map<String, Paybacks> paybacksFiltered;
    private LinearLayout mainLinear;
    private String actualUser = "user1"; //TODO: Change when the login is set up
    private Map<String, Event> eventsMap;
    private DatabaseReference myDbRef;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historyv2, container, false);
        this.progBar = view.findViewById(R.id.prog_hist);
        this.mainLinear = view.findViewById(R.id.linear_hist);


        progBar.setVisibility(View.VISIBLE);

        this.myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventsMap = new HashMap<>();

                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    eventsMap.put(eventSnapshot.getKey(),
                            eventSnapshot.getValue(Event.class));
                }

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
                        Log.w(TAG, "Paybacks: error while retrieving events", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Events: error while retrieving events", databaseError.toException());
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
        for (Map.Entry<String, Paybacks> filMap: this.paybacksFiltered.entrySet()) {
            createCard(filMap.getValue().getSpending(), filMap.getValue().getDate_paid(),
                    filMap.getValue().getEvent(), filMap.getValue().getReceiver(),
                    filMap.getValue().getAmount());
        }
    }

    private void createCard(String spendingTitle, String date, String eventTitle, String name, String amount) {
        // create a new CardView
        CardView cardView = new CardView(this.getActivity());

        // set properties
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dpToPixel(2) ,0,dpToPixel(2),dpToPixel(8));
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dpToPixel(2));

        // create a new View (Left blue rectangle)
        final View view1 = new View(this.getActivity());

        // set properties
        ViewGroup.LayoutParams view1Params = new ViewGroup.LayoutParams(
                dpToPixel(20),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view1.setBackground(getResources().getDrawable(R.drawable.rectangle_orange));
        view1.setLayoutParams(view1Params);

        // create the first LinearLayout of the card
        final LinearLayout linearView = new LinearLayout(this.getActivity());

        // set properties
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearView.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
        linearView.setLayoutParams(linearFirstParams);
        linearView.setOrientation(LinearLayout.VERTICAL);

        // create LinearLayout of the card
        final LinearLayout linearView1 = new LinearLayout(this.getActivity());

        // set properties
        LinearLayout.LayoutParams linearParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearView1.setLayoutParams(linearParams1);
        linearView1.setOrientation(LinearLayout.HORIZONTAL);

        // Create the Spending textView
        TextView titleText = new TextView(this.getActivity());

        // set properties
        titleText.setText(spendingTitle);
        titleText.setTextColor(Color.BLACK);
        titleText.setTextSize(18);
        titleText.setGravity(Gravity.START);
        titleText.setTypeface(Typeface.create("@font/roboto_bold", Typeface.BOLD));
        TableLayout.LayoutParams titleTextParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleTextParams.setMargins(dpToPixel(22), 0, 0, 0);
        titleText.setLayoutParams(titleTextParams);

        // Create the Date textView
        TextView dateText = new TextView(this.getActivity());

        // set properties
        dateText.setText(date);
        dateText.setTextColor(Color.BLACK);
        dateText.setTextSize(13);
        dateText.setGravity(Gravity.END);
        dateText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams titleDateParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleDateParams.setMargins(dpToPixel(22), 0, 0, dpToPixel(8));
        dateText.setLayoutParams(titleDateParams);

        // Create the Event textView
        TextView eventText = new TextView(this.getActivity());

        // set properties
        eventText.setText(this.eventsMap.get(eventTitle).getTitle());
        eventText.setTextColor(Color.BLACK);
        eventText.setTextSize(12);
        eventText.setTypeface(Typeface.create("@font/roboto_light", Typeface.NORMAL));
        TableLayout.LayoutParams titleEventParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        titleEventParams.setMargins(dpToPixel(22), 0, 0, dpToPixel(8));
        eventText.setLayoutParams(titleEventParams);

        // create second LinearLayout of the card
        final LinearLayout linearView2 = new LinearLayout(this.getActivity());

        // set properties
        LinearLayout.LayoutParams linearParams2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearView2.setLayoutParams(linearParams2);
        linearView2.setOrientation(LinearLayout.HORIZONTAL);

        // Create the Name textView
        TextView nameText = new TextView(this.getActivity());

        // set properties
        nameText.setText(this.userMap.get(name).full_name);
        nameText.setTextColor(Color.BLACK);
        nameText.setTextSize(15);
        nameText.setGravity(Gravity.START);
        nameText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams titleNameParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleNameParams.setMargins(dpToPixel(22), 0, 0, 0);
        nameText.setLayoutParams(titleNameParams);

        // Create the Amount textView
        TextView amountText = new TextView(this.getActivity());

        // set properties
        amountText.setText("$" + amount);
        amountText.setTextColor(Color.BLACK);
        amountText.setTextSize(15);
        amountText.setGravity(Gravity.END);
        amountText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams titleAmountParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        titleAmountParams.setMargins(dpToPixel(22), 0, 0, 0);
        amountText.setLayoutParams(titleAmountParams);

        cardView.addView(view1);
        cardView.addView(linearView);

        linearView1.addView(titleText);
        linearView1.addView(dateText);

        linearView2.addView(nameText);
        linearView2.addView(amountText);


        linearView.addView(linearView1);
        linearView.addView(eventText);
        linearView.addView(linearView2);

        this.mainLinear.addView(cardView);
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}
