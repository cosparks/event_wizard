package model;

import model.show.Act;
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
}