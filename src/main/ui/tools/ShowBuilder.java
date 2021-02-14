package ui.tools;

import model.Show;
import model.show.*;

public class ShowBuilder extends EventBuilder {
    private Show show;

    // EFFECTS: calls super constructor to initialize name, then creates show with name
    public ShowBuilder() {
        super();
        show = new Show(name);
    }

    // EFFECTS: runs main menu and returns show after user is finished creating it
    public Show buildShow() {
        runMainMenu();
        return show;
    }

    // MODIFIES: show
    // EFFECTS: displays main menu, allowing user to add details to and edit show object
    private void runMainMenu() {
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

    // EFFECTS: displays show-builder main menu
    private void displayOptions() {
        System.out.println(UIColors.MENU2 + "\t[n]ame\t\t[d]ate\t\t[v]enue\t\t\t[a]cts\t\t\t[b]ar");
        System.out.println("\t[t]ickets\t[c]apacity\te[x]penses\t\t[e]mployees"
                + UIColors.MAIN_MENU + "\t\t[s]ave and return to main");

    }

    // EFFECTS: processes user input to MainMenu
    private void processCommand(String command) {
        if ("n".equals(command)) {
            promptForName(show);
        } else if ("d".equals(command)) {
            setDate(show);
        } else if ("v".equals(command)) {
            promptForLocation(show);
        } else if ("a".equals(command)) {
            promptForActs();
        } else if ("b".equals(command)) {
            promptForBar();
        } else if ("t".equals(command)) {
            promptForTickets();
        } else if ("c".equals(command)) {
            setCapacity();
        } else if ("x".equals(command)) {
            setExpenses();
        } else if ("e".equals(command)) {
            promptForEmployees();
        } else {
            System.out.println(UIColors.QUIT + "error: no event which corresponds to that number");
        }
    }

    // MODIFIES: show
    // EFFECTS: prompts user to add multiple acts to show
    private void promptForActs() {
        createAct();

        while (true) {
            System.out.println(UIColors.MENU1 + "Add another act?  y/n");
            String answer = input.next();

            if (answer.equals("y")) {
                createAct();
            } else if (answer.equals("n")) {
                break;
            } else {
                throwInvalidInput("answer");
            }
        }
        displayMain = true;
    }

    // MODIFIES: show
    // EFFECTS: creates an act with name and pay (other info optional) and adds it to show
    private void createAct() {
        String actName = promptForString("enter name of act:", "name");
        int pay = promptForInteger("enter pay for " + actName + ":", "amount", 0, Integer.MAX_VALUE);

        Act act = new Act(actName, pay);

        promptForRiderAndStagePlot(act);
        show.addAct(act);
        System.out.println(UIColors.MENU1 + "Added new act: " + UIColors.MAIN_MENU + actName + UIColors.MENU1
                + ", with pay: " + UIColors.MAIN_MENU + pay);
    }

    // MODIFIES: act
    // EFFECTS: adds rider and stage plot to act
    private void promptForRiderAndStagePlot(Act act) {
        String rider;
        String stagePlot;

        System.out.println(UIColors.MENU1 + "Add rider and stage plot?  y/n");
        while (true) {
            initialize();
            String answer = input.nextLine();
            if (answer.equals("y")) {
                rider = promptForString("enter rider below", "rider");
                stagePlot = promptForString("enter stageplot below", "stageplot");

                act.setRider(rider);
                act.setStagePlot(stagePlot);
            } else if (answer.equals("n")) {
                break;
            } else {
                throwInvalidInput("input");
            }
        }
    }

    // MODIFIES: show
    // EFFECTS: prompts user to enter multiple bar items to bar
    private void promptForBar() {
        createDrink();

        while (true) {
            System.out.println(UIColors.MENU1 + "Add another bar item?  y/n");
            String answer = input.next();

            if (answer.equals("y")) {
                createDrink();
            } else if (answer.equals("n")) {
                break;
            } else {
                throwInvalidInput("response");
            }
        }
        displayMain = true;
    }

    // MODIFIES: show
    // EFFECTS: creates drink and adds it to drink list in show
    private void createDrink() {
        String name = promptForString("enter name of bar item: ", "entry");
        int amount = promptForInteger("number of units: ", "entry", 0, Integer.MAX_VALUE);
        int cost = promptForInteger("cost per unit: ", "entry", 0, Integer.MAX_VALUE);
        int salePrice = promptForInteger("sale price: ", "entry", 0, Integer.MAX_VALUE);

        Drink drink = new Drink(name, amount, cost);
        drink.setSalePrice(salePrice);
        show.addDrink(drink);
        System.out.println(UIColors.MENU1 + "Added " + UIColors.MAIN_MENU + amount + UIColors.MENU1 + " units of "
                + UIColors.MAIN_MENU + name + UIColors.MENU1 + " to bar at cost of " + UIColors.MAIN_MENU + cost
                + UIColors.MENU1 + " per unit and sale price of " + UIColors.MAIN_MENU + salePrice);
    }

    // MODIFIES: show
    // EFFECTS: prompts user to enter information for tickets
    private void promptForTickets() {
        int ticketPrice = promptForInteger("enter ticket price: ", "entry", 0, Integer.MAX_VALUE);
        int projectedSales = promptForInteger("enter projected ticket sales: ",
                                            "entry", 0, Integer.MAX_VALUE);

        show.setTicketPrice(ticketPrice);
        show.setProjectedSales(projectedSales);
        System.out.println(UIColors.MENU1 + "ticket price set to " + UIColors.MAIN_MENU + ticketPrice
                + UIColors.MENU1 + " and projected sales to " + UIColors.MAIN_MENU + projectedSales);
        displayMain = true;
    }

    private void setCapacity() {
        int capacity = promptForInteger("enter max capacity of venue",
                "entry", 0, Integer.MAX_VALUE);
        show.setCapacity(capacity);
        System.out.println(UIColors.MENU1 + "venue capacity set to: " + UIColors.MAIN_MENU + capacity);
        displayMain = true;
    }

    private void setExpenses() {
        int expenses = promptForInteger("enter additional expenses for event",
                                        "entry", 0, Integer.MAX_VALUE);
        show.setAdditionalCost(expenses);
        System.out.println(UIColors.MENU1 + "additional expenses set to: " + UIColors.MAIN_MENU + expenses);
    }

    // MODIFIES: show
    // EFFECTS: prompts user to add multiple acts to show
    private void promptForEmployees() {
        createEmployee();

        while (true) {
            System.out.println(UIColors.MENU1 + "Add another employee? ");
            String answer = input.next();

            if (answer.equals("y")) {
                createEmployee();
            } else if (answer.equals("n")) {
                break;
            } else {
                throwInvalidInput("answer");
            }
        }
        displayMain = true;
    }

    // MODIFIES: show
    // EFFECTS: constructs a new employee and adds them to show.employees
    private void createEmployee() {
        String employeeName = promptForString("enter name of employee:", "name");
        String job = promptForString("enter job title: ", "title");
        int pay = promptForInteger("enter pay for " + employeeName
                                    + ":", "amount", 0, Integer.MAX_VALUE);

        Employee employee = new Employee(employeeName, pay);
        employee.setJob(job);

        show.addEmployee(employee);

        System.out.println(UIColors.MENU1 + "Added new employee " + UIColors.MAIN_MENU + employeeName
                    + UIColors.MENU1 + " working " + UIColors.MAIN_MENU + employee.getJob()
                    + UIColors.MENU1 + " for " + UIColors.MAIN_MENU + pay);
    }
}
