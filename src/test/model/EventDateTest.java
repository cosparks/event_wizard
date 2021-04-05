package model;

import exceptions.DateFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.texttools.TextColors;

import static org.junit.jupiter.api.Assertions.*;

class EventDateTest {
    EventDate date;
    EventDate dateA;

    @BeforeEach
    void runBefore() {
        try {
            date = new EventDate(11, 11, 2022, 18, 30);
            dateA = new EventDate(1, 1, 1999, 8, 5);
        } catch (DateFormatException dfe) {
            System.out.println(TextColors.QUIT
                    + "DateFormatException: Unable to initialize event dates in @BeforeEach method");
        }
    }

    @Test
    void setDateTest() {
        assertEquals(11, date.getDay());
        assertEquals(11, date.getMonth());
        assertEquals(2022, date.getYear());
        assertEquals(18, date.getHour());
        assertEquals(30, date.getMinute());
    }

    @Test
    void setDateValueTest() {
        try {
            date.setDay(8);
            date.setMonth(12);
            date.setYear(2021);
            date.setHour(13);
            date.setMinute(25);
        } catch (DateFormatException e) {
            fail("DateFormatException not expected");
        }
        assertEquals(8, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2021, date.getYear());
        assertEquals(13, date.getHour());
        assertEquals(25, date.getMinute());
    }

    @Test
    void testGetTimeAsString() {
        assertEquals("18:30", date.getTimeForDisplay());
        assertEquals("08:05", dateA.getTimeForDisplay());
    }

    @Test
    void testGetDateAsString() {
        assertEquals("2022-11-11-18:30", date.getDateAsString());
        assertEquals("1999-01-01-08:05", dateA.getDateAsString());
    }

    @Test
    void testGetDateForDisplay() {
        String expectedDateForDisplayA = "14/02/2021";
        String expectedDateForDisplayB = "01/12/2021";

        try {
            dateA.setDate(14, 02, 2021, 12, 30);
            assertEquals(expectedDateForDisplayA, dateA.getDateForDisplay());
            dateA.setDate(1, 12, 2021, 0, 0);
            assertEquals(expectedDateForDisplayB, dateA.getDateForDisplay());
        } catch (DateFormatException dfe) {
            fail("DateFormatException not expected");
        }
    }

    // the following methods indirectly test the verifyDateValues method in EventDate
    @Test
    void testSetDateInvalidDay() {
        try {
            dateA.setDay(0);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }

        try {
            dateA.setDay(32);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }
        assertEquals(1, dateA.getDay());
    }

    @Test
    void testSetMonthInvalidMonth() {
        try {
            dateA.setMonth(0);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }

        try {
            dateA.setMonth(13);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }
        assertEquals(1, dateA.getMonth());
    }

    @Test
    void testSetYearInvalidYear() {
        try {
            dateA.setYear(1899);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }

        try {
            dateA.setYear(3001);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }
        assertEquals(1999, dateA.getYear());
    }

    @Test
    void testSetHourInvalidHour() {
        try {
            dateA.setHour(-1);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }

        try {
            dateA.setHour(24);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }
        assertEquals(8, dateA.getHour());
    }

    @Test
    void testSetMinuteInvalidMinute() {
        try {
            dateA.setMinute(-1);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }

        try {
            dateA.setMinute(60);
            fail("DateFormatException expected");
        } catch (DateFormatException e) {
            // pass
        }
        assertEquals(5, dateA.getMinute());
    }
}