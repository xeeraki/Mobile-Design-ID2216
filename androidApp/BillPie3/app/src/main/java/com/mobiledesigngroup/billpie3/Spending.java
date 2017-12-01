package com.mobiledesigngroup.billpie3;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Spending {
    private String title, dueDate, amount;
    private Event event;
    private ArrayList<User> payers;

    public Spending(){
    }
}
