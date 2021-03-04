package model.show;

import persistence.Writable;
import org.json.JSONObject;

// represents an employee with name, pay and job name
public class Employee implements Writable {
    private int pay;
    private String job;
    private String name;

    // EFFECTS: Constructs and employee with name and pay
    public Employee(String name, int pay) {
        this.name = name;
        this.pay = pay;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("pay", pay);
        json.put("job", job);
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
            Employee employee = (Employee) obj;
            return name.equals(employee.getName()) && pay == employee.getPay()
                    && job.equals(employee.getJob());
        }
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
