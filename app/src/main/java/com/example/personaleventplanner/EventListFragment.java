package com.example.personaleventplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends Fragment {

    private EventDao eventDao;
    private RecyclerView eventsRecyclerView;
    private EventRecyclerViewAdapter eventRecyclerViewAdapter;
    List<Event> eventList = new ArrayList<>();

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventDao = EventDatabase.getDatabase(requireContext()).eventDao();

        eventsRecyclerView = view.findViewById(R.id.eventListRecyclerView);

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(eventList);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        eventsRecyclerView.setAdapter(eventRecyclerViewAdapter);

        loadEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }

    private void loadEvents() {
        eventList = eventDao.getAll();
        eventRecyclerViewAdapter.setEventList(eventList);
    }
}