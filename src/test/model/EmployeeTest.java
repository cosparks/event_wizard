package model;

import model.show.Act;
import model.show.Drink;
import model.show.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    Employee employeeA;

    @BeforeEach
    void runBefore() {
        employeeA = new Employee("Jared", 111);
        employeeA.setJob("bartender");
    }

    @Test
    void testConstructor() {
        assertEquals("Jared", employeeA.getName());
        assertEquals(111, employeeA.getPay());
    }

    @Test
    void testSetters() {
        employeeA.setJob("Party Man");
        employeeA.setName("Ralph");
        employeeA.setPay(150);

        assertEquals("Party Man", employeeA.getJob());
        assertEquals("Ralph", employeeA.getName());
        assertEquals(150, employeeA.getPay());
    }

    @Test
    void testEqualsSameObject() {
        Employee employeeB = employeeA;
        assertTrue(employeeA.equals(employeeB));
    }

    @Test
    void testEqualsNullObject() {
        Employee employeeB = null;
        assertFalse(employeeA.equals(employeeB));
    }

    @Test
    void testEqualsDifferentTypeOfObject() {
        Drink drinkB = new Drink("Jared", 111, 0);
        assertFalse(employeeA.equals(drinkB));
    }

    @Test
    void testEqualsDifferentObjectSameValues() {
        Employee employeeB = new Employee("Frank", 111);
        assertFalse(employeeA.equals(employeeB));
        employeeB.setName("Jared");
        employeeB.setJob("bartender");
        assertTrue(employeeA.equals(employeeB));
    }
}
