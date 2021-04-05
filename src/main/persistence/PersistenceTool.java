package persistence;

import model.Schedule;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PersistenceTool {
    public static final String JSON_STORE = "./data/mySchedules/";

    public PersistenceTool() { }

    // MODIFIES: this
    // EFFECTS: retrieves and parses json object from JSON_STORE
    public Schedule loadSchedule(String jsonStore) throws IOException {
        Schedule schedule;
        JsonReader reader = new JsonReader(jsonStore);
        schedule = reader.read();

        return schedule;
    }

    // EFFECTS: writes file to json object and stores in destination folder
    public void saveSchedule(Schedule schedule, String destination) throws FileNotFoundException {
        JsonWriter writer = new JsonWriter(destination);

        writer.open();
        writer.write(schedule);
        writer.close();
    }
}
