package com.example.personaleventplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>
{
    private List<Event> eventList;
    private EventListFragment fragment;

    public EventRecyclerViewAdapter(List<Event> eventList, EventListFragment fragment) {
        this.eventList = eventList;
        this.fragment = fragment;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.ViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.eventTitleTextView.setText(event.getTitle());
        holder.eventCategoryTextView.setText("Category: " + event.getCategory());
        holder.eventLocationTextView.setText("Location: " + event.getLocation());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedDate = formatter.format(event.getDateTimeMillis());
        holder.eventDateTimeTextView.setText("Date: " + formattedDate);

        holder.editButton.setOnClickListener(v -> fragment.editEvent(event));
        holder.deleteButton.setOnClickListener(v -> fragment.deleteEvent(event));
    }

    @Override
    public int getItemCount() {
        return eventList == null ? 0 : eventList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        TextView eventTitleTextView;
        TextView eventCategoryTextView;
        TextView eventLocationTextView;
        TextView eventDateTimeTextView;
        Button editButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eventTitleTextView = itemView.findViewById(R.id.eventTitleTextView);
            eventCategoryTextView = itemView.findViewById(R.id.eventCategoryTextView);
            eventLocationTextView = itemView.findViewById(R.id.eventLocationTextView);
            eventDateTimeTextView = itemView.findViewById(R.id.eventDateTimeTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
