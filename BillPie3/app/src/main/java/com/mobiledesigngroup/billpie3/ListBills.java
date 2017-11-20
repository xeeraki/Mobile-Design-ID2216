package com.mobiledesigngroup.billpie3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by adam on 2017-11-19.
 */

public class ListBills extends Fragment {

    private BillBaseHelper mDatabase;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.listbills, container, false);


        ListView listView = (ListView) view.findViewById(R.id.list_view);


        mDatabase = new BillBaseHelper(getActivity());
        ArrayList<String> list = new ArrayList<>();
        Cursor data = mDatabase.getAllData();

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(0));
                list.add(data.getString(1));
                list.add(data.getString(2));
                ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(listAdapter);
            }
        }


                return view;

    }
}
