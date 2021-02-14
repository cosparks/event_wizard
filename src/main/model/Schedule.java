package model;

import java.util.*;

// Represents a schedule with arbitrary number of Events
public class Schedule {

    private ArrayList<Event> events;

    // EFFECTS: creates a new empty schedule
    public Schedule() {
        events = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds event to schedule at appropriate chronological position
    public void addEvent(Event event) {
        events.add(event);
        sortEvents();
    }

    // MODIFIES: this
    // EFFECTS: removes an event specified by index
    public void removeEvent(int index) {
        events.remove(events.get(index));
    }

    public Event getEvent(int index) {
        return events.get(index);
    }

    // EFFECTS: returns list of events sorted by importance
    public ArrayList<Event> getEventsByImportance() {
        ArrayList<Event> sortedEvents = new ArrayList<>();
        for (Event e : events) {
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
}
