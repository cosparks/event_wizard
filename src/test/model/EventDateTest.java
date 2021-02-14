package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventDateTest {
    EventDate date;
    EventDate dateA;

    @BeforeEach
    void runBefore() {
        date = new EventDate(11, 11, 2022, 18, 30);
        dateA = new EventDate(1, 1, 1999, 8, 5);
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
        date.setDay(8);
        date.setMonth(12);
        date.setYear(2021);
        date.setHour(13);
        date.setMinute(25);
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
}