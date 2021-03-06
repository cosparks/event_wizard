package persistence;

import model.*;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

// CITATION: code taken and modified from JsonReaderTest class in JsonSerializationDemo
//           GitHub: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/terribleWeather.json");
        try {
            Schedule schedule = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptySchedule() {
        Schedule schedule = new Schedule();
        Schedule testSchedule;
        JsonWriter writer = new JsonWriter("./data/testEmptySchedule.json");
        try {
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testEmptySchedule.json");
            testSchedule = reader.read();
            assertTrue(schedule.getName().equals(testSchedule.getName()));
            assertEquals(0, testSchedule.getSize());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testReaderScheduleWithTwoShows() {
        JsonWriter writer = new JsonWriter("./data/testReaderWithTwoShows.json");

        Schedule schedule = new Schedule();
        Schedule testSchedule;
        Show showA = createShowA();
        Show showB = createShowB();
        schedule.addEvent(showA);
        schedule.addEvent(showB);

        try {
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testReaderWithTwoShows.json");
            testSchedule = reader.read();
            assertTrue(schedule.getName().equals(testSchedule.getName()));
            assertEquals(2, testSchedule.getSize());
            checkEvent(showA, testSchedule.getEvent(0));
            checkEvent(showB, testSchedule.getEvent(1));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testReaderScheduleWithTwoSimpleEvents() {
        JsonWriter writer = new JsonWriter("./data/testReaderWithTwoSimpleEvents.json");

        Schedule schedule = new Schedule();
        Schedule testSchedule = new Schedule();
        SimpleEvent eventA = createEventA();
        SimpleEvent eventB = createEventB();
        schedule.addEvent(eventA);
        schedule.addEvent(eventB);

        try {
            writer.open();
            writer.write(schedule);
            writer.close();

            JsonReader reader = new JsonReader("./data/testReaderWithTwoSimpleEvents.json");
            testSchedule = reader.read();
            assertTrue(schedule.getName().equals(testSchedule.getName()));
            assertEquals(2, testSchedule.getSize());
            checkEvent(eventA, testSchedule.getEvent(0));
            checkEvent(eventB, testSchedule.getEvent(1));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }
}
