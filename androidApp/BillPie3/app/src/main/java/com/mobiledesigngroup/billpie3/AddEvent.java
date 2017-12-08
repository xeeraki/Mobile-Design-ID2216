package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddEvent extends AppCompatActivity{
    private EditText amount, title, date;
    DatabaseReference mDatabase;
    private String eventId;
    private String actualUser = "user1";
    private FloatingActionButton fabAdd;
    private FloatingActionButton fabCheck;

    private String[] friendList = {"Cassius", "Jiayao", "Adam"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        this.fabAdd = findViewById(R.id.fab_add);
        this.fabCheck = findViewById(R.id.fab_create);

        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        // Dialog friend choice
        final MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.titlef)
                .items(friendList)
                .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        /**
                         * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected check box to actually be selected
                         * (or the newly unselected check box to be unchecked).
                         * See the limited multi choice dialog example in the sample project for details.
                         **/
                        return true;
                    }
                })
                .positiveText("Ok");

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });


        //getFriends();

        title = (EditText) findViewById(R.id.titleText);
        // TODO : retrieve the selected friends and put them into a HashMap<String, String>
//        btnSubmit = (Button) findViewById(R.id.btn_submit);
//        btnCancel = (Button) findViewById(R.id.btn_cancel);
    }

    // Back button navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


/*    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //getFriends();

        title = (EditText) view.findViewById(R.id.titleText);
        // TODO : retrieve the selected friends and put them into a HashMap<String, String>
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        //addEvent();
        return view;
    }*/
 /*   public void addEvent(){
        DatabaseReference eventRef = mDatabase.child("events");
        eventId = mDatabase.push().getKey();

        Event event = new Event(title.getText().toString(), new HashMap<String, Spending>(), new HashMap<String, Boolean>());
        eventRef.child(eventId).setValue(event);
    }

    public void getFriends(){

        DatabaseReference userReference = mDatabase.child("users").child(userId).child("friends");
        final ArrayList<String> friendList = new ArrayList<>();

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnapshot: dataSnapshot.getChildren()) {
                    String retrievedFriend = friendSnapshot.getValue(String.class);
                    friendList.add(retrievedFriend);
                    ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(AddEvent.this, android.R.layout.simple_list_item_1, friendList);
                    // TODO : display the friends with a checkbox for each
                    //listView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "AddEvent:error while retrieving friends", databaseError.toException());
            }

        };

        userReference.addValueEventListener(userListener);
    }
*/
}

