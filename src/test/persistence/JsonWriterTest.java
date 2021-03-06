package persistence;

import model.*;
import model.show.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;


// CITATION: code taken and modified from JsonWriterTest class in JsonSerializationDemo
//           GitHub: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonWriterTest extends JsonTest {

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
}
