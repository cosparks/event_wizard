package model;

import exceptions.DateFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.texttools.TextColors;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {
    private Schedule schedule;
    private ArrayList<ScheduleEvent> testEventList;
    private SimpleEvent eventA;
    private SimpleEvent eventB;
    private SimpleEvent eventC;
    private SimpleEvent eventD;
    private SimpleEvent eventE;
    private SimpleEvent eventF;

    @BeforeEach
    void runBefore() {
        schedule = new Schedule();
        testEventList = new ArrayList<>();
        try {
            eventA = new SimpleEvent("A");  // event A
            EventDate dateA = new EventDate(16, 02, 2021, 15, 30);
            eventA.setStartDate(dateA);
            eventB = new SimpleEvent("B");  // event B
            EventDate dateB = new EventDate(15, 02, 2021, 15, 30);
            eventB.setStartDate(dateB);
            eventC = new SimpleEvent("C");  // event C
            EventDate dateC = new EventDate(01, 01, 2022, 15, 30);
            eventC.setStartDate(dateC);
            eventD = new SimpleEvent("D");  // event D
            EventDate dateD = new EventDate(15, 02, 2021, 14, 30);
            eventD.setStartDate(dateD);
            eventE = new SimpleEvent("E");  // event E
            EventDate dateE = new EventDate(15, 01, 2021, 15, 30);
            eventE.setStartDate(dateE);
            eventF = new SimpleEvent("F");  // event F
            EventDate dateF = new EventDate(15, 02, 2021, 14, 15);
            eventF.setStartDate(dateF);
        } catch (DateFormatException dfe) {
            System.out.println(TextColors.QUIT + "DateFormatException: unable to run @BeforeEach method");
        }
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
    // Tests add event as well is the schedule's sort method
    void testAddEventMultipleWithDifferentDates() {
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
            ScheduleEvent e1 = testEventList.get(i);
            ScheduleEvent e2 = schedule.getEvent(i);
            assertEquals(e1, e2);
        }
    }

    @Test
    void testGetEventsByImportanceTwoEvents() {
        eventA.setImportance(3);
        eventB.setImportance(9);
        schedule.addEvent(eventB);
        schedule.addEvent(eventA);



        testEventList = schedule.getEventsByImportance();

        assertEquals(eventB, testEventList.get(0));
        assertEquals(eventA, testEventList.get(1));
    }

    @Test
    void testGetEventsByImportanceManyEvents() {
        eventA.setImportance(2);
        eventB.setImportance(1);
        eventC.setImportance(4);
        eventD.setImportance(5);
        eventE.setImportance(9);
        eventF.setImportance(8);
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);
        schedule.addEvent(eventC);
        schedule.addEvent(eventD);
        schedule.addEvent(eventE);
        schedule.addEvent(eventF);

        testEventList = schedule.getEventsByImportance();
        assertEquals(eventE, testEventList.get(0));
        assertEquals(eventF, testEventList.get(1));
        assertEquals(eventD, testEventList.get(2));
        assertEquals(eventC, testEventList.get(3));
        assertEquals(eventA, testEventList.get(4));
        assertEquals(eventB, testEventList.get(5));
    }

    @Test
    void testSortEventsOneChanged() {
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);
        schedule.addEvent(eventC);
        testEventList.add(eventC);
        testEventList.add(eventB);
        testEventList.add(eventA);

        try {
            eventC.changeStartDate(01, 01, 2021);
        } catch (DateFormatException e) {
            fail("DateFormatException not expected");
        }

        schedule.sortEvents();

        for (int i = 0; i < schedule.getSize(); i++) {
            ScheduleEvent e1 = testEventList.get(i);
            ScheduleEvent e2 = schedule.getEvent(i);
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
            ScheduleEvent e1 = testEventList.get(i);
            ScheduleEvent e2 = schedule.getEvent(i);
            assertEquals(e1, e2);
        }
    }

    @Test
    void testSortShows() {
        Show showA = new Show("A");
        try {
            EventDate dateA = new EventDate(1, 1, 2021, 18, 30);
            showA.setStartDate(dateA);
        } catch (DateFormatException dfe) {
            fail("DateFormatException not expected");
        }
        schedule.addEvent(showA);
        schedule.addEvent(eventB);
        testEventList.add(showA);
        testEventList.add(eventB);

        assertTrue(schedule.getEvent(0) instanceof Show);
        assertTrue(schedule.getEvent(1) instanceof SimpleEvent);
    }

    @Test
    void testOverloadedConstructor() {
        Schedule schedule = new Schedule("My Schedule Name");
        assertEquals("My Schedule Name", schedule.getName());
    }
}