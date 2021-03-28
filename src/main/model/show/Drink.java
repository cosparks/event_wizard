package model.show;

import persistence.Writable;
import org.json.JSONObject;

import java.util.ArrayList;

// Represents a drink with name, amount, cost per unit, sale price per unit
public class Drink implements Writable {
    private String name;
    private int amount;
    private int cost;
    private int salePrice;

    // EFFECTS: Constructs a drink with amount and cost, automatically setting name to drink (for display)
    public Drink(int amount, int cost) {
        name = "drink";
        this.amount = amount;
        this.cost = cost;
        salePrice = 0;
    }

    // EFFECTS: constructs a drink with name, amount and cost
    public Drink(String name, int amount, int cost) {
        this.name = name;
        this.amount = amount;
        this.cost = cost;
        salePrice = 0;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("amount", amount);
        json.put("cost", cost);
        json.put("sale price", salePrice);
        return json;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            Drink drink = (Drink) obj;
            return name.equals(drink.getName()) && amount == drink.getAmount()
                    && cost == drink.getCost() && salePrice == drink.getSalePrice();
        }
    }

    // EFFECTS: returns the cost of all units at given unit price
    public int calculateTotalCost() {
        return cost * amount;
    }

    // EFFECTS: returns the potential revenue if all units are sold at sale price
    public int calculateRevenue() {
        return amount * salePrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
