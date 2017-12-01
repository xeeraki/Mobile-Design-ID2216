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
 * Created by cassius on 28/11/17.
 */

public class CheckOutPage extends Fragment {

    private BillBaseHelper mDatabase;
    private int userId, eventId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkout_page, container, false);
        ListView listView = (ListView) view.findViewById(R.id.EventPageListView);

        mDatabase = new BillBaseHelper(getActivity());
        ArrayList<String> list = new ArrayList<>();
        Cursor data = mDatabase.getDueByUserIdEventId(userId, eventId);

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(3));
                list.add(data.getString(4));
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
                listView.setAdapter(mAdapter);
            }
        }
        return view;
    }
}