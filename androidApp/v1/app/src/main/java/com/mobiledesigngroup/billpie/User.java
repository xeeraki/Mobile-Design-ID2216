package com.mobiledesigngroup.billpie;

/**
 * Created by adam on 2017-11-13.
 */

public class User {
    private String username;
    private String password;

    public User(String username,String password){
        this.username=username;
        this.password=password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
