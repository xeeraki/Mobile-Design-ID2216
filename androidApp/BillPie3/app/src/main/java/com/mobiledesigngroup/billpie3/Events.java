package com.mobiledesigngroup.billpie3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adam on 2017-11-19.
 */

public class Events extends Fragment {
    private FirebaseDatabase mDatabase;
    private DatabaseReference myDbRef;
    private RecyclerView recyclerView;
    //private FirebaseRecyclerAdapter<EventList, EventViewHolder> mAdapter;
    private List<EventList> eventLists;
    private EventHolder eventHolder;
    private RecyclerView.Adapter sAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_event, container, false);
        // ListView listView = (ListView) view.findViewById(R.id.list_view);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddEvent.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabAddSpending = view.findViewById(R.id.fabAddSpending);
        fabAddSpending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddSpending.class);
                startActivity(intent);
            }
        });


       /* DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("events");
        Query query = mDatabase.orderByKey();*/
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mDatabase = FirebaseDatabase.getInstance();
        myDbRef = mDatabase.getReference("events");



        eventLists = new ArrayList<>();
        for(int i = 0; i <5; i++){
            EventList eventList = new EventList("School Project", "oct 2017");
            eventLists.add(eventList);
        }
        sAdapter = new EventHolder(eventLists,this.getActivity());
        recyclerView.setAdapter(sAdapter);

        return view;
    }
}
/*

    @Override
    public void onStart() {
        super.onStart();

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("events")
                .limitToLast(5);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<EventList> options =
                new FirebaseRecyclerOptions.Builder<EventList>()
                        .setQuery(query, EventList.class)
                        .build();


        FirebaseRecyclerAdapter mAdapter =
                new FirebaseRecyclerAdapter<EventList, EventViewHolder>(options) {

                    @Override
                    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.events, parent, false);
                        return new EventViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(EventViewHolder holder, int position, EventList model) {
                        //TODO implement this method
                        //final DatabaseReference mDbRef=
                       // EventList event = eventLists.get(position);
                        //holder.textViewHeader.setText(event.getHeader());
                        //holder.textViewDate.setText(event.getDate());
                        //holder.textViewRectangle.setText(event.getRect());
                        //holder.imageViewEvent.getDrawable(event.getImage());
                    }
                };
        recyclerView.setAdapter(mAdapter);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHeader;
        public TextView textViewDate;
        View mView;

        //public TextView textViewRectangle;
        //public ImageView imageViewEvent;
        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            textViewHeader = (TextView) mView.findViewById(R.id.textViewHeader);
            textViewDate.setText(title);
        }
            */
/*public void setDate(String date) {
                textViewDate = (TextView) mView.findViewById(R.id.textViewDate);
                textViewDate.setText(date);
            }*//*

        //textViewRectangle = (TextView)itemView.findViewById(R.id.textViewRectangle);
        //imageViewEvent = (ImageView)itemView.findViewById(R.id.imageViewEvent);

*/


