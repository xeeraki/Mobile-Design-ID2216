package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by cassius on 28/11/17.
 */

public class EventPage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String eventId = "event1";
    private ProgressBar progBar;
    private ScrollView scroll;
    private HorizontalScrollView scrollHorizontal;
    private String[] members = {"Adam", "Cassius", "Jiayao"};
    private LinearLayout linearEventPage, linearLayoutMembers;
    private ArrayList<String> spendingTitle;
    private ArrayList<String> spendingAmount;
    private ArrayList<String> eventMembers;
    private HashMap<String, User> userMap;
    private String eventTitle;
    private String actualUser = "user1";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventpage_dyna);
        this.progBar = findViewById(R.id.indeterminateBarEventPag);
        this.scroll = findViewById(R.id.scroll_card_eventpage);
        this.scrollHorizontal = findViewById(R.id.horizontalScroll_eventpage);
        this.linearEventPage = findViewById(R.id.linear_eventpage);
        this.linearLayoutMembers = findViewById(R.id.linearLayoutMembers);

        actualUser = getIntent().getStringExtra("userId");
        eventId = getIntent().getStringExtra("eventID");
        eventTitle = getIntent().getStringExtra("eventTitle");

        setTitle(eventTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fabAddSpending = findViewById(R.id.fabButtonToAddSpending);
        fabAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventPage.this, AddSpending.class);
                intent.putExtra("eventMembers", eventMembers);
                intent.putExtra("userMap", userMap);
                intent.putExtra("eventID", eventId);
                startActivity(intent);
            }
        });

        Button fabCheckOut = findViewById(R.id.fabButtonToCheckOut);
        fabCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventPage.this, CheckOutPage.class);
                intent.putExtra("eventID", eventId);
                intent.putExtra("userId", actualUser);
                startActivity(intent);
            }
        });

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        scrollHorizontal.setVisibility(View.INVISIBLE);

        spendingTitle = new ArrayList<>();
        spendingAmount = new ArrayList<>();
        eventMembers = new ArrayList<>();

        getUserMapAndSpendingsAndEverything();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getSpendings() {
        DatabaseReference spendingReference = mDatabase.child("events").child(eventId).child("spendings");

        ValueEventListener spendingListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                spendingTitle = new ArrayList<>();
                spendingAmount = new ArrayList<>();
                for (DataSnapshot spendingSnapshot: dataSnapshot.getChildren()) {
                    if (!spendingSnapshot.getKey().equals("defaultSpending")) {
                        Spending retrievedSpending = spendingSnapshot.getValue(Spending.class);
                        final String retrievedTitle = retrievedSpending.getTitle();
                        final String retrievedAmount = retrievedSpending.getAmount();
                        spendingTitle.add(retrievedTitle);
                        spendingAmount.add(retrievedAmount);
                    }
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                scrollHorizontal.setVisibility(View.VISIBLE);
                getEventMembers();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "EventPage:error while retrieving spendings", databaseError.toException());
            }
        };

        spendingReference.addValueEventListener(spendingListener);
    }

    private void getEventMembers() {
        DatabaseReference memberRef = mDatabase.child("events").child(eventId).child("members");

        ValueEventListener memberListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventMembers = new ArrayList<>();
                for (DataSnapshot memberSnapshot: dataSnapshot.getChildren()) {
                    eventMembers.add(memberSnapshot.getKey());
                }
                Log.w(TAG, "members EVENTPAGE: " + eventMembers);
                displayDyna();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "EventPage:error while retrieving members", databaseError.toException());
            }
        };
        memberRef.addValueEventListener(memberListener);
    }

    private void getUserMapAndSpendingsAndEverything() {
        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

            myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();

                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(eventSnapshot.getKey(),
                            eventSnapshot.getValue(User.class));
                }
                Log.w(TAG, "UserMap EVENTPAGE: " + userMap.toString());
                getSpendings();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "UserMap EVENTPAGE: error while retrieving users", databaseError.toException());
            }
        });
    }

    private void displayDyna(){
        displayMembers();
        displaySpendings();
    }

    private void displayMembers() {
        LinearLayout linearLayoutVertical;
        for (String member: eventMembers) {
            linearLayoutVertical = createLinearLayoutVerticalMembers();
            linearLayoutVertical.addView(createImage());
            linearLayoutVertical.addView(createTextNameMember(userMap.get(member).full_name));
            linearLayoutMembers.addView(linearLayoutVertical);
        }
    }

    private void displaySpendings() {
        CardView cardView = createNewCardView();
        View view = createBlueRectangleView();
        cardView.addView(view);
        LinearLayout linearFirst = createVerticalLinearLayout();
        linearFirst.addView(createCardViewTitle("Spendings"));

        int index = 0;
        while (index < spendingTitle.size()) {
            LinearLayout paybackLayout = createHorizontalLinearLayout();
            paybackLayout.addView(createTextStart(spendingTitle.get(index)));
            paybackLayout.addView(createTextEnd("$" + spendingAmount.get(index)));
            linearFirst.addView(paybackLayout);
            index++;
        }
        cardView.addView(linearFirst);
        this.linearEventPage.addView(cardView);
    }

    private ImageView createImage() {
        ImageView image = new ImageView(EventPage.this);
        TableLayout.LayoutParams imageParams;
        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black_24dp));
        imageParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        imageParams.gravity = Gravity.CENTER_HORIZONTAL;
        image.setLayoutParams(imageParams);
        return image;
    }

    private LinearLayout createLinearLayoutVerticalMembers() {
        LinearLayout linear = new LinearLayout(EventPage.this);
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                dpToPixel(60),
                dpToPixel(60)
        );
        linear.setLayoutParams(linearFirstParams);
        linear.setOrientation(LinearLayout.VERTICAL);
        return linear;
    }

    private TextView createCardViewTitle(String text) {
        TextView toPayText = new TextView(EventPage.this);

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

    private TextView createTextNameMember(String text) {
        TextView paybackNameUser = new TextView(EventPage.this);

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

    private TextView createTextStart(String text) {
        TextView paybackNameUser = new TextView(EventPage.this);

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

    private TextView createTextEnd(String text) {
        TextView paybackAmount = new TextView(EventPage.this);

        paybackAmount.setText(text);
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

    private CardView createNewCardView() {
        CardView cardView = new CardView(EventPage.this);

        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(dpToPixel(2) ,dpToPixel(90),dpToPixel(2),dpToPixel(8));
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setLayoutParams(cardParams);
        cardView.setRadius(dpToPixel(2));

        return cardView;
    }

    private LinearLayout createHorizontalLinearLayout() {
        LinearLayout paybackLayout = new LinearLayout(EventPage.this);

        LinearLayout.LayoutParams pbLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paybackLayout.setLayoutParams(pbLayoutParams);
        paybackLayout.setOrientation(LinearLayout.HORIZONTAL);

        return paybackLayout;
    }

    private LinearLayout createVerticalLinearLayout() {
        LinearLayout linearFirst = new LinearLayout(EventPage.this);

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
        View view = new View(EventPage.this);

        ViewGroup.LayoutParams viewParams = new ViewGroup.LayoutParams(
                dpToPixel(20),
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        view.setBackground(getResources().getDrawable(R.drawable.rectangle_indigo));
        view.setLayoutParams(viewParams);

        return view;
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}
