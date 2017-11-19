package com.mobiledesigngroup.billpie;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by adam on 2017-11-19.
 */

public class ListBills extends AppCompatActivity {

    Database database;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listbill);


        ListView listView = (ListView) findViewById(R.id.list_view);
        database = new Database(this);


        ArrayList<String> list = new ArrayList<>();
        Cursor data = database.getAllData();

        if(data.getCount() == 0){
            Toast.makeText(ListBills.this,"No data found",Toast.LENGTH_SHORT).show();
        }else {
            while(data.moveToNext()){
                list.add(data.getString(1));
                //list.add(data.getString(2));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
                listView.setAdapter(listAdapter);
            }
        }

    }
}
