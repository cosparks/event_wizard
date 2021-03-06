package ui;

import model.*;
import persistence.*;
import ui.tools.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Represents the main user interface for creating and managing events
public class SchedulerApp {
    private static final String JSON_STORE = "./data/mySchedule.json";

    private Schedule schedule;
    private DisplayTool displayTool;
    private Scanner input;
    private boolean displayMainMenu;
    private boolean newChangesSaved;

    // EFFECTS: creates new scheduler app--runs when main is called
    public SchedulerApp() {
        schedule = new Schedule();
        displayTool = new DisplayTool();
        displayMainMenu = true;
        newChangesSaved = true;
        runStartMenu();
    }

    // EFFECTS: displays start menu and processes user input
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

            if (command.equals("q") && newChangesSaved) {
                runProgram = false;
            } else if (command.equals("q") && !newChangesSaved) {
                promptSaveChanges();
                runProgram = false;
            } else {
                // clearScreen();
                processCommand(command);
            }
        }
        System.out.println(UIColors.MAIN_MENU + "\t\t\t\t\t\tGoodbye");
    }

    // MODIFIES: this
    // EFFECTS: initializes scanner
    private void initialize() {
        input = new Scanner(System.in);
    }

    private void displayMenu() {
        System.out.println(UIColors.MENU1 + "\tMain Menu:");
        System.out.println(UIColors.MENU2 + "\t[c]reate new event \t\t\t[m]anage events");
        System.out.println(UIColors.MENU2 + "\t[d]isplay events" + UIColors.MAIN_MENU + "\t\t\t[s]ave"
                + UIColors.MENU1 + " | " + UIColors.MAIN_MENU + "[l]oad");
        System.out.println(UIColors.QUIT + "\t[q]uit");
    }

    // EFFECTS: selects appropriate method to call given user input
    private void processCommand(String command) {
        if (command.equals("c")) {
            createEvent();
            displayMainMenu = true;
        } else if (command.equals("m")) {
            checkEventsForManager();
        } else if (command.equals("d")) {
            checkEventsForDisplay();
        } else if (command.equals("s")) {
            saveSchedule();
        } else if (command.equals("l")) {
            loadSchedule();
        } else {
            System.out.println(UIColors.QUIT + "\t\terror: please enter a valid selection");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to select which type of event to create and adds event to schedule
    private void createEvent() {
        String command;
        while (true) {
            displayCreateEventMenu();

            command = input.next();
            command = command.toLowerCase();
            if (command.equals("s")) {
                schedule.addEvent(createSimpleEvent());
                newChangesSaved = false;
                break;
            } else if (command.equals("a")) {
                schedule.addEvent(createShow());
                newChangesSaved = false;
                break;
            } else if (command.equals("m")) {
                break;
            } else {
                System.out.println(UIColors.QUIT + "\t\terror: please enter a valid selection");
            }
        }
    }

    // EFFECTS: calls constructor and returns simple event
    private SimpleEvent createSimpleEvent() {
        SimpleEventBuilder seBuilder = new SimpleEventBuilder("event");
        return seBuilder.buildSimpleEvent();
    }

    // EFFECTS: calls constructor and returns show
    private Show createShow() {
        ShowBuilder showBuilder = new ShowBuilder("show");
        return showBuilder.buildShow();
    }

    // EFFECTS: prompts user to choose which type of event they'd like to create
    private void displayCreateEventMenu() {
        System.out.println(UIColors.MENU1 + "\tCreate Event:");
        System.out.println(UIColors.MENU2 + "\t[s]imple \t\t\t\t[a]dvanced"
                + UIColors.MAIN_MENU + "\t\t\t\t\t\treturn to [m]ain");
    }

    // EFFECTS: checks whether or not a user has any events to display
    private void checkEventsForDisplay() {
        if (schedule.getSize() == 0) {
            System.out.println(UIColors.QUIT + "you don't have any events. please enter another request:");
        } else {
            runDisplayOptionsMenu();
            displayMainMenu = true;
        }
    }

    // EFFECTS: prompts user to select type of display, and displays events according to selection
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

    // EFFECTS: verifies that the user has events to manage--calls manager if schedule contains events
    private void checkEventsForManager() {
        if (schedule.getSize() == 0) {
            System.out.println(UIColors.QUIT + "you don't have any events. please enter another request:");
        } else {
            runEventManager();
            newChangesSaved = false;
            displayMainMenu = true;
        }
    }

    // EFFECTS: itemizes events and prompts user to select which event to edit
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

    // MODIFIES: this
    // EFFECTS: selects event by index and calls event editor--sorts event by date when complete
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

    // EFFECTS: prompts user to save before quitting program
    private void promptSaveChanges() {
        String command;
        System.out.println("Would you like to save your changes before you quit?  y/n");
        command = input.next();
        command = command.toLowerCase();
        while (true) {
            if (command.equals("y")) {
                saveSchedule();
                break;
            } else if (command.equals("n")) {
                break;
            } else {
                System.out.println(UIColors.MENU1 + "Invalid command. Please enter valid input");
            }
        }
    }

    // EFFECTS: writes file to json object and stores in destination folder
    private void saveSchedule() {
        JsonWriter writer = new JsonWriter(JSON_STORE);
        try {
            writer.open();
            writer.write(schedule);
            writer.close();
            System.out.println(UIColors.MENU1 + "saved mySchedule to " + JSON_STORE);
            newChangesSaved = true;
        } catch (FileNotFoundException e) {
            System.out.println(UIColors.QUIT + "Error: bad pathway, destination folder not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: retrieves and parses json object from JSON_STORE
    private void loadSchedule() {
        JsonReader reader = new JsonReader(JSON_STORE);
        try {
            schedule = reader.read();
            System.out.println(UIColors.MENU1 + "mySchedule successfully loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println(UIColors.QUIT + "Error: source file not found.  Unable to retrieve source");
        }
    }

    // MODIFIES: show
    // EFFECTS: calls constructor for show editor
    private void editShow(Show show) {
        new ShowEditor(show);
    }

    // MODIFIES: event
    // EFFECTS: calls constructor for simple event editor
    private void editSimpleEvent(SimpleEvent event) {
        new SimpleEventEditor(event);
    }

//    // EFFECTS: supposed to clear console screen--but not very useful at the moment
//    public static void clearScreen() {
//        System.out.print("\033[H\033[2J");
//        System.out.flush();
//    }
}
