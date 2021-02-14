package model.show;

// Represents a drink with name, amount, cost per unit, sale price per unit
public class Drink {
    private String name;
    private int amount;
    private int cost;
    private int salePrice;


    public Drink(int amount, int cost) {
        this.amount = amount;
        this.cost = cost;
        salePrice = 0;
    }

    public Drink(String name, int amount, int cost) {
        this.name = name;
        this.amount = amount;
        this.cost = cost;
        salePrice = 0;
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
}
