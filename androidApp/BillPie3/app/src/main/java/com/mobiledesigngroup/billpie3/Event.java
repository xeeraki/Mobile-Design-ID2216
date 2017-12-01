package com.mobiledesigngroup.billpie3;

import java.util.ArrayList;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Event {
    private String title;
    private ArrayList<Spending> spendings;
    private ArrayList<User> members;

    public Event(){
    }

}
