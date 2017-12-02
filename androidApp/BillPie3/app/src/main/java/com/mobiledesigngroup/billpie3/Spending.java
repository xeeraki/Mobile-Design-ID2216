package com.mobiledesigngroup.billpie3;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Spending {
    public String title;
    public String due_date;
    public String amount;
    public String event;
    public Map<String, String> payers;

    public Spending(){
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "title: " + title + ", amount: " + amount + ", due_date" + due_date + ", event: " + event + ", payers " + payers;
    }
}
