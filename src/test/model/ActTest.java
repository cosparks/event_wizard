package model;

import model.show.Act;
import model.show.Drink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActTest {
    Act actA;

    @BeforeEach
    void runBefore() {
        actA = new Act("Ramones", 1500);
    }

    @Test
    void testConstructor() {
        assertEquals("Ramones", actA.getName());
        assertEquals(1500, actA.getPay());
    }

    @Test
    void testSetters() {
        actA.setName("Iggy Pop");
        actA.setPay(2000);
        actA.setRider("1000 peanuts");
        actA.setStagePlot("Mic in the front");

        assertEquals("Iggy Pop", actA.getName());
        assertEquals(2000, actA.getPay());
        assertEquals("1000 peanuts", actA.getRider());
        assertEquals("Mic in the front", actA.getStagePlot());
    }

    @Test
    void testEqualsSameObject() {
        Act actB = actA;
        assertTrue(actA.equals(actB));
    }

    @Test
    void testEqualsNullObject() {
        Act actB = null;
        assertFalse(actA.equals(actB));
    }

    @Test
    void testEqualsDifferentTypeOfObject() {
        Drink drinkB = new Drink("Ramones", 1500, 3);
        assertFalse(actA.equals(drinkB));
    }

    @Test
    void testEqualsAllSameObjectAllPossibleDifferentValues() {
        actA.setStagePlot("A");
        actA.setRider("B");
        Act actB = new Act("Beanie Man", 1400);
        actB.setStagePlot("C");
        actB.setRider("D");

        int j = 0;
        int k = 0;
        for (int i = 0; i < 15; i++) {
            if (i > 0 && i % 4 == 0) {
                k++;
            }
            if (i > 0 && i % 8 == 0) {
                actB.setName("Ramones");
            }
            if (i > 0 && i % 2 == 0) {
                j++;
            }

            if (k % 2 == 0) {
                actB.setPay(1400);
            } else if (k % 2 == 1) {
                actB.setPay(1500);
            }
            if (j % 2 == 0) {
                actB.setRider("D");
            } else if (j % 2 == 1) {
                actB.setRider("B");
            }
            if (i % 2 == 0) {
                actB.setStagePlot("C");
            } else if (i % 2 == 1) {
                actB.setStagePlot("A");
            }
            assertFalse(actA.equals(actB));
        }
        actB.setStagePlot("A");
        assertTrue(actA.equals(actB));
    }
}