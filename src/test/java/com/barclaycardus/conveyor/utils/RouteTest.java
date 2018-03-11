package com.barclaycardus.conveyor.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RouteTest {

    @Test
    public void shouldExpectTwoRoutesToBeDifferentIfTheyAreInReverse()
    {
        Route route1 = new Route("A1", "A2", 1);
        Route route2 = new Route("A2", "A1", 1);
        Assert.assertNotEquals(route1, route2);
        Assert.assertNotEquals(route1.hashCode(), route2.hashCode());
    }

    @Test
    public void shouldExpectTwoRoutesWithSameTerminalsDifferentTravelTimesToBeEqual()
    {
        Route route1 = new Route("A1", "A2", 1);
        Route route2 = new Route("A1", "A2", 2);
        Assert.assertEquals(route1, route2);
        Assert.assertEquals(route1.hashCode(), route2.hashCode());
    }


}