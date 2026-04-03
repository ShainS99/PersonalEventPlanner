package com.example.personaleventplanner;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {
    private static volatile EventDatabase INSTANCE;
    public abstract EventDao eventDao();

    public static EventDatabase getDatabase(Context context) {
        if (INSTANCE == null)
        {
            synchronized (EventDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            EventDatabase.class,
                            "event_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }


}