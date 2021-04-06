package ui.texttools;

import java.util.Scanner;

import exceptions.DateFormatException;
import model.EventDate;
import model.ScheduleEvent;

// Abstract class for constructing events
public abstract class EventBuilder {
    protected Scanner input;
    protected String name;
    protected EventDate eventDate;
    protected Boolean displayMain;

    // EFFECTS: creates event builder--only to be called when editing existing events
    public EventBuilder() {
        initialize();
        displayMain = true;
    }

    // EFFECTS: creates event builder and sets new name for event; dummy string is used to specify
    //          that this constructor should run when building a new event (not editing an existing one)
    public EventBuilder(String str) {
        initialize();
        promptForName();
        displayMain = true;
    }

    // MODIFIES: this (name field is assigned to Event in subclasses)
    // EFFECTS: initializes input, prompts user to input event name and assigns new name to event
    protected void promptForName() {
        String command;

        System.out.println(TextColors.MENU1 + "enter event name: ");
        while (true) {
            command = input.nextLine();

            if (command.equals("")) {
                throwInvalidInput("name");
            } else {
                break;
            }
        }
        name = command;
    }

    // MODIFIES: this (name field is assigned to Event in subclasses)
    // EFFECTS: initializes input, prompts user to input event name and assigns new name to event
    protected void promptForName(ScheduleEvent event) {
        String command;
        initialize();

        System.out.println(TextColors.MENU1 + "enter event name: ");
        while (true) {
            command = input.nextLine();

            if (command.equals("")) {
                throwInvalidInput("name");
            } else {
                break;
            }
        }
        name = command;
        event.setName(name);
        displayMain = true;
    }

    // MODIFIES: event (show or simpleEvent)
    // EFFECTS: prompts user to enter date and time, then sets date and time of event
    protected void setDate(ScheduleEvent event) {
        String date = datePrompt();
        String time = timePrompt();
        int day = Integer.parseInt(date.substring(0,2));
        int month = Integer.parseInt(date.substring(2,4));
        int year = Integer.parseInt(date.substring(4));
        int hour = Integer.parseInt(time.substring(0,2));
        int minute = Integer.parseInt(time.substring(2));
        try {
            eventDate = new EventDate(day, month, year, hour, minute);
        } catch (DateFormatException dfe) {
            System.out.println(TextColors.QUIT + "Please enter valid date");
        }

        String dateDisplay = eventDate.getDateForDisplay();
        String timeDisplay = eventDate.getTimeForDisplay();

        event.setStartDate(eventDate);
        System.out.println("Date has been set to " + TextColors.PURPLE + dateDisplay + "  " + timeDisplay);
        displayMain = true;
    }

    // EFFECTS: returns date as string after checking to make sure it is a valid input
    private String datePrompt() {
        String day = getTimeValue("day [1, 31]", 1, 31);
        String month = getTimeValue("month [1, 12]", 1, 12);
        String year = getTimeValue("year", 0, 9999);

        return day + month + year;
    }

    // EFFECTS: returns time as string after checking to make sure it is a valid input
    private String timePrompt() {
        String time = "";
        String hour = getTimeValue("hour [0, 23]", 0, 23);
        String minute = getTimeValue("minute [0, 59]", 0, 59);

        time = hour + minute;
        return time;
    }

    // REQUIRES: string is unit of time; min/max are lower and upper thresholds for that unit of time
    // EFFECTS: returns date value (month/day/hour etc.) as string after checking that user input is valid
    private String getTimeValue(String type, int min, int max) {
        int dateValue = 0;
        boolean invalid = true;

        while (invalid) {
            initialize();
            System.out.println(TextColors.MENU1 + "enter event " + type + ": ");
            try {
                dateValue = input.nextInt();
            } catch (Exception e) {
                dateValue = min - 1;
            }
            if (dateValue < min || dateValue > max) {
                System.out.println(TextColors.QUIT + "invalid entry");
            } else {
                invalid = false;
            }
        }
        String insert = (dateValue < 10) ? "0" : "";

        return insert + dateValue;
    }

    // MODIFIES: event
    // EFFECTS: allows user to set location for event
    protected void promptForLocation(ScheduleEvent event) {
        String command;
        initialize();

        System.out.println(TextColors.MENU1 + "enter location: ");
        command = input.nextLine();
        event.setLocation(command);
        displayMain = true;
    }

    // MODIFIES: event
    // EFFECTS: allows user to set importance for event
    protected void setImportance(ScheduleEvent event) {
        int importance = promptForInteger("enter a value for importance [1,10] ", "entry",1, 10);
        importance--;
        event.setImportance(importance);
    }

    // EFFECTS: initializes scanner for this object
    protected void initialize() {
        input = new Scanner(System.in);
    }

    // REQUIRES: prompt is a statement giving directions to user; type identifies category of input for user
    // EFFECTS: returns user input string
    protected String promptForString(String prompt, String type) {
        String str;
        initialize();

        System.out.println(TextColors.MENU1 + prompt);
        while (true) {
            str = input.nextLine();
            if (str == null || str.isEmpty()) {
                throwInvalidInput(type);
            } else {
                break;
            }
        }
        return str;
    }

    // REQUIRES: two desired inputs r1 and r2, and String giving instructions to user
    // EFFECTS: returns string only if it is one of two response (for yes or no questions or similar)
    protected String promptBinaryResponse(String r1, String r2, String prompt) {
        String str;
        initialize();
        System.out.println(TextColors.MENU1 + prompt);
        while (true) {
            str = input.nextLine();
            if (r1.equals(str) || r2.equals(str)) {
                break;
            } else {
                throwInvalidInput("command");
            }
        }
        return str;
    }

    // REQUIRES: prompt is a statement giving directions to user; type identifies kind/function of int for user
    // EFFECTS: prompts user to enter int between min and max; returns user input int
    protected Integer promptForInteger(String prompt, String type, int min, int max) {
        int i;
        System.out.println(TextColors.MENU1 + prompt);
        while (true) {
            initialize();
            try {
                i = input.nextInt();
            } catch (Exception e) {
                i = min - 1;
            }
            if (i < min || i > max) {
                throwInvalidInput(type);
            } else {
                break;
            }
        }
        return i;
    }

    // EFFECTS: warns user they have entered an invalid input of type defined by parameter
    public void throwInvalidInput(String type) {
        System.out.println(TextColors.QUIT + "please enter a valid " + type);
    }
}
