package com.mobiledesigngroup.billpie3;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by adam on 2017-11-19.
 */

public class Events extends Fragment{
    private BillBaseHelper mDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);

        mDatabase = new BillBaseHelper(getActivity());
        ArrayList<String> list = new ArrayList<>();
        Cursor data = mDatabase.getBills();

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                //list.add(data.getString(0));
                list.add(data.getString(1));
                list.add(data.getString(2));
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(mAdapter);
            }
        }
        return view;
    }
}
