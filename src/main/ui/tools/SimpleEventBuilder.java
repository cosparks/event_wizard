package ui.tools;

import model.SimpleEvent;

public class SimpleEventBuilder extends EventBuilder {

    private SimpleEvent event;

    public SimpleEventBuilder() {
    }

    // EFFECTS: calls super constructor to initialize name, then creates show with name
    public SimpleEventBuilder(String str) {
        super(str);
        event = new SimpleEvent(name);
    }

    // EFFECTS: runs main menu and returns show after user is finished creating it
    public SimpleEvent buildSimpleEvent() {
        runMainMenu();
        return event;
    }

    public void setEvent(SimpleEvent event) {
        this.event = event;
    }

    // MODIFIES: show
    // EFFECTS: displays main menu, allowing user to add details to and edit show object
    protected void runMainMenu() {
        String command;

        while (true) {
            if (displayMain) {
                System.out.println(UIColors.TITLE + "\t\t\t\t\t" + name + "\t\t\t\t\t");
                displayOptions();
                displayMain = false;
            }
            System.out.println(UIColors.MENU1 + "select details to add");
            command = input.next();
            command = command.toLowerCase();

            if ("s".equals(command)) {
                break;
            } else {
                processCommand(command);
            }
        }
    }

    // EFFECTS: Displays selection menu for simple events
    private void displayOptions() {
        System.out.println(UIColors.MENU2 + "\t[n]ame\t\t[d]ate\t\t[l]ocation\t\t[a]dd details");
        System.out.println(UIColors.MENU2 + "\t[i]mportance"
                + UIColors.MAIN_MENU + "\t\t\t\t\t\t\t\t\t\t\t[s]ave and return to main");

    }

    // EFFECTS: processes user input to MainMenu
    protected void processCommand(String command) {
        if ("n".equals(command)) {
            promptForName(event);
        } else if ("d".equals(command)) {
            setDate(event);
        } else if ("l".equals(command)) {
            promptForLocation(event);
        } else if ("a".equals(command)) {
            promptForDetails();
        } else if ("i".equals(command)) {
            setImportance(event);
        } else {
            System.out.println(UIColors.QUIT + "error: no option which corresponds to that command");
        }
    }

    // MODIFIES: event
    // EFFECTS: prompts user to add additional details to event
    protected void promptForDetails() {
        createDetail();

        while (true) {
            System.out.println(UIColors.MENU1 + "Add more information to event?  y/n");
            String answer = input.next();

            if (answer.equals("y")) {
                createDetail();
            } else if (answer.equals("n")) {
                break;
            } else {
                throwInvalidInput("answer");
            }
        }
        displayMain = true;
    }

    // MODIFIES: event
    // EFFECTS: creates and adds a detail to event's list of details
    public void createDetail() {
        String detail = promptForString("enter event detail: ", "entry");
        event.addDetail(detail);
    }
}
