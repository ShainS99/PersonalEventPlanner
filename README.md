# Personal Event Planner

A simple Android app built for SIT305 Task 4.1.  
This app allows users to create, view, update, and delete personal events such as appointments, trips, and social plans.

## Features

- Add a event with:
  - Title
  - Category
  - Location
  - Date and time
- View all upcoming events in a list
- Edit existing events
- Delete events
- Events are sorted by date
- Input validation for required fields
- Prevents users from selecting past dates for new events
- Saves data locally using Room Database

## App Structure

The app is built using fragments instead of multiple activities.
- **Event List** – shows all saved upcoming events
- **Add Event** – allows the user to create a new event
- **Edit Event** – allows the user to update an existing event

Navigation between fragments is handled using the **Jetpack Navigation Component** and a **Bottom Navigation Bar**.

## How It Works

### Create
Users can enter event details and save them to the local database.

### Read
All saved events are displayed on the dashboard/event list screen and sorted by date.

### Update
Users can select an existing event and edit details such as the title, location, category, or date/time.

### Delete
Users can remove events they no longer want to keep track of.

## Data Persistence

This project uses **Room Database** to store event data locally on the device.  
This means the saved events remain available even after the app is closed or the device is restarted.
