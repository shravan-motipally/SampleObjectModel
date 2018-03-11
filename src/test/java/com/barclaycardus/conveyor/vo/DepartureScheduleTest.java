package com.barclaycardus.conveyor.vo;

import com.barclaycardus.conveyor.exception.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepartureScheduleTest
{
    private DepartureSchedule schedule = DepartureSchedule.getInstance();

    @Before
    public void setup() throws ValidationException
    {
        schedule  = DepartureSchedule.getInstance();
        schedule.addFlight("AOE", "A1", "IAH", "01:00");
    }

    @Test (expected = ValidationException.class)
    public void shouldMeetRequirementsForFlightTime() throws ValidationException
    {
        schedule.addFlight("BOE777", "A1", "JFK", "invalid");
    }

    @Test
    public void shouldGetFlightIfAddedToScheduleWithDepartureTimeNull()
    {
        Assert.assertEquals(schedule.getFlightTerminal("AOE", null).toLowerCase(), "a1");
    }

}