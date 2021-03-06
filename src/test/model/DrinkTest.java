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
    void testEqualsDifferentObjectSameValues() {
        Drink drinkB = new Drink("scotch", amount, cost);
        assertFalse(drinkA.equals(drinkB));
        drinkA.setName("scotch");
        assertTrue(drinkA.equals(drinkB));
    }
}
