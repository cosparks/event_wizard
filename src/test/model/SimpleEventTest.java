package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleEventTest {
    SimpleEvent eventA;

    @BeforeEach
    void runBefore() {
        eventA = new SimpleEvent("A");
        eventA.addDetail("bring sauce");
    }

    @Test
    void setImportance() {
        eventA.setImportance(1);
        assertEquals(1, eventA.getImportance());
    }

    @Test
    void addDetail() {
        eventA.addDetail("and beer");
        assertEquals("bring sauce", eventA.getDetail(0));
        assertEquals("and beer", eventA.getDetail(1));
    }

    @Test
    void clearDetails() {
        eventA.clearDetails();
        assertEquals(0, eventA.getNumberOfDetails());
    }

    @Test
    void clearDetail() {
        eventA.addDetail("and beer");
        eventA.clearDetail(0);
        assertEquals("and beer", eventA.getDetail(0));
    }
}