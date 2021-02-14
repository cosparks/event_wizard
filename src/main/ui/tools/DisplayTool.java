package ui.tools;

import model.*;
import model.show.*;

import java.util.ArrayList;

// Class for different display functions
public class DisplayTool {

    public DisplayTool() {

    }

    public void displayEventsByDate(Schedule schedule) {
        for (int i = 0; i < schedule.getSize(); i++) {
            Event e = schedule.getEvent(i);
            if (e instanceof Show) {
                displayShow((Show) e);
            } else if (e instanceof SimpleEvent) {
                displaySimpleEvent((SimpleEvent) e);
            }
        }
    }

    public void displayEventsByImportance(Schedule schedule) {
        System.out.println(UIColors.MENU1 + "events sorted importance: ");
        for (Event e : schedule.getEventsByImportance()) {
            if (e instanceof Show) {
                displayShow((Show) e);
            } else if (e instanceof SimpleEvent) {
                displaySimpleEvent((SimpleEvent) e);
            }
        }
    }

    private void displayShow(Show show) {
        String date = show.getStartDate().getDateForDisplay();
        String time = show.getStartDate().getTimeForDisplay();
        String bands = listActs(show);
        String employees = listEmployees(show);
        String location = (show.getLocation() == null) ? "" : show.getLocation();

        int revenue = show.calculateRevenue();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + show.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + location);

        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1 + "Acts:", "Working:", "Expected Revenue:");

        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1
                + bands, employees, UIColors.MENU2 + "$" + revenue);
        System.out.println("  ");
    }

    private void displaySimpleEvent(SimpleEvent event) {
        String date = event.getStartDate().getDateForDisplay();
        String time = event.getStartDate().getTimeForDisplay();
        String location = (event.getLocation() == null) ? "" : event.getLocation();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + event.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + location);

        for (int i = 0; i < event.getNumberOfDetails(); i++) {
            System.out.printf("%-30.30s %-50.50s\n", UIColors.MENU1 + "•", event.getDetail(i));
        }
        System.out.println("  ");
    }

    public void displayShowForEditor(Show show) {
        String date = show.getStartDate().getDateForDisplay();
        String time = show.getStartDate().getTimeForDisplay();
        String bands = listActs(show);
        String employees = listEmployees(show);
        String location = (show.getLocation() == null) ? "" : show.getLocation();

        int revenue = show.calculateRevenue();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + show.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + location);

        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1 + "Acts:", "Working:", "Expected Revenue:");

        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1
                + bands, employees, UIColors.MENU2 + "$" + revenue);
    }

    public void displaySimpleEventForEditor(SimpleEvent event) {
        String date = event.getStartDate().getDateForDisplay();
        String time = event.getStartDate().getTimeForDisplay();
        String location = (event.getLocation() == null) ? "" : event.getLocation();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + event.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + location);

        for (int i = 0; i < event.getNumberOfDetails(); i++) {
            System.out.printf("%-50.50s  %-50.50s\n", "", UIColors.MENU1 + event.getDetail(i));
        }
    }

    // EFFECTS: generates string listing the names of all acts at show
    private String listActs(Show show) {
        ArrayList<Act> acts = show.getActs();
        String bands = "";
        for (Act a : acts) {
            if ("".equals(bands)) {
                bands = bands + a.getName();
            } else {
                bands = bands + ", " + a.getName();
            }
        }

        if ("".equals(bands)) {
            bands = "no acts";
        }
        return bands;
    }

    // EFFECTS: generates string listing the names of all employees working show
    private String listEmployees(Show show) {
        ArrayList<Employee> employees = show.getEmployees();
        String names = "";
        for (Employee e : employees) {
            if ("".equals(names)) {
                names = names + e.getName();
            } else {
                names = names + ", " + e.getName();
            }
        }

        if ("".equals(names)) {
            names = "no employees";
        }
        return names;
    }


    // EFFECTS: displays all events with (index + 1), allowing user to select event from schedule
    public void displayEventsForManager(Schedule schedule) {
        displayTitle("Manage events");

        for (int i = 0; i < schedule.getSize(); i++) {
            int eventNumber = i + 1;
            Event e = schedule.getEvent(i);
            System.out.println(UIColors.MENU1 + eventNumber + UIColors.MENU2 + "\t\t" + e.getName() + "\t\t"
                    + e.getStartDate().getDateForDisplay() + "  " + e.getStartDate().getTimeForDisplay()
                    + UIColors.MAIN_MENU + "\t\t" + e.getLocation());
        }
    }

    public void displayTitle(String title) {
        System.out.println(UIColors.TITLE + "\t\t\t\t\t\t" + title + "\t\t\t\t\t\t");
    }
}
