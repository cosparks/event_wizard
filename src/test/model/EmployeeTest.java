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
    void testEqualsAllSameObjectAllPossibleDifferentValues() {
        Employee employeeB = new Employee("Frank", 180);
        employeeB.setJob("bartender");
        employeeA.setJob("sound");

        int j = 0;
        for (int i = 0; i < 7; i++) {
            if (i > 0 && i % 4 == 0) {
                employeeB.setName("Jared");
            }
            if (i > 0 && i % 2 == 0) {
                j++;
            }

            if (j % 2 == 0) {
                employeeB.setPay(180);
            } else if (j % 2 == 1) {
                employeeB.setPay(111);
            }
            if (i % 2 == 0) {
                employeeB.setJob("bartender");
            } else if (i % 2 == 1) {
                employeeB.setJob("sound");
            }
            assertFalse(employeeA.equals(employeeB));
        }
        employeeB.setJob("sound");
        assertTrue(employeeA.equals(employeeB));
    }
}
