package com.barclaycardus.conveyor.utils;

import com.barclaycardus.conveyor.exception.ValidationException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class BagTest
{
    @Test(expected = ValidationException.class)
    public void shouldExpectExceptionThrownAtBadBagId() throws ValidationException
    {
        Bag bag = new Bag("badInput", "-", "-");
    }
}