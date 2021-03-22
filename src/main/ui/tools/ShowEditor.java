package ui.tools;

import model.*;
import model.show.*;
import model.show.Act;

import java.util.ArrayList;

// UI tool for editing existing shows--subclass of EventBuilder and ShowBuilder
public class ShowEditor extends ShowBuilder {
    protected Show show;
    DisplayTool displayTool;

    // EFFECTS: Constructs a show editor and sets this.show and super.show to show
    public ShowEditor(Show show) {
        this.show = show;
        super.setShow(show);
        displayTool = new DisplayTool();
        displayTool.displayShowForEditor(show);
        name = show.getName();
        runMainMenu();
    }

    @Override
    // EFFECTS: processes user input from super.runMainMenu()
    protected void processCommand(String command) {
        if ("n".equals(command)) {
            promptForName(show);
        } else if ("d".equals(command)) {
            setDate(show);
        } else if ("v".equals(command)) {
            promptForLocation(show);
        } else if ("a".equals(command)) {
            promptEditActs();
        } else if ("b".equals(command)) {
            promptEditBar();
        } else if ("t".equals(command)) {
            promptForTickets();
        } else if ("c".equals(command)) {
            setCapacity();
        } else if ("x".equals(command)) {
            setExpenses();
        } else if ("e".equals(command)) {
            promptEditEmployees();
        } else {
            System.out.println(TextColors.QUIT + "error: no option which corresponds to that command");
        }
        redisplayShowForEditor();
    }

    private void redisplayShowForEditor() {
        displayTool.displayShowForEditor(show);
    }


    // EFFECTS: prompts user to select and edit show's acts
    private void promptEditActs() {
        listActs();
        String command = promptBinaryResponse("a", "e", "[a]dd new or [e]dit existing? ");
        if ("a".equals(command)) {
            promptForActs();
        } else if (show.getActs().isEmpty()) {
            System.out.println(TextColors.QUIT + "there are no acts to edit");
        } else {
            Act act = getActSelection();
            while (true) {
                editAct(act);
                String newCommand = promptBinaryResponse("y", "n","finished editing?  y/n");
                if ("y".equals(newCommand)) {
                    break;
                }
            }
        }
        displayMain = true;
    }

    // EFFECTS: displays itemized acts for user selection
    private void listActs() {
        ArrayList<Act> acts = show.getActs();
        int i = 1;
        for (Act a : acts) {
            System.out.printf("%-50.50s %-50.50s\n",
                    TextColors.MENU1 + i + "  " + TextColors.MENU2 + a.getName(),
                    TextColors.PURPLE + a.getPay());
            i++;
        }
    }

    // EFFECTS: returns act specified by user input
    private Act getActSelection() {
        int min = 1;
        int max = show.getActs().size();
        int selection = promptForInteger("select one of the above [1," + max + "]", "selection", min, max);
        int index = selection - 1;
        return show.getActs().get(index);
    }

    // MODIFIES: this and act
    // EFFECTS: runs editor menu and prompts user to edit values of act
    private void editAct(Act act) {
        String command;
        String name = act.getName();
        while (true) {
            command = promptForString("edit:\t[n]ame\t[p]ay\t[r]ider\t[s]tage plot", "selection");
            if ("n".equals(command) || "p".equals(command) || "r".equals(command) || "s".equals(command)) {
                break;
            } else {
                System.out.println("please enter a valid command");
            }
        }
        if ("n".equals(command)) {
            String newName = promptForString("enter name of act:", "name");
            act.setName(newName);
        } else if ("p".equals(command)) {
            int pay = promptForInteger("enter pay for " + name + ":", "amount", 0, Integer.MAX_VALUE);
            act.setPay(pay);
        } else if ("r".equals(command)) {
            String rider = promptForString("enter new rider for " + name, "entry");
            act.setRider(rider);
        } else {
            String stageplot = promptForString("enter new stageplot for " + name, "entry");
            act.setStagePlot(stageplot);
        }
    }

    // MODIFIES: this and employee
    // EFFECTS: runs editor menu and prompts user to edit values of employee
    public void editEmployee(Employee employee) {
        String command;
        String name = employee.getName();
        while (true) {
            command = promptForString("edit:\t[n]ame\t[p]ay\t[j]ob", "selection");
            if ("n".equals(command) || "p".equals(command) || "j".equals(command)) {
                break;
            } else {
                System.out.println("please enter a valid command");
            }
        }
        if ("n".equals(command)) {
            String newName = promptForString("enter name of employee:", "name");
            employee.setName(newName);
        } else if ("p".equals(command)) {
            int pay = promptForInteger("enter pay for " + name + ":", "amount", 0, Integer.MAX_VALUE);
            employee.setPay(pay);
        } else {
            String job = promptForString("enter job for " + name, "entry");
            employee.setJob(job);
        }
    }

