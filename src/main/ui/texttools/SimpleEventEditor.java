package ui.texttools;

import model.SimpleEvent;

// Represents UI tool for editing simple events--subclass of SimpleEventBuilder and EventBuilder
public class SimpleEventEditor extends SimpleEventBuilder {
    SimpleEvent event;
    DisplayTool displayTool;

    // EFFECTS: Constructs a simple event editor and sets this.event and super.event to event
    public SimpleEventEditor(SimpleEvent event) {
        this.event = event;
        super.setEvent(event);
        name = event.getName();
        displayTool = new DisplayTool();
        displayTool.displaySimpleEventForEditor(event);
        runMainMenu();
    }

    @Override
    // EFFECTS: processes user input from super's main menu in order to modify this event
    protected void processCommand(String command) {
        if ("n".equals(command)) {
            promptForName(event);
        } else if ("d".equals(command)) {
            setDate(event);
        } else if ("l".equals(command)) {
            promptForLocation(event);
        } else if ("a".equals(command)) {
            promptEditDetails();
        } else if ("i".equals(command)) {
            setImportance(event);
        } else {
            System.out.println(TextColors.QUIT + "error: no option which corresponds to that command");
        }
        redisplayEventForEditor();
    }

    // EFFECTS: displays and prompts user to edit details of event
    private void promptEditDetails() {
        listDetails();
        String command = promptBinaryResponse("a", "e", "[a]dd new or [e]dit existing? ");
        if ("a".equals(command)) {
            createDetail();
        } else if (event.getNumberOfDetails() == 0) {
            System.out.println(TextColors.QUIT + "there are no details to edit");
        } else {
            selectDetail();
            while (true) {
                editDetail();
                String newCommand = promptBinaryResponse("y", "n","finished editing?  y/n");
                if ("y".equals(newCommand)) {
                    break;
                }
            }
        }
        displayMain = true;
    }

    private void redisplayEventForEditor() {
        displayTool.displaySimpleEventForEditor(event);
    }

    // EFFECTS: prompts user to select detail from list and clears that detail
    private void selectDetail() {
        int min = 1;
        int max = event.getNumberOfDetails();
        int selection = promptForInteger("select one of the above (by number)", "selection", min, max);
        int index = selection - 1;
        event.clearDetail(index);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to enter detail and adds detail to list
    private void editDetail() {
        event.addDetail(promptForString("please enter any relevant information ", "input"));
    }

    // EFFECTS: displays itemized list of details for user selection
    private void listDetails() {
        int size = event.getNumberOfDetails();
        for (int i = 0; i < size; i++) {
            int num = i + 1;
            System.out.println(TextColors.MENU1 + num + "  " + event.getDetail(i));
        }
    }
}
