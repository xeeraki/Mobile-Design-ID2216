package com.mobiledesigngroup.billpie3;

import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Event {
    private String title;
    private Map<String, Spending> spendings;
    private Map<String, Boolean> members;

    public Event(){
    }

    public String getTitle() {
        return title;
    }

    public Map<String, Spending> getSpendings() {
        return spendings;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "title: " + title + ", spending: " + spendings.toString() + ", members" + members.toString();
    }
}
