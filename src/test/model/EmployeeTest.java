package model;

import model.show.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    Employee employeeA;

    @BeforeEach
    void runBefore() {
        employeeA = new Employee("Jared", 111);
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
}
