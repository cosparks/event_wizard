package model;

import java.util.ArrayList;

public class SimpleEvent extends Event {

    private ArrayList<String> details;

    public SimpleEvent(String name) {
        this.name = name;
        importance = 0;
        details = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: add more information to event details
    public void addDetail(String detail) {
        details.add(detail);
    }

    public String getDetail(int index) {
        return details.get(index);
    }

    public int getNumberOfDetails() {
        return details.size();
    }

    // MODIFIES: this
    // EFFECTS: erases all details in details
    public void clearDetails() {
        details.clear();
    }

    // MODIFIES: this
    // EFFECTS: erases one detail from list of details
    public void clearDetail(int index) {
        details.remove(index);
    }
}
