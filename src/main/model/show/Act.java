package model.show;

import persistence.Writable;
import org.json.JSONObject;

// Represents an act with name, pay, and additional info: rider and stageplot
public class Act implements Writable {
    private String name;
    private int pay;
    private String rider;
    private String stagePlot;

    // EFFECTS: constructs a new act with name and how much they are to be paid
    public Act(String name, int pay) {
        this.name = name;
        this.pay = pay;
        rider = "";
        stagePlot = "";
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("pay", pay);
        json.put("rider", rider);
        json.put("stage plot", stagePlot);
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
            Act act = (Act) obj;
            return name.equals(act.getName()) && pay == act.getPay()
                    && rider.equals(act.getRider()) && stagePlot.equals(act.getStagePlot());
        }
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
