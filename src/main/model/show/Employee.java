package model.show;

// represents an employee with name, pay and job name
public class Employee {
    private int pay;
    private String job;
    private String name;

    public Employee(String name, int pay) {
        this.name = name;
        this.pay = pay;
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
