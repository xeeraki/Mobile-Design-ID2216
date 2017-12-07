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
import android.widget.FrameLayout;
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
 * Created by eric.
 */
// TODO: Add later the possibility to click and view for each user per event the amount
public class Pay extends Fragment {

    private final String actualUser = "user1";
    private Map<String, String> userDebt;
    private Map<String, Map<String, String>> users;
    private Map<String, User> receivedUserMap;
    private ProgressBar progBar;
    private ScrollView scroll;
    private LinearLayout linearPay;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBarPay);
        this.scroll = view.findViewById(R.id.scroll_card_pay);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        this.linearPay = view.findViewById(R.id.linear_pay);

        this.receivedUserMap = new HashMap<>();
        this.users = new HashMap<>();
        this.userDebt = new HashMap<>();

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    receivedUserMap.put(userSnapshot.getKey(), userSnapshot.getValue(User.class));
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                Log.w(TAG, "ReceivedUserMap: " + receivedUserMap.toString());
                displayData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "History: error while retrieving events", databaseError.toException());
            }
        });

        return view;
    }

    // TODO: replace by the user
    // TODO: update paid after he pays, then reset for each update the Map
    private void displayData() {
        createUserMap();
        Log.w(TAG, "USERMAP: " + users.toString());
        filterDataUser();
        displayDyna();
    }

    private void createUserMap() {
        for (Map.Entry<String, User> user: this.receivedUserMap.entrySet()) {
            // Check if the user has a payback
            if (user.getValue().paybacks != null) {
                // Loop through all the payback
                for (Map.Entry<String, Payback> payB: user.getValue().paybacks.entrySet()) {
                    // Check if the payback has been paid, if not it's added to the userMap
                    if (!payB.getValue().getPaid()) {
                        // Check if the user who has a debt is already in the map
                        if (this.users.containsKey(user.getKey())) {
                            // Check if the user already has a debt to the same user
                            if (this.users.get(user.getKey()).containsKey(payB.getValue().getUser())) {
                                this.users.get(user.getKey()).put(payB.getValue().getUser(),
                                        this.users.get(user.getKey()).get(payB.getValue().getUser())
                                                + payB.getValue().getAmount());
                            } else {
                                this.users.get(user.getKey()).put(payB.getValue().getUser(),
                                        payB.getValue().getAmount());
                            }
                        } else {
                            Map<String, String> newPayback = new HashMap<>();
                            newPayback.put(payB.getValue().getUser(), payB.getValue().getAmount());
                            this.users.put(user.getKey(), newPayback);
                        }
                    }
                }
            }
        }
    }

    private void filterDataUser() {
        if (!users.containsKey(this.actualUser))  {
            // The user doesn't have debt
        } else {
            // Get his debt before balancing
            this.userDebt = users.get(this.actualUser);
            for (Map.Entry<String, Map<String, String>> userM: users.entrySet()) {
                if (this.userDebt.containsKey(userM.getKey())) {
                    for (Map.Entry<String, String> subUser: userM.getValue().entrySet()) {
                        if (subUser.getKey().equals(this.actualUser)) {
                            this.userDebt.put(userM.getKey(),
                                    Float.toString(Float.parseFloat(this.userDebt.get(userM.getKey()))
                                            - Float.parseFloat(subUser.getValue())));
                        }
                    }
                }
            }
        }
    }

    private void displayDyna(){
        //TODO: Show To pay
        if (users.containsKey(this.actualUser)) {
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
