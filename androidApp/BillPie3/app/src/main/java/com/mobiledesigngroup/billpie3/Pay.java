package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private Map<String, User> userMap;
    private ProgressBar progBar;
    private ScrollView scroll;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay, container, false);
        this.progBar = view.findViewById(R.id.indeterminateBarPay);
        this.scroll = view.findViewById(R.id.scroll_card_pay);

        progBar.setVisibility(View.VISIBLE);
        scroll.setVisibility(View.INVISIBLE);

        this.userMap = new HashMap<>();


        DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        myDbRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    userMap.put(userSnapshot.getKey(), userSnapshot.getValue(User.class));
                }
                progBar.setVisibility(View.INVISIBLE);
                scroll.setVisibility(View.VISIBLE);
                Log.w(TAG, "LAAAAAAAAAAAAAAAAAAAAAAA: " + userMap.toString());
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
    private void displayData() {
        for (Map.Entry<String, User> user: this.userMap.entrySet()) {

        }
    }
}
