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
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    public Schedule read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSchedule(jsonObject);
    }

    // TODO read up on this method to understand what it does
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    private Schedule parseSchedule(JSONObject jsonObject) {
        // String name = jsonObject.getString("name");
        Schedule schedule = new Schedule();
        addEvents(schedule, jsonObject);
        return schedule;
    }

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

    // EFFECTS: parses show details from json object and adds them to new show
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


    private void addSimpleEvent(Schedule schedule, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        SimpleEvent event = new SimpleEvent(name);
        addBasicEventDetails(event, jsonObject);

        addSimpleEventDetails(event, jsonObject);
        schedule.addEvent(event);
    }


    private void addSimpleEventDetails(SimpleEvent event, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("details");
        for (Object json : jsonArray) {
            String nextDetail = (String) json;
            event.addDetail(nextDetail);
        }
    }

    private void addEmployees(Show show, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("employees");
        for (Object json : jsonArray) {
            JSONObject nextEmployee = (JSONObject) json;
            show.addEmployee(getEmployee(nextEmployee));
        }
    }

    private Employee getEmployee(JSONObject nextEmployee) {
        String name = nextEmployee.getString("name");
        int pay = nextEmployee.getInt("pay");

        Employee employee = new Employee(name, pay);
        employee.setJob(nextEmployee.getString("job"));

        return employee;
    }

    private void addActs(Show show, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("acts");
        for (Object json : jsonArray) {
            JSONObject nextAct = (JSONObject) json;
            show.addAct(getAct(nextAct));
        }
    }

    private Act getAct(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int pay = jsonObject.getInt("pay");
        Act act = new Act(name, pay);

        act.setRider(jsonObject.getString("rider"));
        act.setStagePlot(jsonObject.getString("stage plot"));

        return act;
    }

    private void addBar(Show show, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("bar");
        for (Object json : jsonArray) {
            JSONObject nextDrink = (JSONObject) json;
            show.addDrink(getDrink(nextDrink));
        }
    }

    private Drink getDrink(JSONObject nextDrink) {
        String name = nextDrink.getString("name");
        int amount = nextDrink.getInt("amount");
        int cost = nextDrink.getInt("cost");

        Drink drink = new Drink(name, amount, cost);
        drink.setSalePrice(nextDrink.getInt("sale price"));

        return drink;
    }


    // EFFECTS: parses event details from json object and adds them to event
    private void addBasicEventDetails(Event event, JSONObject jsonObject) {
        int importance = jsonObject.getInt("importance");
        String location = jsonObject.getString("location");
        EventDate startDate = getDate(jsonObject.get("start date"));

        event.setImportance(importance);
        event.setLocation(location);
        event.setStartDate(startDate);
    }

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
