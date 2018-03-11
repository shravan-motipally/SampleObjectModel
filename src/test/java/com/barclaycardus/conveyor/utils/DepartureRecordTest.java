package com.barclaycardus.conveyor.utils;

import com.barclaycardus.conveyor.exception.ValidationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DepartureRecordTest
{
    @Test(expected = ValidationException.class)
    public void shouldExpectExceptionThrownOnBadTimeInput() throws ValidationException
    {
        DepartureRecord dr = new DepartureRecord("any", "either", "not in a 00:00 format");
    }

}