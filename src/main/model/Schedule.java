package model;

import persistence.*;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.*;

// Represents a schedule with arbitrary number of Events
public class Schedule implements Writable {
    private ArrayList<ScheduleEvent> events;
    private String name;

    // EFFECTS: creates a new empty schedule
    public Schedule() {
        events = new ArrayList<>();
        name = "My Schedule";
    }

    public Schedule(String name) {
        events = new ArrayList<>();
        this.name = name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("events", eventsToJson());
        return json;
    }

    // EFFECTS: returns events in schedule as JSONArray
    private JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (ScheduleEvent e : events) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: adds event to schedule at appropriate chronological position
    public void addEvent(ScheduleEvent event) {
        events.add(event);
        sortEvents();
    }

    // MODIFIES: this
    // EFFECTS: removes an event specified by index
    public void removeEvent(int index) {
        events.remove(events.get(index));
    }

    public ScheduleEvent getEvent(int index) {
        return events.get(index);
    }

    // EFFECTS: returns list of events sorted by importance
    public ArrayList<ScheduleEvent> getEventsByImportance() {
        ArrayList<ScheduleEvent> sortedEvents = new ArrayList<>();
        for (ScheduleEvent e : events) {
            sortedEvents.add(e);
        }
        sortedEvents.sort((e1, e2) -> e1.compareImportance(e2));
        return sortedEvents;
    }

    // MODIFIES: this
    // EFFECTS: sort events by date
    public void sortEvents() {
        events.sort((e1, e2) -> e1.compareDates(e2));
    }

    public int getSize() {
        return events.size();
    }

    public String getName() {
        return name;
    }
}
