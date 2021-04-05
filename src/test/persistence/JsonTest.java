package persistence;

import exceptions.DateFormatException;
import model.*;

import model.show.Act;
import model.show.Drink;
import model.show.Employee;
import ui.texttools.TextColors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class JsonTest {

    protected void checkEvent(SimpleEvent eventA, ScheduleEvent eventFromReader) {
        SimpleEvent eventB = (SimpleEvent) eventFromReader;
        checkBasicDetails(eventA, eventB);
        for (int i = 0; i < eventA.getNumberOfDetails(); i++) {
            assertTrue(eventA.getDetail(i).equals(eventB.getDetail(i)));
        }
    }

    protected void checkEvent(Show showA, ScheduleEvent eventFromReader) {
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

    protected void checkBasicDetails(ScheduleEvent eventA, ScheduleEvent eventB) {
        assertEquals(eventA.getName(), eventB.getName());
        assertEquals(eventA.getLocation(), eventB.getLocation());
        assertEquals(eventA.getImportance(), eventB.getImportance());
        checkEventDates(eventA, eventB);
    }

    protected void checkEventDates(ScheduleEvent eventA, ScheduleEvent eventB) {
        String dateA = eventA.getStartDate().getDateAsString();
        String dateB = eventB.getStartDate().getDateAsString();
        assertTrue(dateA.equals(dateB));
    }

    protected SimpleEvent createEventA() {
        SimpleEvent event = new SimpleEvent("Appointment 52");
        try {
            EventDate startDate = new EventDate(15, 5, 2021, 9, 0);
            event.setStartDate(startDate);
        } catch (DateFormatException e) {
            System.out.println(TextColors.QUIT + "Error: Unable to set date due to DateFormatException");
        }

        for (int i = 1; i <= 3; i++) {
            event.addDetail("detail " + i);
        }

        event.setLocation("250 fatcat lane");
        event.setImportance(5);
        return event;
    }

    protected SimpleEvent createEventB() {
        SimpleEvent event = new SimpleEvent("Surfing in Tofino");
        try {
            EventDate startDate = new EventDate(17, 7, 2021, 15, 30);
            event.setStartDate(startDate);
        } catch (DateFormatException e) {
            System.out.println(TextColors.QUIT + "Error: Unable to set date due to DateFormatException");
        }

        for (int i = 1; i <= 3; i++) {
            event.addDetail("surfing detail " + i);
        }

        event.setLocation("333 Beach Avenue");
        event.setImportance(9);
        return event;
    }

    protected Show createShowA() {
        Show show = new Show("Chad VanGaalen with guests");
        try {
            EventDate startDate = new EventDate(21, 3, 2021, 21, 25);
            show.setStartDate(startDate);
        } catch (DateFormatException e) {
            System.out.println(TextColors.QUIT + "Error: Unable to set date due to DateFormatException");
        }

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

    protected Show createShowB() {
        Show show = new Show("Last run of the Decepticons");
        try {
            EventDate startDate = new EventDate(07, 9, 2021, 19, 15);
            show.setStartDate(startDate);
        } catch (DateFormatException e) {
            System.out.println(TextColors.QUIT + "Error: Unable to set date due to DateFormatException");
        }

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
