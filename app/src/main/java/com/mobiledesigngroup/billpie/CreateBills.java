package com.mobiledesigngroup.billpie;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

/**
 * Created by adam on 2017-11-19.
 */

public class CreateBills extends AppCompatActivity{
    private EditText amount , title;
    private Button btnSubmit , btnCancel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createbill);

        title = (EditText)findViewById(R.id.titleText);
        amount = (EditText)findViewById(R.id.amountText);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        btnCancel = (Button)findViewById(R.id.btn_cancel);

    }



}
