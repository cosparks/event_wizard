package ui;

import model.*;
import ui.tools.*;

import java.util.Scanner;

public class SchedulerApp {
    private final Schedule schedule;
    private DisplayTool displayTool;
    private Scanner input;
    private boolean displayMainMenu;



    public SchedulerApp() {
        schedule = new Schedule();
        displayTool = new DisplayTool();
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
        SimpleEventBuilder seBuilder = new SimpleEventBuilder("event");
        return seBuilder.buildSimpleEvent();
    }

    private Show createShow() {
        ShowBuilder showBuilder = new ShowBuilder("show");
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
                displayTool.displayEventsByDate(schedule);
                break;
            } else if (command.equals("i")) {
                displayTool.displayEventsByImportance(schedule);
                break;
            } else if (command.equals("m")) {
                break;
            } else {
                System.out.println("I didn't understand your request.  Please try again:");
            }
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

        displayTool.displayEventsForManager(schedule);
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
        schedule.sortEvents();
    }

    private void editShow(Show show) {
        new ShowEditor(show);
    }

    private void editSimpleEvent(SimpleEvent event) {
        new SimpleEventEditor(event);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
