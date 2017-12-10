package com.mobiledesigngroup.billpie3;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
 * Created by cassius
 */
// TODO: Add later the possibility to click and view for each user per event the amount
public class Pay extends Fragment {

    private String actualUser = "user1";

    private ProgressBar progBar;
    private ScrollView scroll;
    private LinearLayout linearPay;

    private Map<String, User> userMap;


    private Map<String, Float> mapOwe;
    private Map<String, Float> mapOwed;
    private Map<String, Float> toReceive;
    private Map<String, Float> toPay;
    private Map<String, Paybacks> receivedPaybackMap;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBarPay);
        this.scroll = view.findViewById(R.id.scroll_card_pay);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        this.linearPay = view.findViewById(R.id.linear_pay);

        actualUser = getActivity().getIntent().getStringExtra("userId");

        this.receivedPaybackMap = new HashMap<>();

        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("paybacks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mapOwe = new HashMap<>();
                mapOwed = new HashMap<>();
                toPay = new HashMap<>();
                toReceive = new HashMap<>();
                for (DataSnapshot pbSnapshot: dataSnapshot.getChildren()) {
                    receivedPaybackMap.put(pbSnapshot.getKey(), pbSnapshot.getValue(Paybacks.class));
                }
                createUserMap();

                Log.w(TAG, "ReceivedPaybackMap: " + receivedPaybackMap.toString());
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
            if (balance < 0)
                toPay.put(receiver, -balance);
            else
                toReceive.put(receiver, balance);
        }
        for (Map.Entry<String, Float> payback: mapOwe.entrySet()) {
            String payer = payback.getKey();
            if (!mapOwed.containsKey(payer)) {
                toPay.put(payer, -mapOwe.get(payer));
            }
        }
        Log.w(TAG, "toPay: " + toPay.toString());
        Log.w(TAG, "toReceive: " + toReceive.toString());
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
        if (!toPay.isEmpty()) {
                Log.w(TAG, "displaying TO PAY cardview: ");
                // create a new CardView
                CardView cardView = createNewCardView();

                // create a new View (Left blue rectangle)
                View view = createBlueRectangleView();

                // add to CardView
                cardView.addView(view);

                /* FIRST LINEAR LAYOUT (VERTICAL)
                * */
                LinearLayout linearFirst = createVerticalLinearLayout();

                /* TEXTVIEW : TO PAY
                * */
                linearFirst.addView(createCardViewTitle("To pay"));

                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

                for (Map.Entry<String, Float> payback: toPay.entrySet()) {
                    LinearLayout paybackLayout = createHorizontalLinearLayout();
                    paybackLayout.setClickable(true);
                    paybackLayout.setBackgroundResource(outValue.resourceId);
                    paybackLayout.addView(createTextReceiver(this.userMap.get(payback.getKey()).full_name));
                    paybackLayout.addView(createTextAmount(payback.getValue()));
                    paybackLayout.addView(createImage());
                    linearFirst.addView(paybackLayout);
                }
                cardView.addView(linearFirst);
                this.linearPay.addView(cardView);
            }

        if (!toReceive.isEmpty()) {

            Log.w(TAG, "displaying TO RECEIVE cardview: ");
            CardView cardView = createNewCardView();

            View view = createBlueRectangleView();

            cardView.addView(view);

            LinearLayout linearFirst = createVerticalLinearLayout();

            linearFirst.addView(createCardViewTitle("To receive"));

            for (Map.Entry<String, Float> payback: toReceive.entrySet()) {
                LinearLayout paybackLayout = createHorizontalLinearLayout();
                paybackLayout.addView(createTextReceiver(this.userMap.get(payback.getKey()).full_name));
                paybackLayout.addView(createTextAmount(payback.getValue()));
                linearFirst.addView(paybackLayout);
            }
            cardView.addView(linearFirst);
            this.linearPay.addView(cardView);
        }
    }

    private TextView createTextReceiver(String text) {
        TextView paybackNameUser = new TextView(getActivity());

        paybackNameUser.setText(text);
        paybackNameUser.setTextColor(Color.BLACK);
        paybackNameUser.setGravity(Gravity.START);
        paybackNameUser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        paybackNameUser.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams paybackNameUserParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        paybackNameUserParams.setMargins(dpToPixel(22), 0, 0, 0);
        paybackNameUser.setLayoutParams(paybackNameUserParams);

        return paybackNameUser;
    }

    private TextView createTextAmount(Float amount) {
        TextView paybackAmount = new TextView(getActivity());

        paybackAmount.setText("$" + amount);
        paybackAmount.setTextColor(Color.BLACK);
        paybackAmount.setGravity(Gravity.END);
        paybackAmount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        paybackAmount.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
        TableLayout.LayoutParams paybackAmountParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        paybackAmountParams.setMargins(dpToPixel(22), 0, 0, 0);
        paybackAmount.setLayoutParams(paybackAmountParams);

        return paybackAmount;
    }

    private LinearLayout createHorizontalLinearLayout() {
        LinearLayout paybackLayout = new LinearLayout(this.getActivity());

        LinearLayout.LayoutParams pbLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paybackLayout.setLayoutParams(pbLayoutParams);
        paybackLayout.setOrientation(LinearLayout.HORIZONTAL);

        return paybackLayout;
    }

    private TextView createCardViewTitle(String text) {
        TextView toPayText = new TextView(getActivity());

        toPayText.setText(text);
        toPayText.setTextColor(Color.BLACK);
        toPayText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        toPayText.setTypeface(Typeface.create("@font/roboto", Typeface.BOLD));
        TableLayout.LayoutParams toPayTextParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        toPayTextParams.setMargins(dpToPixel(22), 0, 0, dpToPixel(8));
        toPayText.setLayoutParams(toPayTextParams);

        return toPayText;
    }

    private LinearLayout createVerticalLinearLayout() {
        LinearLayout linearFirst = new LinearLayout(this.getActivity());

        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearFirst.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
        linearFirst.setLayoutParams(linearFirstParams);
        linearFirst.setOrientation(LinearLayout.VERTICAL);
        return linearFirst;
    }

    private View createBlueRectangleView() {
        View view = new View(this.getActivity());

        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(
                dpToPixel(20),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setBackground(getResources().getDrawable(R.drawable.rectangle_indigo));
        view.setLayoutParams(viewParams);

        return view;
    }

    private CardView createNewCardView() {
        CardView cardView = new CardView(this.getActivity());

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dpToPixel(2) ,0,dpToPixel(2),dpToPixel(8));
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dpToPixel(2));

        return cardView;
    }

    private ImageView createImage() {
        ImageView imageChevron = new ImageView(getActivity());
        TableLayout.LayoutParams imageChevronParams;
        imageChevron.setImageDrawable(getResources().getDrawable(R.drawable.ic_chevron_right_black_24dp));
        imageChevronParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT,
                1.0f
        );
        imageChevronParams.gravity = Gravity.END;
        imageChevron.setAdjustViewBounds(false);
        imageChevron.setLayoutParams(imageChevronParams);
        return imageChevron;
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }

    private void createUserMap() {
        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();
                for (DataSnapshot paybackSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(paybackSnapshot.getKey(), paybackSnapshot.getValue(User.class));
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                displayData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Pay: error while retrieving events", databaseError.toException());
            }
        });
    }
}
