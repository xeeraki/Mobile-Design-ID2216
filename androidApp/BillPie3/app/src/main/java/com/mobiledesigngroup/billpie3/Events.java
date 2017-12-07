package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 2017-11-19.
 */

public class Events extends Fragment{
    private BillBaseHelper mDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<EventList> eventLists;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_event, container, false);
       // ListView listView = (ListView) view.findViewById(R.id.list_view);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CreateEvent.class);
                startActivity(intent);
            }
        });


        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //DatabaseReference myDbRef = FirebaseDatabase.getInstance().getReference();

        eventLists = new ArrayList<>();
        for(int i = 0; i <5; i++){
            EventList eventList = new EventList("School Project", "oct 2017");
                eventLists.add(eventList);
        }
        mAdapter = new EventHolder(eventLists,this.getActivity());
        recyclerView.setAdapter(mAdapter);

       /* mDatabase = new BillBaseHelper(getActivity());
        ArrayList<String> list = new ArrayList<>();
        Cursor data = mDatabase.getBills();

        if (data.getCount() == 0) {
            Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (data.moveToNext()) {
                //list.add(data.getString(0));
                list.add(data.getString(1));
                list.add(data.getString(2));
                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(mAdapter);
            }
        }*/
        return view;
    }
}
