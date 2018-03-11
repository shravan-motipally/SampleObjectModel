package com.barclaycardus.conveyor.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalTest {
    @Test //Bad design decision to not raise exception on validation.
    public void shouldNotExpectTerminalToRaiseExceptionOnSetId()
    {
        try {
            Terminal nullTerminal = new Terminal(null);//this is ok.
        }
        catch (Exception e)
        {
            fail("Exception caught when none expected.");
        }
    }

}