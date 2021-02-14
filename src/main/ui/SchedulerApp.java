package ui;

import model.*;
import model.show.*;
import ui.tools.*;

import java.util.ArrayList;
import java.util.Scanner;

public class SchedulerApp {
    private final Schedule schedule;
    private Scanner input;
    private boolean displayMainMenu;



    public SchedulerApp() {
        schedule = new Schedule();
        displayMainMenu = true;
        runStartMenu();
    }

    private void runStartMenu() {
        boolean runProgram = true;
        String command;

        initialize();

        System.out.println(UIColors.TITLE + "\t\t\t\t\t\teManager\t\t\t\t\t\t");

        while (runProgram) {
            if (displayMainMenu) {
                displayMenu();
                displayMainMenu = false;
            }

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                runProgram = false;
            } else {
                clearScreen();
                processCommand(command);
            }
        }
        System.out.println("\t\t\t\t\t\tGoodbye");
    }

    private void initialize() {
        input = new Scanner(System.in);
    }

    private void displayMenu() {
        System.out.println(UIColors.MENU1 + "\tMain Menu:");
        System.out.println(UIColors.MENU2 + "\t[c]reate a new event \t[m]anage existing events");
        System.out.println(UIColors.MENU2 + "\t[d]isplay events" + UIColors.QUIT + "    \t[q]uit");
    }

    private void processCommand(String command) {
        if (command.equals("c")) {
            createEvent();
            displayMainMenu = true;
        } else if (command.equals("m")) {
            manageEvents();
        } else if (command.equals("d")) {
            runDisplayOptions();
            displayMainMenu = true;
        } else {
            System.out.println(UIColors.QUIT + "\t\terror: please enter a valid selection");
        }
    }

    private void createEvent() {
        String command;
        while (true) {
            displayCreateEventMenu();

            command = input.next();
            command = command.toLowerCase();
            if (command.equals("s")) {
                schedule.addEvent(createSimpleEvent());
                break;
            } else if (command.equals("a")) {
                schedule.addEvent(createShow());
                break;
            } else if (command.equals("m")) {
                break;
            } else {
                System.out.println(UIColors.QUIT + "\t\terror: please enter a valid selection");
            }
        }
    }

    private SimpleEvent createSimpleEvent() {
        SimpleEventBuilder seBuilder = new SimpleEventBuilder();
        return seBuilder.buildSimpleEvent();
    }

    private Show createShow() {
        ShowBuilder showBuilder = new ShowBuilder();
        return showBuilder.buildShow();
    }

    private void displayCreateEventMenu() {
        System.out.println(UIColors.MENU1 + "\tCreate Event:");
        System.out.println(UIColors.MENU2 + "\t[s]imple \t\t\t\t[a]dvanced"
                + UIColors.MAIN_MENU + "\t\t\t\t\t\treturn to [m]ain");
    }

    private void runDisplayOptions() {
        runDisplayOptionsMenu();
    }

    private void runDisplayOptionsMenu() {
        String command;

        while (true) {
            System.out.println(UIColors.MENU1 + "\tView Events listed by:");
            System.out.println(UIColors.MENU2 + "\t[d]ate \t\t\t[i]mportance"
                    + UIColors.MAIN_MENU + "\t\treturn to [m]ain");

            command = input.next();
            command = command.toLowerCase();

            if (command.equals("d")) {
                displayEventsByDate();
                break;
            } else if (command.equals("i")) {
                displayEventsByImportance();
                break;
            } else if (command.equals("m")) {
                break;
            } else {
                System.out.println("I didn't understand your request.  Please try again:");
            }
        }
    }

    private void displayEventsByDate() {
        for (int i = 0; i < schedule.getSize(); i++) {
            Event e = schedule.getEvent(i);
            if (e instanceof Show) {
                displayShow((Show) e);
            } else if (e instanceof SimpleEvent) {
                displaySimpleEvent((SimpleEvent) e);
            }
        }
    }

    private void displayEventsByImportance() {
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
        int revenue = show.calculateRevenue();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + show.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + show.getLocation());

        System.out.printf("%-50.50s  %-30.30s %-30ss\n", UIColors.MENU1 + "Acts:", "Working:", "Expected Revenue:");

        System.out.printf("%-50.50s  %-30.30s %-30s\n", UIColors.MENU1
                + bands, employees, UIColors.MENU2 + "$" + revenue);
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
            names = "";
        }
        return names;
    }

    private void displaySimpleEvent(SimpleEvent event) {
        String date = event.getStartDate().getDateForDisplay();
        String time = event.getStartDate().getTimeForDisplay();

        System.out.printf("%-50.50s  %-40.40s %-30s\n", UIColors.PURPLE + event.getName(), UIColors.MENU2
                + date + "  " + time, UIColors.MAIN_MENU + event.getLocation());

        for (int i = 0; i < event.getNumberOfDetails(); i++) {
            System.out.printf("%-50.50s  %-50.50s\n", "", UIColors.MENU1 + event.getDetail(i));
        }
    }

    private void manageEvents() {
        if (schedule.getSize() == 0) {
            System.out.println(UIColors.QUIT + "you don't have any events. please enter another request:");
        } else {
            runEventManager();
            displayMainMenu = true;
        }
    }

    private void runEventManager() {
        int selection;
        int size = schedule.getSize();

        displayEventsForManager();
        System.out.println(UIColors.MENU1 + "enter the number of the event you'd like to edit:");

        while (true) {
            initialize();
            try {
                selection = input.nextInt();
            } catch (Exception e) {
                selection = -1;
            }

            if (selection > 0 && selection <= size) {
                manageEvent(selection);
                break;
            } else {
                System.out.println(UIColors.QUIT + "error: please enter a valid number [1, " + size + "]");
            }
        }
    }

    private void manageEvent(int selection) {
        int index = selection - 1;

        if (schedule.getEvent(index) instanceof Show) {
            Show show = (Show) schedule.getEvent(index);
            editShow(show);
        } else if (schedule.getEvent(index) instanceof SimpleEvent) {
            SimpleEvent simpleEvent = (SimpleEvent) schedule.getEvent(index);
            editSimpleEvent(simpleEvent);
        }
    }

    private void editShow(Show show) {
        new ShowEditor(show);
    }

    private void editSimpleEvent(Event event) {
    }

    // EFFECTS: displays all events with (index + 1), allowing user to select event from schedule
    private void displayEventsForManager() {
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

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
