package ui.swingtools;

import java.util.ArrayList;

// represents a Subject to be used in implementing the observer pattern
public class Subject {
    ArrayList<Observer> observers;

    // EFFECTS: constructs a new subject with a list of observers
    public Subject() {
        observers = new ArrayList<>();
    }

    // EFFECTS: iterates though list of observers, calling update on each
    public void updateObservers(String actionCommand) {
        for (Observer o : observers) {
            o.update(actionCommand);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds observer to observers
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // MODIFIES: this
    // EFFECTS: removes observer from observers
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
