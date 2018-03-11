package com.barclaycardus.conveyor.vo;

import com.barclaycardus.conveyor.exception.NotFoundException;
import com.barclaycardus.conveyor.exception.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaggerTest {
    private DepartureSchedule schedule;
    private Bagger bagger;

    @Before
    public void setup() throws ValidationException, NotFoundException
    {
        schedule = DepartureSchedule.getInstance();
        bagger = Bagger.getInstance();
        schedule.addFlight("FL0", "A0", "A1", "00:00");
    }

    @Test
    public void shouldInitializeOnStartup()
    {
        Assert.assertNotNull(Bagger.getInstance());
    }

    @Test (expected = NotFoundException.class)
    public void shouldNotFindFlightIfNotInDepartureSchedule() throws NotFoundException, ValidationException
    {
        schedule.addFlight("FL1", "A1", "A2", "01:00");
        bagger.addBag("1", "A1", "does not exist");
    }

    @Test
    public void shouldFindFlightIfInDepartureSchedule() throws NotFoundException, ValidationException
    {
        schedule.addFlight("FL2", "A2", "A3", "02:00");
        bagger.addBag("2", "A2", "FL2");
    }

    @Test
    public void shouldFindARRIVAL() throws NotFoundException, ValidationException
    {
        bagger.addBag("3", "A0", "ARRIVAL");
    }
}