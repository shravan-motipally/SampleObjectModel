package com.barclaycardus.conveyor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationTest
{
    @Test
    public void shouldThrowNotThrowIOExceptionIfFileNotValid() throws Exception
    {
        Application.main(new String[]{"bad input file"});
    }

}