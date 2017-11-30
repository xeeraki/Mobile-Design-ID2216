package com.mobiledesigngroup.billpie3;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class CreateEvent extends Fragment{
    private EditText amount, title, date;
    private Button btnSubmit, btnCancel;
    BillBaseHelper mDatabase;
    SQLiteDatabase sDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_event, container, false);


        title = (EditText) view.findViewById(R.id.titleText);
        amount = (EditText) view.findViewById(R.id.amountText);
        date = (EditText) view.findViewById(R.id.dateText);
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
                String Date = date.getText().toString();
                mDatabase = new BillBaseHelper(getContext());
                sDatabase = mDatabase.getWritableDatabase();
                boolean isInserted = mDatabase.insertSpending(Title, Amount, Date);
                if (isInserted == true) {
                    Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}

