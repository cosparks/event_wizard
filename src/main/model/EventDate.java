package model;

import java.util.ArrayList;

// Represents a date with day, month, year and time of day
public class EventDate {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    // EFFECTS: Constructs a new date with empty fields
    public EventDate() {

    }

    // REQUIRES: day [1, 31], month [1, 12], year [1000, 9999], hour [0, 23], minute [0, 59]
    // EFFECTS: Constructs a new date with assigned fields
    public EventDate(int day, int month, int year, int hour, int minute) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    // REQUIRES: day [1, 31], month [1, 12], year [1000, 9999], hour [0, 23], minute [0, 59]
    // EFFECTS: erases date and replaces with new values
    public void setDate(int day, int month, int year, int hour, int minute) {
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
