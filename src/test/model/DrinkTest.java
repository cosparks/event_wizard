package model;

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
    void testConstructor() {
        assertEquals(0, drinkA.getSalePrice());
        assertEquals(amount, drinkA.getAmount());
        assertEquals(cost, drinkA.getCost());
    }

    @Test
    void testSetters() {
        drinkA.setAmount(150);
        drinkA.setSalePrice(6);
        drinkA.setCost(3);
        assertEquals(6, drinkA.getSalePrice());
        assertEquals(150, drinkA.getAmount());
        assertEquals(3, drinkA.getCost());
    }
}
