package ui.tools;

import model.*;
import model.show.*;

import java.util.ArrayList;

// Class containing different display functions for events
public class DisplayTool {

    // EFFECTS: constructs new display tool
    public DisplayTool() {

    }

    // EFFECTS: displays all events in schedule in chronological order (adds line between each displayed event)
    public void displayEventsByDate(Schedule schedule) {
        for (int i = 0; i < schedule.getSize(); i++) {
            Event e = schedule.getEvent(i);
            if (e instanceof Show) {
                displayShow((Show) e);
            } else if (e instanceof SimpleEvent) {
                displaySimpleEvent((SimpleEvent) e);
            }
            System.out.println("  ");
        }
    }

    // EFFECTS: displays all events in schedule ranked in order of importance
    public void displayEventsByImportance(Schedule schedule) {
        for (Event e : schedule.getEventsByImportance()) {
            int importance = e.getImportance() + 1;
            System.out.println(UIColors.QUIT + importance);
            if (e instanceof Show) {
                displayShow((Show) e);
            } else if (e instanceof SimpleEvent) {
                displaySimpleEvent((SimpleEvent) e);
            }
        }
    }

    // EFFECTS: simple title display for events--this is the first line for schedule and editor display
    private void displayNameDateAndTime(Event event) {
        String date = event.getStartDate().getDateForDisplay();
        String time = event.getStartDate().getTimeForDisplay();
        String location = (event.getLocation() == null) ? "" : event.getLocation();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + event.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + location);
    }

    // EFFECTS: displays name, date and time, location, acts, employees and potential revenue of show
    private void displayShow(Show show) {
        String bands = listActs(show);
        String employees = listEmployees(show);

        int revenue = show.calculateRevenue();

        displayNameDateAndTime(show);
        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1 + "Acts:", "Working:", "Expected Revenue:");
        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1
                + bands, employees, UIColors.MENU2 + "$" + revenue);
    }

    // EFFECTS: displays name, date and time, location, acts, employees, potential revenue, bar items, ticket price
    //          and expected ticket sales of show
    public void displayShowForEditor(Show show) {
        int projectedSales = show.getProjectedSales();
        int ticketPrice = show.getTicketPrice();
        int revenue = show.calculateRevenue();

        displayNameDateAndTime(show);
        listActsForEditor(show.getActs());
        System.out.print("\n");
        listEmployeesForEditor(show.getEmployees());
        System.out.print("\n");
        listBarItems(show.getBar());
        System.out.print("\n");
        System.out.println(UIColors.MENU1 + "Ticket price: " + UIColors.MENU2 + "$" + ticketPrice
                + UIColors.MENU1 + "   Projected ticket sales: " + UIColors.MENU2 + projectedSales
                + UIColors.MENU1 + "   Projected profit: " + UIColors.MENU2 + "$" + revenue);
    }

    // EFFECTS: lists bar items on one line for editor display
    private void listBarItems(ArrayList<Drink> bar) {
        boolean first = true;
        System.out.print(UIColors.MENU1 + "bar: ");
        for (Drink d : bar) {
            if (first) {
                barItemForDisplay(d, true);
                first = false;
            } else {
                barItemForDisplay(d, false);
            }
        }
    }

    // EFFECTS: lists acts on one line for editor display
    private void listActsForEditor(ArrayList<Act> acts) {
        boolean first = true;
        System.out.print(UIColors.MENU1 + "acts: ");
        for (Act a : acts) {
            if (first) {
                actForDisplay(a, true);
                first = false;
            } else {
                actForDisplay(a, false);
            }
        }
    }

    // EFFECTS: lists employees on one line for editor display
    private void listEmployeesForEditor(ArrayList<Employee> employees) {
        boolean first = true;
        System.out.print(UIColors.MENU1 + "employees: ");
        for (Employee e : employees) {
            if (first) {
                employeeForDisplay(e, true);
                first = false;
            } else {
                employeeForDisplay(e, false);
            }
        }
    }

    // EFFECTS: prints out act name and pay
    private void actForDisplay(Act a, Boolean first) {
        String insert = (first) ? "" : ", ";
        System.out.print(UIColors.MENU1 + insert + "" + UIColors.MAIN_MENU + a.getName()
                        + UIColors.MENU1 + "  $" + a.getPay() + UIColors.MENU1);
    }

    // EFFECTS: prints out act name and pay
    private void employeeForDisplay(Employee e, Boolean first) {
        String insert = (first) ? "" : ", ";
        String s = "*";
        System.out.print(insert + UIColors.PURPLE + "" + e.getName()
                + " " + UIColors.PURPLE + s + e.getJob() + s + UIColors.MENU1 + " $" + e.getPay());
    }

    // EFFECTS: prints out bar item name, amount, cost and sale price
    private void barItemForDisplay(Drink d, Boolean first) {
        String insert = (first) ? "" : ", ";
        System.out.print(UIColors.MENU1 + insert + UIColors.PURPLE + d.getName()
                + UIColors.MENU2 + " $" + d.getSalePrice()
                +  UIColors.MENU1 + " $" + d.getCost() + " x" + d.getAmount());
    }

    // EFFECTS: displays name, date and time, location, and details of simple event
    private void displaySimpleEvent(SimpleEvent event) {
        displayNameDateAndTime(event);
        for (int i = 0; i < event.getNumberOfDetails(); i++) {
            System.out.println(UIColors.MENU1 + "• " + event.getDetail(i));
        }
    }

    // EFFECTS: displays name, date and time, location, details and importance of simple event
    public void displaySimpleEventForEditor(SimpleEvent event) {
        int importance = event.getImportance() + 1;
        System.out.println(UIColors.MENU1 + "importance  " + UIColors.QUIT + importance);
        displaySimpleEvent(event);
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
            String location = (e.getLocation() == null) ? "" : e.getLocation();
            System.out.printf("%-45.45s %-30.30s %-50.50s\n",
                    UIColors.MENU1 + eventNumber + UIColors.MENU2 + "\t" + e.getName(),
                    e.getStartDate().getDateForDisplay() + "  " + e.getStartDate().getTimeForDisplay(),
                    UIColors.MAIN_MENU + "\t\t" + location);
        }
    }

    // EFFECTS: displays title underlined with TITLE color from ui colors
    public void displayTitle(String title) {
        System.out.println(UIColors.TITLE + "\t\t\t\t\t\t" + title + "\t\t\t\t\t\t");
    }
}
