package com.mobiledesigngroup.billpie3;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by cassius
 */
// TODO: Add later the possibility to click and view for each user per event the amount
public class PayV2 extends Fragment {

    private final String actualUser = "user1";
    private Map<String, String> userDebt;
    private Map<String, Map<String, String>> userMap;

    private ProgressBar progBar;
    private ScrollView scroll;
    private LinearLayout linearPay;

    private Map<String, Float> mapOwe;
    private Map<String, Float> mapOwed;
    private Map<String, Float> balanceMap;
    private Map<String, Paybacks> receivedPaybackMap;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBarPay);
        this.scroll = view.findViewById(R.id.scroll_card_pay);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        this.linearPay = view.findViewById(R.id.linear_pay);

        this.receivedPaybackMap = new HashMap<>();
        this.userMap = new HashMap<>();
        this.userDebt = new HashMap<>();

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("paybacks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapOwe = new HashMap<>();
                mapOwed = new HashMap<>();
                balanceMap = new HashMap<>();
                for (DataSnapshot pbSnapshot: dataSnapshot.getChildren()) {
                    receivedPaybackMap.put(pbSnapshot.getKey(), pbSnapshot.getValue(Paybacks.class));
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                Log.w(TAG, "ReceivedPaybackMap: " + receivedPaybackMap.toString());
                displayData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Pay: error while retrieving paybacks", databaseError.toException());
            }
        });

        return view;
    }

    private void createMaps() {
        for (Map.Entry<String, Paybacks> payback: this.receivedPaybackMap.entrySet()) {
            Paybacks pb = payback.getValue();
            // Check if the payback is already paid
            if (!pb.getPaid()) {
                if (pb.getPayer().equals(actualUser)) {
                    String receiver = pb.getReceiver();
                    if (mapOwe.containsKey(receiver)) {
                        mapOwe.put(receiver, mapOwe.get(receiver) - Float.parseFloat(pb.getAmount()));
                    }
                    else {
                        mapOwe.put(receiver, - Float.parseFloat(pb.getAmount()));
                    }
                }
                else {
                    if (pb.getReceiver().equals(actualUser)) {
                        String payer = pb.getPayer();
                        if (mapOwed.containsKey(payer)) {
                            mapOwed.put(payer, mapOwed.get(payer) + Float.parseFloat(pb.getAmount()));
                        }
                        else {
                            mapOwed.put(payer, Float.parseFloat(pb.getAmount()));
                        }
                    }
                }
            }
        }
        Log.w(TAG, "mapOwe: " + mapOwe.toString());
        Log.w(TAG, "mapOwed: " + mapOwed.toString());
    }

    private void balanceMaps() {
        /*
        * After this method is called, this.balanceMap contains the names of each user's friend and
        * a negative/positive amount (+ : friend owes something to user, - : user owes something to friend)
        * */
        for (Map.Entry<String, Float> payback: mapOwed.entrySet()) {
            String receiver = payback.getKey();
            Float balance = payback.getValue();
            if (mapOwe.containsKey(receiver)) {
                balance += mapOwe.get(receiver);
            }
            balanceMap.put(receiver, balance);
        }
        for (Map.Entry<String, Float> payback: mapOwe.entrySet()) {
            String payer = payback.getKey();
            if (!mapOwed.containsKey(payer)) {
                balanceMap.put(payer, mapOwe.get(payer));
            }
        }
        Log.w(TAG, "balanceMap: " + balanceMap.toString());
    }

    // TODO: replace by the user
    // TODO: update paid after he pays, then reset for each update the Map
    private void displayData() {
        createMaps();
        balanceMaps();
        displayDyna();
    }


    private void displayDyna(){
        //TODO: Show To pay
        if (userMap.containsKey(this.actualUser)) {
            for (Map.Entry<String, String> userD: userDebt.entrySet()) {
                // create a new CardView
                final CardView cardView = new CardView(this.getActivity());

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
                final View view = new View(this.getActivity());

                // set properties
                ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(
                        dpToPixel(20),
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                view.setBackground(getResources().getDrawable(R.drawable.rectangle_indigo));
                view.setLayoutParams(viewParams);

                // add to CardView
                cardView.addView(view);

                // create a first LinearLayout for the text
                final LinearLayout linearFirst = new LinearLayout(this.getActivity());

                // set properties
                LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                linearFirst.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
                linearFirst.setLayoutParams(linearFirstParams);
                linearFirst.setOrientation(LinearLayout.VERTICAL);

                this.linearPay.addView(cardView);
            }
        } else {
            //TODO: Show To receive
        }
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}
