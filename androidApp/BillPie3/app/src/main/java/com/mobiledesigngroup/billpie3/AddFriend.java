package com.mobiledesigngroup.billpie3;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by adam on 2017-11-21.
 */

public class AddFriend extends Fragment {
    private EditText name, phone;
    private Button btnSubmit, btnCancel;
    BillBaseHelper mDatabase;
    SQLiteDatabase sDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addfriend, container, false);


        name = (EditText) view.findViewById(R.id.nameText);
        phone = (EditText) view.findViewById(R.id.phoneText);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmitFriend);
        btnCancel = (Button) view.findViewById(R.id.btnCancelFriend);
        addFriend();
        return view;
    }
    public void addFriend(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Phone = phone.getText().toString();
                mDatabase = new BillBaseHelper(getContext());
                sDatabase = mDatabase.getWritableDatabase();
                boolean isInserted = mDatabase.insertFriend(Name, Phone);
                if (isInserted == true) {
                    Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
