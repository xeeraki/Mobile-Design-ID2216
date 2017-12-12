package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static android.content.ContentValues.TAG;

/**
 * Created by cassius on 09/12/17.
 */

public class EventsV2 extends Fragment {
    private DatabaseReference mDatabase;
    private ProgressBar progBar;
    private ScrollView scroll;
    private LinearLayout linearEvents;
    private Map<String, Event> events;
    private Map<String, User> userMap;
    private String actualUser = "user1";
    private Map<String, Boolean> userEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eventsv2, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBarEvents);
        this.scroll = view.findViewById(R.id.scroll_card_events);
        this.linearEvents = view.findViewById(R.id.linear_events);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);
        userEvents = new HashMap<>();

        //actualUser = getActivity().getIntent().getStringExtra("userId");

        FloatingActionButton fabAddEvent = view.findViewById(R.id.FABevents);
        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEvent.class);
                intent.putExtra("userId", actualUser);
                startActivity(intent);
            }
        });

        mDatabase =  FirebaseDatabase.getInstance().getReference();
        getUsers();

        return view;
    }

    private void getEvents(){
        DatabaseReference eventReference = mDatabase.child("events");

        ValueEventListener enventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events =  new HashMap<>();
                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    Event retrievedEvent = eventSnapshot.getValue(Event.class);
                    if (userEvents.containsKey(eventSnapshot.getKey())) {
                        events.put(eventSnapshot.getKey(), retrievedEvent);
                      }
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                Log.w(TAG, "EventsMap EVENTS: " + events.toString());
                displayDyna();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Events:error while retrieving events", databaseError.toException());
            }
        };
        eventReference.addValueEventListener(enventListener);
    }

    private void getUsers() {
        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();
        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();

                for (DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                    if (eventSnapshot.getKey().equals(actualUser)) {
                            userEvents = eventSnapshot.getValue(User.class).events;
                        }
                    userMap.put(eventSnapshot.getKey(),
                            eventSnapshot.getValue(User.class));
                }
                Log.w(TAG, "UserMap EVENTS: " + userMap.toString());
                getEvents();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "UserMap EVENTS: error while retrieving users", databaseError.toException());
            }
        });
    }

    private void displayDyna() {

//        TypedValue outValue = new TypedValue();
//        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
//        SimpleDateFormat month_date = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);

        for (final Map.Entry<String, Event> eventEntry: events.entrySet()) {

            final Event event = eventEntry.getValue();
            CardView cardView = createNewCardView();

            cardView.setClickable(true);
//            cardView.setBackgroundResource(outValue.resourceId);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventPage.class);
                    intent.putExtra("eventID", eventEntry.getKey());
                    intent.putExtra("eventTitle", event.getTitle());
                    intent.putExtra("userId", actualUser);
                    startActivity(intent);
                    Log.w(TAG, "LOL I JUST CLICKED ON A CARD");
                }
            }) ;

            View view = createBlueRectangleView();
            cardView.addView(view);
            LinearLayout linearVertical = createVerticalLinearLayout();

            LinearLayout linearHorizontal = createHorizontalLinearLayout();

            linearHorizontal.addView(createCardViewTitle(event.getTitle()));

/*            Date date = new Date();
            try {
                Log.w(TAG, "getCreateDate: " + event.getCreateDate());
                 date = sdf.parse(event.getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

//            linearHorizontal.addView(createTextDate(month_date.format(date)));
            //linearHorizontal.addView(createTextDate(event.getCreateDate()));
            linearHorizontal.addView(createImage());

            linearVertical.addView(linearHorizontal);

            LinearLayout linearHorizontalMembers = createHorizontalLinearMargin();

            for (Map.Entry<String, Boolean> userEntry: event.getMembers().entrySet()) {
                LinearLayout linearVerticalMembers = createLinearLayoutVerticalMembers();
                ImageView imageMember = createImageMember();
                TextView nameMember = createNameMember(userMap.get(userEntry.getKey()).full_name);
                linearVerticalMembers.addView(imageMember);
                linearVerticalMembers.addView(nameMember);
                linearHorizontalMembers.addView(linearVerticalMembers);
            }

            linearVertical.addView(linearHorizontalMembers);
            cardView.addView(linearVertical);
            linearEvents.addView(cardView);
        }
    }

    private LinearLayout createLinearLayoutVerticalMembers() {
        LinearLayout linear = new LinearLayout(getActivity());
        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                dpToPixel(60),
                dpToPixel(60)
        );
        linear.setLayoutParams(linearFirstParams);
        linear.setOrientation(LinearLayout.VERTICAL);
        return linear;
    }

    private ImageView createImage() {
        ImageView imageChevron = new ImageView(getActivity());
        TableLayout.LayoutParams imageChevronParams;
        imageChevron.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete_black_24dp));
        imageChevronParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        imageChevron.setAdjustViewBounds(false);
        imageChevron.setLayoutParams(imageChevronParams);
        return imageChevron;
    }

    private TextView createNameMember(String text) {
        TextView paybackNameUser = new TextView(getActivity());

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

    private ImageView createImageMember() {
        ImageView image = new ImageView(getActivity());
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

    private TextView createTextDate(String text) {
        TextView paybackNameUser = new TextView(getActivity());

        paybackNameUser.setText(text);
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

    private LinearLayout createHorizontalLinearLayout() {
        LinearLayout paybackLayout = new LinearLayout(getActivity());

        LinearLayout.LayoutParams pbLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        paybackLayout.setLayoutParams(pbLayoutParams);
        paybackLayout.setOrientation(LinearLayout.HORIZONTAL);

        return paybackLayout;
    }

    private LinearLayout createHorizontalLinearMargin() {
        LinearLayout paybackLayout = new LinearLayout(getActivity());

        LinearLayout.LayoutParams pbLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        pbLayoutParams.setMargins(dpToPixel(22), 0, 0, 8);
        paybackLayout.setLayoutParams(pbLayoutParams);
        paybackLayout.setOrientation(LinearLayout.HORIZONTAL);

        return paybackLayout;
    }

    private LinearLayout createVerticalLinearLayout() {
        LinearLayout linearFirst = new LinearLayout(getActivity());

        LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearFirst.setPadding(dpToPixel(16), dpToPixel(16), dpToPixel(16), dpToPixel(16));
        linearFirst.setLayoutParams(linearFirstParams);
        linearFirst.setOrientation(LinearLayout.VERTICAL);
        return linearFirst;
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
        toPayTextParams.setMargins(dpToPixel(16), 0, 0, dpToPixel(16));
        toPayText.setLayoutParams(toPayTextParams);

        return toPayText;
    }

    private CardView createNewCardView() {
        CardView cardView = new CardView(getActivity());

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

    private View createBlueRectangleView() {
        View view = new View(getActivity());

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