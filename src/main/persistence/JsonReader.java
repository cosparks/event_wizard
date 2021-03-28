package persistence;

import model.*;
import model.show.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.show.Employee;
import org.json.*;

// Represents a reader that reads schedule from JSON data stored in file
// CITATION: code copied and modified from JsonReader class in JsonSerializationDemo
//           GitHub: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads data from json object and returns schedule
    public Schedule read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSchedule(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses serialized json data and returns schedule
    private Schedule parseSchedule(JSONObject jsonObject) {
        Schedule schedule = new Schedule();
        addEvents(schedule, jsonObject);
        return schedule;
    }

    // EFFECTS: parses JsonArray representing events and calls method to parse and add events
    private void addEvents(Schedule schedule, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("events");
        for (Object json : jsonArray) {
            JSONObject nextEvent = (JSONObject) json;
            String type = nextEvent.getString("type");
            if (type.equals("show")) {
                addShow(schedule, nextEvent);
            } else {
                addSimpleEvent(schedule, nextEvent);
            }
        }
    }

    // MODIFIES: schedule
    // EFFECTS: parses show details from json object and adds show to schedule
    private void addShow(Schedule schedule, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Show show = new Show(name);
        addBasicEventDetails(show, jsonObject);

        show.setCapacity(jsonObject.getInt("capacity"));
        show.setProjectedSales(jsonObject.getInt("projected sales"));
        show.setTicketPrice(jsonObject.getInt("ticket price"));
        show.setAdditionalCost(jsonObject.getInt("additional cost"));
        addEmployees(show, jsonObject);
        addActs(show, jsonObject);
        addBar(show, jsonObject);
        schedule.addEvent(show);
    }

    // MODIFIES: schedule
    // EFFECTS: parses details of simple event from json object and adds event to schedule
    private void addSimpleEvent(Schedule schedule, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        SimpleEvent event = new SimpleEvent(name);
        addBasicEventDetails(event, jsonObject);

        addSimpleEventDetails(event, jsonObject);
        schedule.addEvent(event);
    }

    // MODIFIES: event
    // EFFECTS: parses json array of notes/details and adds them to simple event
    private void addSimpleEventDetails(SimpleEvent event, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("details");
        for (Object json : jsonArray) {
            String nextDetail = (String) json;
            event.addDetail(nextDetail);
        }
    }

    // MODIFIES: show
    // EFFECTS: parses json array representing employees and adds employees to show
    private void addEmployees(Show show, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("employees");
        for (Object json : jsonArray) {
            JSONObject nextEmployee = (JSONObject) json;
            show.addEmployee(getEmployee(nextEmployee));
        }
    }

    // EFFECTS: parses json object representing employee and returns employee
    private Employee getEmployee(JSONObject nextEmployee) {
        String name = nextEmployee.getString("name");
        int pay = nextEmployee.getInt("pay");

        Employee employee = new Employee(name, pay);
        employee.setJob(nextEmployee.getString("job"));

        return employee;
    }

    // MODIFIES: show
    // EFFECTS: parses json array representing employees and adds employees to show
    private void addActs(Show show, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("acts");
        for (Object json : jsonArray) {
            JSONObject nextAct = (JSONObject) json;
            show.addAct(getAct(nextAct));
        }
    }

    // EFFECTS: returns act parsed from json object
    private Act getAct(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int pay = jsonObject.getInt("pay");
        Act act = new Act(name, pay);

        act.setRider(jsonObject.getString("rider"));
        act.setStagePlot(jsonObject.getString("stage plot"));

        return act;
    }

    // MODIFIES: show
    // EFFECTS: parses json array representing list of bar items and adds bar items to show
    private void addBar(Show show, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("bar");
        for (Object json : jsonArray) {
            JSONObject nextDrink = (JSONObject) json;
            show.addDrink(getDrink(nextDrink));
        }
    }

    // EFFECTS: returns drink parsed from json object
    private Drink getDrink(JSONObject nextDrink) {
        String name = nextDrink.getString("name");
        int amount = nextDrink.getInt("amount");
        int cost = nextDrink.getInt("cost");

        Drink drink = new Drink(name, amount, cost);
        drink.setSalePrice(nextDrink.getInt("sale price"));

        return drink;
    }

    // MODIFIES: event
    // EFFECTS: parses event details from json object and adds them to event
    private void addBasicEventDetails(ScheduleEvent event, JSONObject jsonObject) {
        int importance = jsonObject.getInt("importance");
        String location = jsonObject.getString("location");
        EventDate startDate = getDate(jsonObject.get("start date"));

        event.setImportance(importance);
        event.setLocation(location);
        event.setStartDate(startDate);
    }

    // EFFECTS: returns EventDate parsed from json object
    private EventDate getDate(Object json) {
        JSONObject jsonDate = (JSONObject) json;
        int day = jsonDate.getInt("day");
        int month = jsonDate.getInt("month");
        int year = jsonDate.getInt("year");
        int hour = jsonDate.getInt("hour");
        int minute = jsonDate.getInt("minute");
        return new EventDate(day, month, year, hour, minute);
    }
}
