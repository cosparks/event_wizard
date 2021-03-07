package model;

import model.show.Act;
import model.show.Drink;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DrinkTest {
    private Drink drinkA;
    private int amount = 100;
    private int cost = 2;
    private int salePrice = 5;

    @BeforeEach
    void runBefore() {
        drinkA = new Drink(amount, cost);
    }

    @Test
    void testConstructorWithTwoArgs() {
        assertEquals(0, drinkA.getSalePrice());
        assertEquals(amount, drinkA.getAmount());
        assertEquals(cost, drinkA.getCost());
    }

    @Test
    void testSetters() {
        drinkA.setName("Red Scotch");
        drinkA.setAmount(150);
        drinkA.setSalePrice(6);
        drinkA.setCost(3);
        assertEquals("Red Scotch", drinkA.getName());
        assertEquals(6, drinkA.getSalePrice());
        assertEquals(150, drinkA.getAmount());
        assertEquals(3, drinkA.getCost());
    }

    @Test
    void testConstructorWithThreeArgs() {
        Drink drinkB = new Drink("Blue Scotch", 200, 3);
        assertEquals("Blue Scotch", drinkB.getName());
        assertEquals(200, drinkB.getAmount());
        assertEquals(3, drinkB.getCost());
    }

    @Test
    void testEqualsSameObject() {
        Drink drinkB = drinkA;
        assertTrue(drinkA.equals(drinkB));
    }

    @Test
    void testEqualsNullObject() {
        Drink drinkB  = null;
        assertFalse(drinkA.equals(drinkB));
    }

    @Test
    void testEqualsDifferentTypeOfObject() {
        Act actA = new Act("Ramones", 1500);
        assertFalse(drinkA.equals(actA));
    }

    @Test
    void testEqualsAllSameObjectAllPossibleDifferentValues() {
        Drink drinkB = new Drink("scotch", 50, 3);
        drinkB.setSalePrice(6);
        drinkA.setName("beer");
        drinkA.setSalePrice(salePrice);

        int j = 0;
        int k = 0;
        for (int i = 0; i < 15; i++) {
            if (i > 0 && i % 4 == 0) {
                k++;
            }
            if (i > 0 && i % 2 == 0) {
                j++;
            }
            if (i > 0 && i % 8 == 0) {
                drinkB.setName("beer");
            }

            if (k % 2 == 0) {
                drinkB.setAmount(50);
            } else if (k % 2 == 1) {
                drinkB.setAmount(amount);
            }
            if (j % 2 == 0) {
                drinkB.setCost(3);
            } else if (j % 2 == 1) {
                drinkB.setCost(cost);
            }
            if (i % 2 == 0) {
                drinkB.setSalePrice(6);
            } else if (i % 2 == 1) {
                drinkB.setSalePrice(salePrice);
            }
            assertFalse(drinkA.equals(drinkB));
        }
        drinkB.setSalePrice(salePrice);
        assertTrue(drinkA.equals(drinkB));
    }
}
