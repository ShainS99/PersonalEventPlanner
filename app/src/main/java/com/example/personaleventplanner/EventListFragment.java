package com.example.personaleventplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventListFragment extends Fragment {

    private EventDao eventDao;
    private RecyclerView eventsRecyclerView;
    private TextView noEventsTextView;
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
        noEventsTextView = view.findViewById(R.id.noEventsTextView);

        eventRecyclerViewAdapter = new EventRecyclerViewAdapter(eventList, this);
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

        // Create an executor service
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Run the operation in the background
        executorService.execute(() -> {
            eventList = eventDao.getAll();

            requireActivity().runOnUiThread(() -> {
                eventRecyclerViewAdapter.setEventList(eventList);

                if (eventList.isEmpty()) {
                    noEventsTextView.setVisibility(View.VISIBLE);
                } else {
                    noEventsTextView.setVisibility(View.GONE);
                }
            });
        });
    }

    public void deleteEvent(Event event) {
        // Create an executor service
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Run the operation in the background
        executorService.execute(() -> {
            eventDao.delete(event);

            // Causes more crashes if i dont run on ui thread
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                loadEvents();
            });
        });
    }

    public void editEvent(Event event) {
        Bundle bundle = new Bundle();
        bundle.putInt("eventId", event.getId());
        bundle.putString("title", event.getTitle());
        bundle.putString("category", event.getCategory());
        bundle.putString("location", event.getLocation());
        bundle.putLong("dateTimeMillis", event.getDateTimeMillis());

        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(R.id.editEventFragment, bundle);
    }
}