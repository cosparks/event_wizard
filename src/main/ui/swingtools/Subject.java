package ui.swingtools;

import java.util.ArrayList;

public class Subject {
    ArrayList<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void updateObservers(String actionCommand) {
        for (Observer o : observers) {
            o.update(actionCommand);
        }
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
