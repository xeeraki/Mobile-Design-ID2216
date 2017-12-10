package com.mobiledesigngroup.billpie3;

import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class AddEvent extends AppCompatActivity{
    DatabaseReference mDatabase;
    private Map<String, User> userMap;
    private Map<String, Boolean> dataFriendList;
    private Map<String, String> usedFriendList;
    private Map<String, Boolean> friendChosen;
    private EditText eventTitle;
    private String actualUser = "user1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        FloatingActionButton fabCheck = findViewById(R.id.fab_create);
        this.eventTitle = findViewById(R.id.eventTitle);

        actualUser = getIntent().getStringExtra("userId");

        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        fabCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvent();
                // Go to main activity
                finish();
            }
        });

        getFriends();
    }

    // Back button navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");

    private void addEvent(){
        DatabaseReference eventRef = mDatabase.child("events");
        String eventId = mDatabase.push().getKey();

        // create fake spending
        Spending fakeSpending = new Spending("defaultSpending", "00/00/0000", "0", new HashMap<String, String>());

        // create Map default spending
        Map<String, Spending> defaultSpend = new HashMap<>();
        defaultSpend.put("defaultSpending", fakeSpending);

        Event event = new Event(eventTitle.getText().toString(), defaultSpend, friendChosen, curFormater.format(new Date()));
        eventRef.child(eventId).setValue(event);
    }

    private void getFriends(){

        DatabaseReference userReference = mDatabase.child("users").child(this.actualUser).child("friends");
        this.dataFriendList = new HashMap<>();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    dataFriendList.put(friendSnapshot.getKey(), friendSnapshot.getValue(Boolean.class));
                }
                createFriendList(dataFriendList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "GetFriends :error while retrieving friends", databaseError.toException());
            }
        };
        userReference.addValueEventListener(userListener);
    }

    private void createFriendList(final Map<String, Boolean> dataFriendList) {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userMap = new HashMap<>();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(userSnapshot.getKey(), userSnapshot.getValue(User.class));
                }
                usedFriendList = new HashMap<>();

                for (Map.Entry<String, Boolean> dataF: dataFriendList.entrySet()) {
                    // Because when we create a user, there is a fake friend put at false
                    if (dataF.getValue()) {
                        // Do that because of the Dialog (inverse)
                        usedFriendList.put(userMap.get(dataF.getKey()).full_name, dataF.getKey());
                    }
                }

                Log.w(TAG, "userFriendList: " + usedFriendList.toString());
                createDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "CreateFriend DB :error while retrieving friends", databaseError.toException());
            }
        });
    }

    private void createDialog() {
        // Dialog friend choice
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.titlef)
                .items(usedFriendList.keySet().toArray(new String[usedFriendList.keySet().size()]))
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        friendChosen = new HashMap<>();
                        for (int i = 0; i < text.length; i++) {
                            friendChosen.put(usedFriendList.get(text[i]), true);
                        }
                        createParticipantsDyna(text);
                        // Add himself
                        friendChosen.put(actualUser, true);
                        Log.w(TAG, "friendChosen :" + friendChosen.toString());
                        return true;
                    }
                })
                .positiveText("Ok");

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });
    }

    private void createParticipantsDyna(CharSequence[] friendChosen) {
        LinearLayout partLinear = findViewById(R.id.participants_linear);

        for (int i = 0; i < friendChosen.length; i++) {
            // create the first LinearLayout
            final LinearLayout linearView = new LinearLayout(this);

            // set properties
            LinearLayout.LayoutParams linearFirstParams = new LinearLayout.LayoutParams(
                    dpToPixel(74),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            linearView.setPadding(dpToPixel(12), dpToPixel(11), 0, 0);
            linearView.setLayoutParams(linearFirstParams);
            linearView.setOrientation(LinearLayout.VERTICAL);

            partLinear.addView(linearView);

            // Crete the image view
            ImageView image = new ImageView(this);
            TableLayout.LayoutParams imageParams;
            image.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black_24dp));
            imageParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.CENTER_HORIZONTAL;
            image.setLayoutParams(imageParams);
            image.setMinimumHeight(dpToPixel(68));
            image.setMinimumWidth(dpToPixel(68));

            linearView.addView(image);

            // Create the Name textView
            TextView nameText = new TextView(this);

            // set properties
            nameText.setText(friendChosen[i]);
            nameText.setTextColor(Color.BLACK);
            nameText.setTextSize(12);
            nameText.setTypeface(Typeface.create("@font/roboto", Typeface.NORMAL));
            TableLayout.LayoutParams nameParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            );
            nameText.setGravity(Gravity.CENTER_HORIZONTAL);
            nameText.setLayoutParams(nameParams);

            linearView.addView(nameText);
        }
    }

    private int dpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }
}