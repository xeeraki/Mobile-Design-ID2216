package com.mobiledesigngroup.billpie3;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;

/**
 * Created by adam on 2017-11-13.
 */

@IgnoreExtraProperties
public class User {
    private String username, password, full_name, phoneNumber, email;
    private ArrayList<User> friends;
    private ArrayList<Event> events;
    private ArrayList<Payback> paybacks;

    // Default constructor (required)
    public User() {
    }

    public User(String username, String password, String phoneNumber, String email, String full_name){
        this.username=username;
        this.password=password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
