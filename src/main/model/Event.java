package model;

// Represents an event with date, start/end time and location
public class Event {
    protected String name;
    protected EventDate startDate;
    protected EventDate endDate;
    protected String location;
    protected int importance;

    // EFFECTS: Produce new event with given name
    public Event(String name) {
        this.name = name;
        startDate = new EventDate();
        endDate = new EventDate();
        importance = 0;
    }

    // EFFECTS: Construct a new Event with empty fields
    public Event() {
        startDate = new EventDate();
        endDate = new EventDate();
        importance = 0;
    }

    // MODIFIES: this
    // EFFECTS: sets start date and time according to EventDate parameter; sets endDate for 1 hour later
    public void setStartDate(EventDate date) {
        startDate = date;
        setEndDate(date);
        generateEndTime(date.getHour());
    }

    // MODIFIES: this
    // EFFECTS: sets endDate equal to starting date
    private void setEndDate(EventDate start) {
        endDate.setDay(start.getDay());
        endDate.setMonth(start.getMonth());
        endDate.setYear(start.getYear());
        endDate.setHour(start.getHour());
        endDate.setMinute(start.getMinute());
    }

    // MODIFIES: this
    // EFFECTS: changes the start time of event to new hour/minute and sets endDate to one hour later
    public void changeStartTime(int hour, int minute) {
        startDate.setHour(hour);
        startDate.setMinute(minute);
        setEndDate(startDate);
        generateEndTime(hour);
    }

    // MODIFIES: this
    // EFFECTS: changes the start date of event to new DD/MM/YYYY and end to one hour after start time
    public void changeStartDate(int day, int month, int year) {
        startDate.setDay(day);
        startDate.setMonth(month);
        startDate.setYear(year);
        generateEndTime(startDate.getHour());
    }

    // MODIFIES: this
    // EFFECTS: changes the start time of event to new hour:minute
    public void changeEndTime(int hour, int minute) {
        endDate.setHour(hour);
        endDate.setMinute(minute);
    }

    // MODIFIES: this
    // EFFECTS: changes the end date of event to new DD/MM/YYYY
    public void changeEndDate(int day, int month, int year) {
        endDate.setDay(day);
        endDate.setMonth(month);
        endDate.setYear(year);
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

    public EventDate getEndDate() {
        return endDate;
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

    // MODIFIES: this
    // EFFECTS: given a starting hour, sets the end time to one hour later, changing days if necessary
    private void generateEndTime(int hour) {
        if (hour == 23) {
            endDate.setHour(0);
            int newDay = startDate.getDay() + 1;
            endDate.setDay(newDay);
        } else {
            int newHour = startDate.getHour() + 1;
            endDate.setHour(newHour);
        }
    }

    // EFFECTS: compares date of this and event parameter for Java sort method
    public int compareDates(Event eventA) {
        return startDate.getDateAsString().compareTo(eventA.startDate.getDateAsString());
    }

    // EFFECTS: compares importance int of this and event parameter for Java sort method
    public int compareImportance(Event eventA) {
        return Integer.toString(eventA.getImportance()).compareTo(Integer.toString(this.importance));
    }
}
