package com.mobiledesigngroup.billpie3;

/**
 * Created by adam on 2017-11-13.
 */

public class User {
    private String username, password, phoneNumber, email;

    // Default constructor (required)
    public User() {
    }

    public User(String username, String password, String phoneNumber, String email){
        this.username=username;
        this.password=password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
