package model;

import model.show.Act;
import model.show.Drink;
import model.show.Employee;

import java.util.ArrayList;

// Represents a show with acts, bar items, employees, tickets, venue capacity and additional costs
public class Show extends Event {
    private ArrayList<Act> acts;
    private ArrayList<Employee> employees;
    private ArrayList<Drink> bar;
    private int capacity;
    private int projectedSales;
    private int ticketPrice;
    private int additionalCost;

    // EFFECTS: constructs a new show with name--instantiates arrays for bar, employees and acts
    public Show(String name) {
        this.name = name;
        acts = new ArrayList<>();
        employees = new ArrayList<>();
        bar = new ArrayList<>();
        additionalCost = 0;
        capacity = 0;
        ticketPrice = 0;
        importance = 8;
    }

    // MODIFIES: this
    // EFFECTS: adds act to list of acts
    public void addAct(Act act) {
        acts.add(act);
    }

    // MODIFIES: this
    // EFFECTS: removes act from list of acts
    public void removeAct(int index) {
        acts.remove(index);
    }

    public ArrayList<Act> getActs() {
        return acts;
    }

    // MODIFIES: this
    // EFFECTS: adds employee to list of employees
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    // MODIFIES: this
    // EFFECTS: removes employee from list of employees
    public void removeEmployee(int index) {
        employees.remove(index);
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    // MODIFIES: this
    // EFFECTS: adds drink to bar
    public void addDrink(Drink drink) {
        bar.add(drink);
    }

    // REQUIRES: index within list range
    // MODIFIES: this
    // EFFECTS: removes drink from bar
    public void removeDrink(int index) {
        bar.remove(index);
    }

    // EFFECTS: returns projected revenue from tickets minus expenses
    public int calculateRevenue() {
        int costs = additionalCost;
        int revenue = ticketPrice * projectedSales;
        for (Employee e : employees) {
            costs = costs + e.getPay();
        }
        for (Act a : acts) {
            costs = costs + a.getPay();
        }
        for (Drink d : bar) {
            costs = costs + d.calculateTotalCost();
            revenue = revenue + d.calculateRevenue();
        }
        return revenue - costs;
    }

    public ArrayList<Drink> getBar() {
        return bar;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int price) {
        ticketPrice = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setAdditionalCost(int cost) {
        additionalCost = additionalCost + cost;
    }

    public int getAdditionalCost() {
        return additionalCost;
    }

    public int getProjectedSales() {
        return projectedSales;
    }

    public void setProjectedSales(int sales) {
        projectedSales = sales;
    }
}
