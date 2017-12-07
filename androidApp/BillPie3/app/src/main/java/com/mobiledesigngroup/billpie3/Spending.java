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
    public Map<String, String> payers;

    public Spending(){
    }

    public Spending(String title, String due_date, String amount, Map<String, String> payers){
        this.title = title;
        this.due_date = due_date;
        this.amount = amount;
        this.payers = payers;
    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public Map<String, String> getPayers() {
        return payers;
    }

    @Override
    public String toString() {
        return "title: " + title + ", amount: " + amount + ", due_date" + due_date + ", payers " + payers;
    }
}
