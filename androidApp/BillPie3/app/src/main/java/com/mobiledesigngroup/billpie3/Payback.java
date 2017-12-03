package com.mobiledesigngroup.billpie3;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Payback {
    private String user;
    private String amount;
    private String event;

    public Payback(){
    }

    public String getUser() {
        return user;
    }

    public String getAmount() {
        return amount;
    }

    public String getEvent() {
        return event;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "User: " + user.toString() + ", Amount: " + amount.toString() + ", Event" + event.toString();
    }
}
