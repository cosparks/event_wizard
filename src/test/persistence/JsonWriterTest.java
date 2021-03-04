package persistence;

import model.*;
import model.show.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0bigBadFile:Name.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptySchedule() {
        try {
            Schedule schedule = new Schedule(); // creates schedule with name "My Schedule"
            JsonWriter writer = new JsonWriter("./data/testEmptySchedule.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptySchedule.json");
            schedule = reader.read();
            assertEquals("My Schedule", schedule.getName());
            assertEquals(0, schedule.getSize());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testWriterScheduleWithOneSimpleEvent() {
        try {
            Schedule schedule = new Schedule(); // creates schedule with name "My Schedule"
            SimpleEvent eventA = createEventA();
            schedule.addEvent(eventA);

            JsonWriter writer = new JsonWriter("./data/testWriterWithOneSimpleEvent.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterWithOneSimpleEvent.json");
            schedule = reader.read();

            assertEquals("My Schedule", schedule.getName());
            assertEquals(1, schedule.getSize());
            checkEvent(eventA, schedule.getEvent(0));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testWriterScheduleWithTwoSimpleEvents() {
        try {
            Schedule schedule = new Schedule(); // creates schedule with name "My Schedule"
            SimpleEvent eventA = createEventA();
            SimpleEvent eventB = createEventB();
            schedule.addEvent(eventA);
            schedule.addEvent(eventB);

            JsonWriter writer = new JsonWriter("./data/testWriterWithTwoSimpleEvents.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterWithTwoSimpleEvents.json");
            schedule = reader.read();

            assertEquals("My Schedule", schedule.getName());
            assertEquals(2, schedule.getSize());
            checkEvent(eventA, schedule.getEvent(0));
            checkEvent(eventB, schedule.getEvent(1));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testWriterScheduleWithOneShow() {
        try {
            Schedule schedule = new Schedule(); // creates schedule with name "My Schedule"
            Show showA = createShowA();
            schedule.addEvent(showA);

            JsonWriter writer = new JsonWriter("./data/testWriterWithShow.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterWithShow.json");
            schedule = reader.read();

            assertEquals("My Schedule", schedule.getName());
            assertEquals(1, schedule.getSize());
            checkEvent(showA, schedule.getEvent(0));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testWriterScheduleWithTwoShows() {
        try {
            Schedule schedule = new Schedule(); // creates schedule with name "My Schedule"
            Show showA = createShowA();
            Show showB = createShowB();
            schedule.addEvent(showA);
            schedule.addEvent(showB);

            JsonWriter writer = new JsonWriter("./data/testWriterWithTwoShows.json");
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterWithTwoShows.json");
            schedule = reader.read();

            assertEquals("My Schedule", schedule.getName());
            assertEquals(2, schedule.getSize());
            checkEvent(showA, schedule.getEvent(0));
            checkEvent(showB, schedule.getEvent(1));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    private void checkEvent(SimpleEvent eventA, Event eventFromReader) {
        SimpleEvent eventB = (SimpleEvent) eventFromReader;
        checkBasicDetails(eventA, eventB);
        for (int i = 0; i < eventA.getNumberOfDetails(); i++) {
            assertTrue(eventA.getDetail(i).equals(eventB.getDetail(i)));
        }
    }

    private void checkEvent(Show showA, Event eventFromReader) {
        Show showB = (Show) eventFromReader;
        checkBasicDetails(showA, showB);
        assertEquals(showA.getCapacity(), showB.getCapacity());
        assertEquals(showA.getAdditionalCost(), showB.getAdditionalCost());
        assertEquals(showA.getProjectedSales(), showB.getProjectedSales());
        assertEquals(showA.getCapacity(), showB.getCapacity());
        assertEquals(showA.getTicketPrice(), showB.getTicketPrice());

        for (int i = 0; i < showA.getNumDrinks(); i++) {
            assertTrue(showA.getDrink(i).equals(showB.getDrink(i)));
        }
        for (int i = 0; i < showA.getNumEmployees(); i++) {
            assertTrue(showA.getEmployee(i).equals(showB.getEmployee(i)));
        }
        for (int i = 0; i < showA.getNumActs(); i++) {
            assertTrue(showA.getAct(i).equals(showB.getAct(i)));
        }
    }

    private void checkBasicDetails(Event eventA, Event eventB) {
        assertEquals(eventA.getName(), eventB.getName());
        assertEquals(eventA.getLocation(), eventB.getLocation());
        assertEquals(eventA.getImportance(), eventB.getImportance());
        checkEventDates(eventA, eventB);
    }

    private void checkEventDates(Event eventA, Event eventB) {
        String dateA = eventA.getStartDate().getDateAsString();
        String dateB = eventB.getStartDate().getDateAsString();
        assertTrue(dateA.equals(dateB));
    }

    private SimpleEvent createEventA() {
        SimpleEvent event = new SimpleEvent("Appointment 52");
        EventDate startDate = new EventDate(15, 5, 2021, 9, 0);
        for (int i = 1; i <= 3; i++) {
            event.addDetail("detail " + i);
        }
        event.setStartDate(startDate);
        event.setLocation("250 fatcat lane");
        event.setImportance(5);
        return event;
    }

    private SimpleEvent createEventB() {
        SimpleEvent event = new SimpleEvent("Surfing in Tofino");
        EventDate startDate = new EventDate(17, 7, 2021, 15, 30);
        for (int i = 1; i <= 3; i++) {
            event.addDetail("surfing detail " + i);
        }
        event.setStartDate(startDate);
        event.setLocation("333 Beach Avenue");
        event.setImportance(9);
        return event;
    }

    private Show createShowA() {
        Show show = new Show("Chad VanGaalen with guests");
        EventDate startDate = new EventDate(21, 3, 2021, 21, 25);
        show.setStartDate(startDate);
        show.setLocation("The Rio Theatre");
        show.setCapacity(300);
        show.setTicketPrice(22);
        show.setProjectedSales(245);
        show.setAdditionalCost(500);

        Drink drinkA = new Drink("Beer", 1, 2);
        drinkA.setSalePrice(3);
        Drink drinkB = new Drink("Craft Beer", 4, 5);
        drinkB.setSalePrice(6);
        show.addDrink(drinkA);
        show.addDrink(drinkB);

        Employee employeeA = new Employee("John", 195);
        employeeA.setJob("sound");
        Employee employeeB = new Employee("Sarah", 180);
        employeeB.setJob("lighting");
        show.addEmployee(employeeA);
        show.addEmployee(employeeB);

        Act actA = new Act("Bratboy", 300);
        Act actB = new Act("Amyl", 1000);
        show.addAct(actA);
        show.addAct(actB);

        return show;
    }

    private Show createShowB() {
        Show show = new Show("Last run of the Decepticons");
        EventDate startDate = new EventDate(07, 9, 2021, 19, 15);
        show.setStartDate(startDate);
        show.setLocation("Pacific Spirit Park");
        show.setCapacity(1000);
        show.setTicketPrice(19);
        show.setProjectedSales(667);
        show.setAdditionalCost(775);

        Drink drinkA = new Drink("Beer", 500, 3);
        drinkA.setSalePrice(6);
        Drink drinkB = new Drink("Craft Beer", 750, 4);
        drinkB.setSalePrice(8);
        show.addDrink(drinkA);
        show.addDrink(drinkB);

        Employee employeeA = new Employee("Colby", 335);
        employeeA.setJob("sound");
        Employee employeeB = new Employee("Jacinda", 5000);
        employeeB.setJob("lighting");
        show.addEmployee(employeeA);
        show.addEmployee(employeeB);

        Act actA = new Act("Pragmatics", 500);
        Act actB = new Act("Warp Horde", 500);
        show.addAct(actA);
        show.addAct(actB);

        return show;
    }
}
