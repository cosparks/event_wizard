package model;

import java.util.ArrayList;

import persistence.Writable;
import org.json.JSONObject;
import org.json.JSONArray;

// Represents a simple event with list of additional details
public class SimpleEvent extends Event implements Writable {
    private ArrayList<String> details;

    // EFFECTS: constructs simple event with name--instantiates detail list
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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("type", "simple event");
        json.put("details", detailsToJson());
        return json;
    }

    // EFFECTS: returns list of event details as json array
    private JSONArray detailsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (String d : details) {
            jsonArray.put(d);
        }
        return jsonArray;
    }
}
