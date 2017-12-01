package com.mobiledesigngroup.billpie3;
import com.google.firebase.database.IgnoreExtraProperties;
/**
 * Created by cassius on 01/12/17.
 */

@IgnoreExtraProperties
public class Payback {
    private User user;
    private String amount;
    private Event event;

    public Payback(){
    }
}
