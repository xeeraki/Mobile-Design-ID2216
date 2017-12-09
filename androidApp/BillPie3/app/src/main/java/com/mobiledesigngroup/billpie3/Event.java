package com.mobiledesigngroup.billpie3;

import java.util.ArrayList;
import java.util.Map;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Event {
    private String create_date;
    private String title;
    private Map<String, Spending> spendings;
    private Map<String, Boolean> members;

    public Event(){
    }

    public Event(String title, Map<String, Spending> spendings, Map<String, Boolean> members, String create_date){
        this.title = title;
        this.spendings = spendings;
        this.members = members;
        this.create_date = create_date;
    }

    public String getCreateDate() {
        return create_date;
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
        return "title: " + title + ", spending: " + spendings.toString() + ", members" + members.toString() + ", create_date " + create_date;
    }
}
