package model;

import exceptions.DateFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.texttools.TextColors;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    ScheduleEvent eventA;
    EventDate date1;

    @BeforeEach
    void runBefore() {
        eventA = new SimpleEvent("A");
        try {
            date1 = new EventDate(02, 11, 2021, 23, 30);
            eventA.setStartDate(date1);
        } catch (DateFormatException dfe) {
            System.out.println(TextColors.QUIT + "DateFormatException: unable to run @BeforeEach method");
        }
    }

    @Test
    void testSetStartDate() {
        assertEquals(date1, eventA.getStartDate());
    }

//    @Test
//    void testSetStartDateGetEndDate() {
//        assertEquals(03, eventA.getEndDate().getDay());
//        assertEquals(00, eventA.getEndDate().getHour());
//    }

    @Test
    void testChangeStartDate() {
        try {
            eventA.changeStartDate(03, 11, 2021);
        } catch (DateFormatException e) {
            fail("DateFormatException not expected");
        }
        assertEquals(3, eventA.getStartDate().getDay());
        // assertEquals(4, eventA.getEndDate().getDay());
        // assertEquals(0, eventA.getEndDate().getHour());
    }

    @Test
    void testChangeStartTime() {
        try {
            eventA.changeStartTime(18, 25);
        } catch (DateFormatException e) {
            fail("DateFormatException not expected");
        }
        assertEquals(18, eventA.getStartDate().getHour());
        assertEquals(25, eventA.getStartDate().getMinute());
        // assertEquals(19, eventA.getEndDate().getHour());
        // assertEquals(2, eventA.getEndDate().getDay());
    }

//    @Test
//    void testChangeEndTime() {
//        eventA.changeEndTime(1, 17);
//
//        assertEquals(1, eventA.getEndDate().getHour());
//        assertEquals(17, eventA.getEndDate().getMinute());
//    }

//    @Test
//    void testChangeEndDate() {
//        eventA.changeEndDate(17, 03, 2022);
//
//        assertEquals(17, eventA.getEndDate().getDay());
//        assertEquals(03, eventA.getEndDate().getMonth());
//        assertEquals(2022, eventA.getEndDate().getYear());
//    }

    @Test
    void testSetLocation() {
        eventA.setLocation("Happy's");
        assertEquals("Happy's", eventA.getLocation());
        eventA.setLocation("Sandino's");
        assertEquals("Sandino's", eventA.getLocation());
    }

    @Test
    void testSetName() {
        assertEquals("A", eventA.getName());

        String newName = "New Event Name";
        eventA.setName(newName);

        assertEquals(newName, eventA.getName());
    }
}
