package model;

import exceptions.DateFormatException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// Represents a date with day, month, year and time of day
public class EventDate implements Writable {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    // EFFECTS: Constructs a new date with empty fields
    public EventDate() {

    }

    // EFFECTS: checks validity of input date, then constructs a new date with assigned fields
    //          throws DateFormatException if date is invalid
    public EventDate(int day, int month, int year, int hour, int minute) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);

        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("day", day);
        json.put("month", month);
        json.put("year", year);
        json.put("hour", hour);
        json.put("minute", minute);
        return json;
    }

    // MODIFIES: this
    // EFFECTS: checks validity of new date values, then updates according to those values;
    //          throws DateFormatException if date is invalid
    public void setDate(int day, int month, int year, int hour, int minute) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);

        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    // EFFECTS: generates date as string of form: YYYY-MM-DD-HH:MM
    public String getDateAsString() {
        String insertA = (month < 10) ? "-0" : "-";
        String insertB = (day < 10) ? "-0" : "-";
        String insertC = (hour < 10) ? "-0" : "-";
        String insertD = (minute < 10) ? ":0" : ":";
        return year + insertA + month + insertB + day + insertC + hour + insertD + minute;
    }

    // EFFECTS: returns start time as string
    public String getTimeForDisplay() {
        String insertA = (hour < 10) ? "0" : "";
        String insertB = (minute < 10) ? "0" : "";
        return insertA + hour + ":" + insertB + minute;
    }

    // EFFECTS: returns date as string DD/MM/YYYY
    public String getDateForDisplay() {
        String insertA = (day < 10) ? "0" : "";
        String insertB = (month < 10) ? "/0" : "/";
        return  insertA + day + insertB + month + "/" + year;
    }

    // EFFECTS: checks validity of all parameters--throws DateFormatException if day is >31 or <1, month
    //          is >12 or <1, year is >3000 or <1900, hour is >23 or <0, or if minute is >59 or <1
    private void verifyDateValues(int day, int month, int year, int hour, int minute) throws DateFormatException {
        boolean throwException = false;
        if (day > 31 || day < 1) {
            throwException = true;
        } else if (month > 12 || month < 1) {
            throwException = true;
        } else if (year < 1900 || year > 3000) {
            throwException = true;
        } else if (hour < 0 || hour > 23) {
            throwException = true;
        } else if (minute < 0 || minute > 59) {
            throwException = true;
        }

        if (throwException) {
            throw new DateFormatException();
        }
    }

    public int getDay() {
        return day;
    }

    // MODIFIES: this
    // EFFECT: sets day of this, throws DateFormatException if day is not valid
    public void setDay(int day) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    // MODIFIES: this
    // EFFECT: sets month of this, throws DateFormatException if day is not valid
    public void setMonth(int month) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    // MODIFIES: this
    // EFFECT: sets year of this, throws DateFormatException if day is not valid
    public void setYear(int year) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    // MODIFIES: this
    // EFFECT: sets hour of this, throws DateFormatException if day is not valid
    public void setHour(int hour) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    // MODIFIES: this
    // EFFECT: sets minute of this, throws DateFormatException if day is not valid
    public void setMinute(int minute) throws DateFormatException {
        verifyDateValues(day, month, year, hour, minute);
        this.minute = minute;
    }
}
