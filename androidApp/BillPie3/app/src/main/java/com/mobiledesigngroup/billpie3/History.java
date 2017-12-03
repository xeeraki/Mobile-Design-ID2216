package com.mobiledesigngroup.billpie3;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobiledesigngroup.billpie3.Event;
import com.mobiledesigngroup.billpie3.R;
import com.mobiledesigngroup.billpie3.Spending;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by eric.
 */

public class History extends Fragment {
    private class historyData {
        private String spendingTitle;
        private String amount;

        public String getSpendingTitle() {
            return spendingTitle;
        }

        public String getAmount() {
            return amount;
        }

        historyData(String spendingTitle, String amount) {
            this.spendingTitle = spendingTitle;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "spending: " + spendingTitle + ", amount" + amount;
        }
    }

    private ProgressBar progBar;
    private ScrollView scroll;
    private ArrayList<Event> eventList;
    private LinearLayout linearViewCards;
    private Map<String, ArrayList<historyData>> histData;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBar);
        this.scroll = view.findViewById(R.id.scroll_card);
        this.linearViewCards = view.findViewById(R.id.linear_cards);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();
        this.eventList = new ArrayList<>();

        myDbRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event retrievedEvent = eventSnapshot.getValue(Event.class);
                    eventList.add(retrievedEvent);
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                Log.w(TAG, "LAAAAAAAAAAAAAAAAAAAAAAA: " + eventList.toString());
                displayData();
                Log.w(TAG, "LAAAAAAAAAAAAAAAAAAAAAAA_HIST: " + histData.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "History: error while retrieving events", databaseError.toException());
            }
        });

        return view;
    }

    private void displayData() {
        this.histData = new HashMap<>();
        for (Event event: this.eventList) {
            for (Map.Entry<String, Spending> spending: event.getSpendings().entrySet()) {
                for (Map.Entry<String, String> payer: spending.getValue().getPayers().entrySet()) {
                    filterHistory(event, spending, payer);
                }
            }
        }
        createCard();
    }

    // TODO: Change user when the login will be implemented
    private void filterHistory(Event event, Map.Entry<String, Spending> spending, Map.Entry<String, String> payer) {
        if (payer.getKey().equals("user4")) {
            if (!this.histData.containsKey(event.getTitle())) {
                this.histData.put(event.getTitle(),
                        new ArrayList<>(Collections.singletonList(new historyData(spending.getValue().getTitle(), payer.getValue()))));
            } else {
                this.histData.get(event.getTitle())
                        .add(new historyData(spending.getValue().getTitle(), payer.getValue()));
            }
        }
    }

    private void createCard() {
        //final CardView[] eventCards = new CardView[this.histData.size()];
        for (Map.Entry<String, ArrayList<historyData>> histD: histData.entrySet()) {
            // create a new CardView
            final CardView cardView = new CardView(this.getActivity());

            // set properties
            CardView.LayoutParams cardParams = new CardView.LayoutParams(
                    CardView.LayoutParams.FILL_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(dpToPixel(2) ,0,dpToPixel(2),dpToPixel(8));
            cardView.setCardBackgroundColor(Color.WHITE);
            cardView.setLayoutParams(cardParams);
            cardView.setRadius(dpToPixel(2));

            // create the first LinearLayout of the card
            final LinearLayout linearFirst = new LinearLayout(this.getActivity());

            // set properties
            LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            linearFirst.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
            linearFirst.setLayoutParams(linearFirstParams);
            linearFirst.setOrientation(LinearLayout.VERTICAL);

            // Create the First textView
            TextView eventText = new TextView(this.getActivity());

            // set properties
            eventText.setText(histD.getKey());
            eventText.setTextColor(Color.BLACK);
            eventText.setTextSize(18);
            eventText.setTypeface(Typeface.DEFAULT_BOLD);
            TableLayout.LayoutParams eventTextParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );
            eventTextParams.setMargins(dpToPixel(8), dpToPixel(8), dpToPixel(8), dpToPixel(8));
            eventText.setLayoutParams(eventTextParams);

            linearFirst.addView(eventText);

            // Create linear layout for the spendings
            for (historyData histSA: histD.getValue()) {
                // create the linear layout containing the spending title and the amount
                final LinearLayout linearSpend = new LinearLayout(this.getActivity());
                linearSpend.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                linearSpend.setOrientation(LinearLayout.HORIZONTAL);

                // Create textView for spending title
                final TextView spendTextTitle = new TextView(this.getActivity());
                TableLayout.LayoutParams spendTextParams = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        0.20f
                );
                spendTextParams.setMargins(dpToPixel(8f),0,0,0);
                spendTextParams.gravity = Gravity.START;
                spendTextTitle.setLayoutParams(spendTextParams);
                spendTextTitle.setTextColor(Color.BLACK);
                spendTextTitle.setText(histSA.getSpendingTitle());

                // Create textView for amount of the spending
                final TextView amountTextTitle = new TextView(this.getActivity());
                TableLayout.LayoutParams amountTextParams = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        0.80f
                );
                amountTextParams.setMargins(dpToPixel(8f),0,0,0);
                amountTextParams.gravity = Gravity.END;
                amountTextTitle.setLayoutParams(amountTextParams);
                amountTextTitle.setTextColor(Color.BLACK);
                amountTextTitle.setText(histSA.getAmount() + "$");

                linearSpend.addView(spendTextTitle);
                linearSpend.addView(amountTextTitle);

                // Add each linear created to the first linear
                linearFirst.addView(linearSpend);
            }

            cardView.addView(linearFirst);
            this.linearViewCards.addView(cardView);
        }
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}