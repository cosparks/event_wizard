package ui.tools;

import model.SimpleEvent;
import model.show.Act;

public class SimpleEventEditor extends SimpleEventBuilder {
    SimpleEvent event;
    DisplayTool displayTool;

    public SimpleEventEditor(SimpleEvent event) {
        this.event = event;
        super.setEvent(event);
        name = event.getName();
        displayTool = new DisplayTool();
        displayTool.displaySimpleEventForEditor(event);
        runMainMenu();
    }

    @Override
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
            System.out.println(UIColors.QUIT + "error: no option which corresponds to that command");
        }
    }

    private void promptEditDetails() {
        listDetails();
        String command = promptBinaryResponse("a", "e", "[a]dd new or [e]dit existing? ");
        if ("a".equals(command)) {
            createDetail();
        } else if (event.getNumberOfDetails() == 0) {
            System.out.println(UIColors.QUIT + "there are no details to edit");
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

    private void selectDetail() {
        int min = 1;
        int max = event.getNumberOfDetails();
        int selection = promptForInteger("select one of the above (by number)", "selection", min, max);
        int index = selection - 1;
        event.clearDetail(index);
    }

    private void editDetail() {
        event.addDetail(promptForString("please enter any relevant information ", "input"));
    }

    private void listDetails() {
        int size = event.getNumberOfDetails();
        for (int i = 0; i < size; i++) {
            int num = i + 1;
            System.out.println(UIColors.MENU1 + num + "  " + event.getDetail(i));
        }
    }
}
