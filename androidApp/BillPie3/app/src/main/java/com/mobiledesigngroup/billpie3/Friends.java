package com.mobiledesigngroup.billpie3;

import java.util.Map;

/**
 * Created by adam on 2017-12-08.
 */

public class Friends {
    private String name;
    private String phone;
    public Map<String, String> friends;

    public Friends(){

    }

    public Friends(String name, String phone ,Map<String,String> friends) {
        this.name = name;
        this.phone = phone;
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    Map<String , String> getFriends(){
        return friends;
    }

    @Override
    public String toString() {
        return "Friends{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", friends=" + friends +
                '}';
    }
}