    // EFFECTS: prompts user to add to or edit events employees
    private void promptEditEmployees() {
        listEmployees();
        String command = promptBinaryResponse("a", "e", "[a]dd new or [e]dit existing? ");
        if ("a".equals(command)) {
            promptForEmployees();
        } else if (show.getEmployees().isEmpty()) {
            System.out.println(TextColors.QUIT + "there are no employees to edit");
        } else {
            Employee employee = getEmployeeSelection();
            while (true) {
                editEmployee(employee);
                String newCommand = promptBinaryResponse("y", "n","finished editing?  y/n");
                if ("y".equals(newCommand)) {
                    break;
                }
            }
        }
        displayMain = true;
    }

    // EFFECTS: returns employee specified by user
    private Employee getEmployeeSelection() {
        int min = 1;
        int max = show.getEmployees().size();
        int selection = promptForInteger("select one of the above (by number)", "selection", min, max);
        int index = selection - 1;
        return show.getEmployees().get(index);
    }

    // EFFECTS: displays itemized employees for user selection
    private void listEmployees() {
        ArrayList<Employee> employees = show.getEmployees();
        int i = 1;
        for (Employee e : employees) {
            System.out.printf("%-30.30s %-30.30s %-30.30s\n",
                    TextColors.MENU1 + i + "  " + TextColors.MENU2 + e.getName(),
                    TextColors.PURPLE + e.getPay(),
                    e.getJob());
            i++;
        }
    }

    // EFFECTS: prompts user to select between adding to or editing current bar items
    private void promptEditBar() {
        listDrinks();
        int drinks = show.getBar().size();

        String command = promptBinaryResponse("a", "e", "[a]dd new or [e]dit existing? ");
        if ("a".equals(command)) {
            promptForBar();
        } else if (drinks == 0) {
            System.out.println(TextColors.QUIT + "there are no drinks to edit");
        } else {
            int selection = promptForInteger("select one of the above", "selection", 1, drinks);
            int index = selection - 1;
            Drink drink = show.getBar().get(index);
            while (true) {
                editDrink(drink);
                String newCommand = promptBinaryResponse("y", "n","finished editing?  y/n");
                if ("y".equals(newCommand)) {
                    break;
                }
            }
        }
        displayMain = true;
    }

    // MODIFIES: this and drink
    // EFFECTS: runs editor menu and prompts user to edit values of drink
    private void editDrink(Drink drink) {
        String name = drink.getName();
        String command = getEditDrinkCommand();
        if ("n".equals(command)) {
            String newName = promptForString("enter name of bar item:", "name");
            drink.setName(newName);
        } else if ("a".equals(command)) {
            int amount = promptForInteger("enter amount for " + name + ":", "amount",
                    0, Integer.MAX_VALUE);
            drink.setAmount(amount);
        } else if ("c".equals(command)) {
            int cost = promptForInteger("enter cost for " + name + ":", "amount",
                    0, Integer.MAX_VALUE);
            drink.setCost(cost);
        } else {
            int salePrice = promptForInteger("enter sale price for " + name + ":", "amount",
                    0, Integer.MAX_VALUE);
            drink.setSalePrice(salePrice);
        }
    }

    // EFFECTS: prompts user to select which fields they want to change for drink
    private String getEditDrinkCommand() {
        String command;
        while (true) {
            command = promptForString("edit:\t[n]ame\t[a]mount\t[c]ost\t[s]ale price", "selection");
            if ("n".equals(command) || "a".equals(command) || "c".equals(command) || "s".equals(command)) {
                break;
            } else {
                System.out.println("please enter a valid command");
            }
        }
        return command;
    }

    // EFFECTS: displays itemized drinks for user selection
    private void listDrinks() {
        ArrayList<Drink> acts = show.getBar();
        int i = 1;
        for (Drink d : acts) {
            System.out.printf("%-40.40s %-20.20s %-20.20s %-20.20s\n",
                    TextColors.MENU1 + i + " " + TextColors.MENU2 + d.getName(),
                    TextColors.PURPLE + d.getAmount(),
                    TextColors.QUIT + "$" + d.getCost(),
                    TextColors.MENU2 + "$" + d.getSalePrice());
            i++;
        }
    }
}
