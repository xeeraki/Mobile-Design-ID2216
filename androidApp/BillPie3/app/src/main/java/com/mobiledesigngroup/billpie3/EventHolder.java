package com.mobiledesigngroup.billpie3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adam on 2017-12-06.
 */

public class EventHolder extends RecyclerView.Adapter<EventHolder.ViewHolder>{
    private List<EventList> eventLists;
    private Context context;

    public EventHolder(List<EventList> eventLists, Context context) {
        this.eventLists = eventLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.events,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EventList event = eventLists.get(position);
        holder.textViewHeader.setText(event.getHeader());
        holder.textViewDate.setText(event.getDate());
        //holder.textViewRectangle.setText(event.getRect());
        //holder.imageViewEvent.getDrawable(event.getImage());

    }

    @Override
    public int getItemCount() {
        return eventLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHeader;
        public TextView textViewDate;
        //public TextView textViewRectangle;
        //public ImageView imageViewEvent;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewHeader = (TextView)itemView.findViewById(R.id.textViewHeader);
            textViewDate = (TextView)itemView.findViewById(R.id.textViewDate);
            //textViewRectangle = (TextView)itemView.findViewById(R.id.textViewRectangle);
            //imageViewEvent = (ImageView)itemView.findViewById(R.id.imageViewEvent);
        }
    }
}
