package com.mobiledesigngroup.billpie3;

import android.content.Intent;
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
/**
 * Created by adam on 2017-11-19.
 */
public class CreateBills extends Fragment {
    private static final String TAG = "CreateBills";
    private EditText amount, title;
    private Button btnSubmit, btnCancel;
    private BillBaseHelper mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.createbill, container, false);

        title = (EditText) view.findViewById(R.id.titleText);
        amount = (EditText) view.findViewById(R.id.amountText);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = mDatabase.insertData(title.getText().toString(),
                        amount.getText().toString());
                if (isInserted == true) {
                    Toast.makeText(getContext(), "Data inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Not inserted", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(getContext(), ListBills.class);
                startActivity(intent);
            }
        });
        return view;
    }
}