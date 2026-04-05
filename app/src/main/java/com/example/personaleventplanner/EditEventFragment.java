package com.example.personaleventplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditEventFragment extends Fragment {

    private EventDao eventDao;

    private EditText titleEditText;
    private Spinner categorySpinner;
    private EditText locationEditText;
    private TextView selectedDateTimeTextView;

    private Button selectDateButton;
    private Button selectTimeButton;
    private Button updateEventButton;

    private Calendar selectedDateTime;
    private boolean dateChosen = false;
    private boolean timeChosen = false;

    private int eventId;

    public EditEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eventDao = EventDatabase.getDatabase(requireContext()).eventDao();

        titleEditText = view.findViewById(R.id.titleEditText);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        locationEditText = view.findViewById(R.id.locationEditText);
        selectedDateTimeTextView = view.findViewById(R.id.selectedDateTimeTextView);
        selectDateButton = view.findViewById(R.id.selectDateButton);
        selectTimeButton = view.findViewById(R.id.selectTimeButton);
        updateEventButton = view.findViewById(R.id.updateEventButton);

        selectedDateTime = Calendar.getInstance();

        setupCategorySpinner();
        loadEventData();
        setupListeners();
    }

    private void setupCategorySpinner() {
        String[] categories = {"Work", "Social", "Travel", "Personal", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void loadEventData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            eventId = bundle.getInt("eventId");
            String title = bundle.getString("title");
            String category = bundle.getString("category");
            String location = bundle.getString("location");
            long dateTimeMillis = bundle.getLong("dateTimeMillis");

            titleEditText.setText(title);
            locationEditText.setText(location);

            String[] categories = {"Work", "Social", "Travel", "Personal", "Other"};
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(category)) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }

            selectedDateTime.setTimeInMillis(dateTimeMillis);
            dateChosen = true;
            timeChosen = true;
            updateDateTimeText();
        }
    }

    private void setupListeners() {
        selectDateButton.setOnClickListener(v -> showDatePicker());
        selectTimeButton.setOnClickListener(v -> showTimePicker());
        updateEventButton.setOnClickListener(v -> updateEvent());
    }

    private void showDatePicker() {
        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDateTime.set(Calendar.YEAR, selectedYear);
                    selectedDateTime.set(Calendar.MONTH, selectedMonth);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, selectedDay);
                    dateChosen = true;
                    updateDateTimeText();
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker() {
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, selectedHour, selectedMinute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                    selectedDateTime.set(Calendar.MINUTE, selectedMinute);
                    timeChosen = true;
                    updateDateTimeText();
                },
                hour,
                minute,
                true
        );

        timePickerDialog.show();
    }

    private void updateDateTimeText() {
        if (dateChosen && timeChosen)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            selectedDateTimeTextView.setText(formatter.format(selectedDateTime.getTime()));
        }
        else if (dateChosen)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDateTimeTextView.setText(formatter.format(selectedDateTime.getTime()));
        }
        else if (timeChosen)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
            selectedDateTimeTextView.setText(formatter.format(selectedDateTime.getTime()));
        }
        else
        {
            selectedDateTimeTextView.setText("No date/time selected");
        }
    }

    private void updateEvent() {
        String title = titleEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString();

        if (title.isEmpty())
        {
            Toast.makeText(requireContext(), "Please enter a title!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dateChosen || !timeChosen)
        {
            Toast.makeText(requireContext(), "Please select a date and time!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDateTime.getTimeInMillis() < System.currentTimeMillis()) {
            Toast.makeText(requireContext(), "Date cannot be in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(title, category, location, selectedDateTime.getTimeInMillis());
        event.setId(eventId);

        // Create an executor service
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Run the operation on the thread
        executorService.execute(() -> {
            eventDao.update(event);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Event updated", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(EditEventFragment.this).navigateUp();
            });
        });
    }
}