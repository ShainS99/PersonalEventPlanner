package com.example.personaleventplanner;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "date_time")
    private long dateTimeMillis;

    public Event(String title, String category, String location, long dateTimeMillis)
    {
        this.title = title;
        this.category = category;
        this.location = location;
        this.dateTimeMillis = dateTimeMillis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDateTimeMillis() {
        return dateTimeMillis;
    }

    public void setDateTimeMillis(long dateTimeMillis) {
        this.dateTimeMillis = dateTimeMillis;
    }
}
