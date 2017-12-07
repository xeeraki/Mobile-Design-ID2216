package com.mobiledesigngroup.billpie3;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by adam on 2017-12-04.
 */

public class EventList {
    private String Header;
    private String date;
    private String rect;


    public EventList(String header, String date) {
        Header = header;
        this.date = date;
        //this.image = image;
    }

    public String getRect() {
        return rect;
    }

    public void setRect(String rect) {
        this.rect = rect;
    }

    private String image;



    public String getHeader() {
        return Header;
    }

    public void setHeader(String header) {
        Header = header;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
