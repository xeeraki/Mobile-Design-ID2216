package com.mobiledesigngroup.billpie3;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by adam on 2017-11-13.
 */

@IgnoreExtraProperties
public class User implements Serializable {
    public String username;
    public String password;
    public String full_name;
    public String phoneNumber;
    public String email;
    public Map<String, Boolean> friends;
    public Map<String, Boolean> events;

    // Default constructor (required)
    public User() {
    }

    public User(String username, String password, String phoneNumber, String email, String full_name){
        this.username=username;
        this.password=password;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.full_name = full_name;
        this.friends = new HashMap<>();
        this.friends.put("userCreate", false);
        Log.w(TAG, "Friends!!!!! :" + this.friends.toString());
        this.events = new HashMap<>();
        this.events.put("userCreate", false);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + password + full_name + phoneNumber + email + friends.toString() + events.toString();
    }
}
