package model.show;

// Represents an act with name, pay, and additional info: rider and stageplot
public class Act {
    private String name;
    private int pay;
    private String rider;
    private String stagePlot;

    // EFFECTS: constructs a new act with name and how much they are to be paid
    public Act(String name, int pay) {
        this.name = name;
        this.pay = pay;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getStagePlot() {
        return stagePlot;
    }

    public void setStagePlot(String stagePlot) {
        this.stagePlot = stagePlot;
    }
}
