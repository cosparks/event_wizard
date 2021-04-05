package model;

import exceptions.DateFormatException;
import exceptions.InvalidNameException;
import persistence.*;
import org.json.JSONObject;
import ui.texttools.TextColors;

import java.util.Objects;


// abstract representation of an event with date, start/end time and location
public abstract class ScheduleEvent implements Writable {
    protected String name;
    protected EventDate startDate;
    protected String location;
    protected int importance;
    // protected EventDate endDate;

    // EFFECTS: sets name and startDate, and initializes importance and location
    public ScheduleEvent(String name) {
        this.name = name;
        startDate = new EventDate();
        importance = 0;
        location = "";
    }

    // MODIFIES: this
    // EFFECTS: sets start date and time according to EventDate parameter; sets endDate for 1 hour later
    public void setStartDate(EventDate date) {
        startDate = date;
    }

    // MODIFIES: this
    // EFFECTS: changes the start time of event to new hour/minute and sets endDate to one hour later
    public void changeStartTime(int hour, int minute) throws DateFormatException {
        startDate.setHour(hour);
        startDate.setMinute(minute);

    }

    // MODIFIES: this
    // EFFECTS: changes the start date of event to new DD/MM/YYYY and end to one hour after start time
    public void changeStartDate(int day, int month, int year) throws DateFormatException {
        startDate.setDay(day);
        startDate.setMonth(month);
        startDate.setYear(year);

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("importance", importance);
        json.put("location", location);
        json.put("start date", startDate.toJson());
        return json;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventDate getStartDate() {
        return startDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    // EFFECTS: compares date of this and event parameter for Java sort method
    public int compareDates(ScheduleEvent eventA) {
        return startDate.getDateAsString().compareTo(eventA.startDate.getDateAsString());
    }

    // EFFECTS: compares importance int of this and event parameter for Java sort method
    public int compareImportance(ScheduleEvent eventA) {
        return Integer.toString(eventA.getImportance()).compareTo(Integer.toString(this.importance));
    }

//    // Code GraveYard for endDate methods (saving in case program will incorporate end dates)
//
//    // EFFECTS: Produce new event with given name
//    public Event(String name) {
//        this.name = name;
//        startDate = new EventDate();
//        // endDate = new EventDate();
//        importance = 0;
//    }

//    // MODIFIES: this
//    // EFFECTS: sets endDate equal to starting date
//    private void setEndDate(EventDate start) {
//        endDate.setDay(start.getDay());
//        endDate.setMonth(start.getMonth());
//        endDate.setYear(start.getYear());
//        endDate.setHour(start.getHour());
//        endDate.setMinute(start.getMinute());
//    }

//    // MODIFIES: this
//    // EFFECTS: changes the start time of event to new hour:minute
//    public void changeEndTime(int hour, int minute) {
//        endDate.setHour(hour);
//        endDate.setMinute(minute);
//    }

//    // MODIFIES: this
//    // EFFECTS: changes the end date of event to new DD/MM/YYYY
//    public void changeEndDate(int day, int month, int year) {
//        endDate.setDay(day);
//        endDate.setMonth(month);
//        endDate.setYear(year);
//    }

//    public EventDate getEndDate() {
//        return endDate;
//    }

//    // MODIFIES: this
//    // EFFECTS: given a starting hour, sets the end time to one hour later, changing days if necessary
//    private void generateEndTime(int hour) {
//        if (hour == 23) {
//            endDate.setHour(0);
//            int newDay = startDate.getDay() + 1;
//            endDate.setDay(newDay);
//        } else {
//            int newHour = startDate.getHour() + 1;
//            endDate.setHour(newHour);
//        }
//    }
}
