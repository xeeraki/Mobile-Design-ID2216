package com.mobiledesigngroup.billpie3;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by adam on 2017-11-13.
 */

@IgnoreExtraProperties
public class User {
    public String username;
    public String password;
    public String full_name;
    public String phoneNumber;
    public String email;
    public Map<String, Boolean> friends;
    public Map<String, Boolean> events;
    public Map<String, Payback> paybacks;

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

    @Override
    public String toString() {
        if (paybacks != null) {
            return username + password + full_name + phoneNumber + email + friends.toString() + events.toString() + paybacks.toString();
        }
        return username + password + full_name + phoneNumber + email + friends.toString() + events.toString();
    }
}
