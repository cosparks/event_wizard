package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    private Schedule schedule;
    private ArrayList<Event> testEventList;
    private SimpleEvent eventA;
    private SimpleEvent eventB;
    SimpleEvent eventC;

    @BeforeEach
    void runBefore() {
        schedule = new Schedule();
        testEventList = new ArrayList<>();

        eventA = new SimpleEvent("A");
        EventDate dateA = new EventDate(16, 02, 2021, 15, 30);
        eventA.setStartDate(dateA);

        eventB = new SimpleEvent("B");
        EventDate dateB = new EventDate(15, 02, 2021, 15, 30);
        eventB.setStartDate(dateB);

        eventC = new SimpleEvent("C");
        EventDate dateC = new EventDate(01, 01, 2022, 15, 30);
        eventC.setStartDate(dateC);
    }

    @Test
    void testAddEventTwoEvents() {
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);
        testEventList.add(eventB);
        testEventList.add(eventA);

        assertEquals(schedule.getEvent(0), testEventList.get(0));
        assertEquals(schedule.getEvent(1), testEventList.get(1));
    }

    @Test
    void testAddEventMultipleWithDifferentDates() {
        SimpleEvent eventD = new SimpleEvent("D");
        EventDate dateD = new EventDate();
        dateD.setDate(15, 02, 2021, 14, 30);
        eventD.setStartDate(dateD);

        SimpleEvent eventE = new SimpleEvent("E");
        EventDate dateE = new EventDate();
        dateE.setDate(15, 01, 2021, 15, 30);
        eventE.setStartDate(dateE);

        SimpleEvent eventF = new SimpleEvent("F");
        EventDate dateF = new EventDate();
        dateF.setDate(15, 02, 2021, 14, 15);
        eventF.setStartDate(dateF);

        schedule.addEvent(eventA);
        schedule.addEvent(eventB);
        schedule.addEvent(eventC);
        schedule.addEvent(eventD);
        schedule.addEvent(eventE);
        schedule.addEvent(eventF);
        testEventList.add(eventE);
        testEventList.add(eventF);
        testEventList.add(eventD);
        testEventList.add(eventB);
        testEventList.add(eventA);
        testEventList.add(eventC);

        for (int i = 0; i < testEventList.size(); i++) {
            Event e1 = testEventList.get(i);
            Event e2 = schedule.getEvent(i);
            assertEquals(e1, e2);
        }
    }

    @Test
    void testGetEventsByImportanceTwoEvents() {
        eventA.setImportance(3);
        eventB.setImportance(11);
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);

        testEventList = schedule.getEventsByImportance();

        assertEquals(eventB, testEventList.get(0));
        assertEquals(eventA, testEventList.get(1));
    }

    @Test
    void testSortEventsOneChanged() {
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);
        schedule.addEvent(eventC);
        testEventList.add(eventC);
        testEventList.add(eventB);
        testEventList.add(eventA);

        eventC.changeStartDate(01, 01, 2021);
        schedule.sortEvents();

        for (int i = 0; i < schedule.getSize(); i++) {
            Event e1 = testEventList.get(i);
            Event e2 = schedule.getEvent(i);
            assertEquals(e1, e2);
        }
    }

    @Test
    void testRemoveEvent() {
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);
        schedule.addEvent(eventC);
        testEventList.add(eventA);
        testEventList.add(eventC);

        schedule.removeEvent(0);

        for (int i = 0; i < schedule.getSize(); i++) {
            Event e1 = testEventList.get(i);
            Event e2 = schedule.getEvent(i);
            assertEquals(e1, e2);
        }
    }

    @Test
    void testSortShows() {
        Show showA = new Show("A");
        EventDate dateA = new EventDate(1, 1, 2021, 18, 30);
        showA.setStartDate(dateA);

        schedule.addEvent(showA);
        schedule.addEvent(eventB);
        testEventList.add(showA);
        testEventList.add(eventB);

        assertTrue(schedule.getEvent(0) instanceof Show);
        assertTrue(schedule.getEvent(1) instanceof SimpleEvent);
    }
}