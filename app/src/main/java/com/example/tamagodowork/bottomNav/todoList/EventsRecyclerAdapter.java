package com.example.tamagodowork.bottomNav.todoList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.widget.Toast;

import com.example.tamagodowork.MainActivity;
import com.example.tamagodowork.R;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Events> eventsList;


    public EventsRecyclerAdapter(Context context, ArrayList<Events> events) {
        this.context = context;
        this.eventsList = events;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText, event, time;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dateText = itemView.findViewById(R.id.eventDate);
            this.event = itemView.findViewById(R.id.eventName);
            this.time = itemView.findViewById(R.id.eventTime);
            this.deleteButton = itemView.findViewById(R.id.delete_icon);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.show_events_row_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Events events = eventsList.get(position);
        String key = events.getKEY();
        holder.event.setText(events.getEVENT());
        holder.dateText.setText(events.getSTARTDATE());
        holder.time.setText(events.getTIME());


        holder.deleteButton.setOnClickListener(v -> {
            eventsList.remove(position);
            notifyDataSetChanged();
            MainActivity.userDoc.collection("Events").document(key).delete()
                    .addOnSuccessListener(e -> Toast.makeText(context, "item Updated", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(context, "Complete Failed", Toast.LENGTH_SHORT).show());

        });
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }



}
