package model;

import exceptions.InvalidNameException;
import model.show.Act;
import model.show.Drink;
import model.show.Employee;

import persistence.Writable;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

// Represents a show with acts, bar items, employees, tickets, venue capacity and additional costs
public class Show extends ScheduleEvent implements Writable {
    private ArrayList<Act> acts;
    private ArrayList<Employee> employees;
    private ArrayList<Drink> bar;
    private int capacity;
    private int projectedSales;
    private int ticketPrice;
    private int additionalCost;

    // EFFECTS: constructs a new show with name--instantiates arrays for bar, employees and acts
    public Show(String name) {
        super(name);
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

    public ArrayList<Drink> getBar() {
        return bar;
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

    @Override
    public JSONObject toJson() {
        JSONObject json = super.toJson();
        json.put("type", "show");
        json.put("capacity", capacity);
        json.put("projected sales", projectedSales);
        json.put("ticket price", ticketPrice);
        json.put("additional cost", additionalCost);
        json.put("employees", employeesToJson());
        json.put("acts", actsToJson());
        json.put("bar", barToJson());
        return json;
    }

    // EFFECTS: returns list of employees as json array
    private JSONArray employeesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Employee e : employees) {
            jsonArray.put(e.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns list of acts as json array
    private JSONArray actsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Act a : acts) {
            jsonArray.put(a.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns list of bar items as json array
    private JSONArray barToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Drink d : bar) {
            jsonArray.put(d.toJson());
        }
        return jsonArray;
    }

    public int getNumActs() {
        return acts.size();
    }

    public int getNumEmployees() {
        return employees.size();
    }

    public int getNumDrinks() {
        return bar.size();
    }

    public Drink getDrink(int index) {
        return bar.get(index);
    }

    public Employee getEmployee(int index) {
        return employees.get(index);
    }

    public Act getAct(int index) {
        return acts.get(index);
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
        this.additionalCost = cost;
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
