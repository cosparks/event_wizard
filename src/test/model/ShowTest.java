package model;

import model.show.Act;
import model.show.Drink;
import model.show.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ShowTest {
    Show showA;
    Act actA;
    Act actB;

    @BeforeEach
    void runBefore() {
        showA = new Show("Bratboy");
        actA = new Act("Bratboy", 300);
        actB = new Act("Amyl", 1000);
        showA.addAct(actA);
        showA.addAct(actB);
        Employee employeeA = new Employee("John", 200);
        Employee employeeB = new Employee("Sarah", 200);
        showA.addEmployee(employeeA);
        showA.addEmployee(employeeB);
        Drink beer = new Drink(200, 2);
        beer.setSalePrice(5);
        showA.addDrink(beer);
        showA.setTicketPrice(10);
        showA.setProjectedSales(175);
    }

    @Test
    void testCalculateRevenueSimpleShow() {
        int cost = 300 + 1000 + (200 * 4);
        int revenue = 5 * 200 + 10 * 175;

        assertEquals(revenue - cost, showA.calculateRevenue());
    }

    @Test
    void testAddAdditionalCost() {
        int expense = 450;
        showA.setAdditionalCost(450);

        int cost = 300 + 1000 + (200 * 4) + expense;
        int revenue = 5 * 200 + 10 * 175;

        assertEquals(revenue - cost, showA.calculateRevenue());
    }

    @Test
    void testAddAdditionalCostTwice() {
        int expense1 = 450;
        int expense2 = 300;
        showA.setAdditionalCost(expense1);
        showA.setAdditionalCost(expense2);

        int cost = 300 + 1000 + (200 * 4) + expense1 + expense2;
        int revenue = 5 * 200 + 10 * 175;

        assertEquals(expense2, showA.getAdditionalCost());
        assertEquals(350, showA.calculateRevenue());
    }

    @Test
    void testCalculateRevenueChangeSalesAndTicketPrice() {
        int newTicketPrice = showA.getTicketPrice() + 10;
        int newProjectedSales = showA.getProjectedSales() + 15;
        int cost = 300 + 1000 + (200 * 4);
        int revenue = 5 * 200 + newTicketPrice * newProjectedSales;

        showA.setTicketPrice(newTicketPrice);
        showA.setProjectedSales(newProjectedSales);
        assertEquals(revenue - cost, showA.calculateRevenue());
    }

    @Test
    void testAddEmployee() {
        assertEquals(2, showA.getEmployees().size());

        Employee employeeC = new Employee("Colby", 200);
        showA.addEmployee(employeeC);

        assertEquals(3, showA.getEmployees().size());
    }

    @Test
    void testRemoveEmployee() {
        assertEquals(2, showA.getEmployees().size());
        Employee employeeC = new Employee("Colby", 200);
        showA.addEmployee(employeeC);
        showA.removeEmployee(2);
        assertEquals(2, showA.getEmployees().size());
    }

    @Test
    void testAddDrink() {
        Drink wine = new Drink(20, 15);
        assertEquals(1, showA.getBar().size());

        showA.addDrink(wine);
        assertEquals(2, showA.getBar().size());

    }

    @Test
    void testRemoveDrink() {
        Drink wine = new Drink(20, 15);
        assertEquals(1, showA.getBar().size());

        showA.addDrink(wine);
        showA.removeDrink(1);
        assertEquals(1, showA.getBar().size());

    }

    @Test
    void testAddAct() {
        Act actC = new Act("Flowers", 200);
        assertEquals(2, showA.getActs().size());

        showA.addAct(actC);
        assertEquals(3, showA.getActs().size());
    }

    @Test
    void testRemoveAct() {
        ArrayList<Act> testActs = new ArrayList<>();
        testActs.add(actB);
        assertEquals(2, showA.getActs().size());

        showA.removeAct(0);
        assertEquals(testActs, showA.getActs());
    }

    @Test
    void testSetCapacity() {
        showA.setCapacity(300);
        assertEquals(300, showA.getCapacity());
    }

}
