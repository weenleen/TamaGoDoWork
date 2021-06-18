package com.example.tamagodowork.bottomNav.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamagodowork.R;
import com.google.api.Context;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    private android.content.Context context;
    private ArrayList<Events> eventsList;


    public EventsRecyclerAdapter(android.content.Context context, ArrayList<Events> eventsList) {
        this.context = context;
        this.eventsList = eventsList;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Events events = eventsList.get(position);
        holder.event.setText(events.getEVENT());
        holder.dateText.setText(events.getDATE());
        holder.time.setText(events.getTIME());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText, event, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateText = itemView.findViewById(R.id.eventDate);
            this.event = itemView.findViewById(R.id.eventName);
            this.time = itemView.findViewById(R.id.eventTime);
        }
    }

}
