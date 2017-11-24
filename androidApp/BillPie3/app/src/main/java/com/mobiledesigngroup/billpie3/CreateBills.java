package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateBills extends Fragment{
    private EditText amount, title;
    private Button btnSubmit, btnCancel;
    BillBaseHelper mDatabase;
    SQLiteDatabase sDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.createbill, container, false);


        title = (EditText) view.findViewById(R.id.titleText);
        amount = (EditText) view.findViewById(R.id.amountText);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        addData();
        return view;
    }
    public void addData(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = title.getText().toString();
                String Amount = amount.getText().toString();
                mDatabase = new BillBaseHelper(getContext());
                sDatabase = mDatabase.getWritableDatabase();
                boolean isInserted = mDatabase.insertData(Title, Amount);
                if (isInserted == true) {
                    Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}

