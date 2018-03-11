package com.barclaycardus.conveyor.vo;

import com.barclaycardus.conveyor.exception.NotFoundException;
import com.barclaycardus.conveyor.exception.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class ConveyorTest
{
    private Conveyor empty;
    private Conveyor single_node;
    private Conveyor single_path;
    private Conveyor single_path_length_zero;
    private Conveyor straight_path;
    private Conveyor cyclic_path;
    private Conveyor graph_with_neg_edges;
    @Before
    public void setup() throws ValidationException
    {
        empty = new Conveyor();

        single_node = new Conveyor();
        single_node.addRoute("A", "A", 0);

        single_path = new Conveyor();
        single_path.addRoute("A", "B", 1);

        single_path_length_zero = new Conveyor();
        single_path_length_zero.addRoute("A", "B", 0);

        straight_path = new Conveyor();
        straight_path.addRoute("A", "B", 1);
        straight_path.addRoute("B", "C", 2);
        straight_path.addRoute("C", "D", 3);

        cyclic_path = new Conveyor();
        cyclic_path.addRoute("A", "B", 2);
        cyclic_path.addRoute("A", "C", 1);
        cyclic_path.addRoute("B", "C", 1);
        cyclic_path.addRoute("B", "D", 3);

        graph_with_neg_edges = new Conveyor();
        graph_with_neg_edges.addRoute("B", "A", 1);

    }

    @Test(expected = ValidationException.class)
    public void cannotAddNullTerminals() throws ValidationException
    {
        new Conveyor().addTerminal(null);
    }

    @Test(expected = ValidationException.class)
    public void cannotAddNegativeTravelTimes() throws ValidationException
    {
        graph_with_neg_edges.addRoute("A", "B", -2);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowNotFoundExceptionWithEmptyGraph() throws NotFoundException, ValidationException
    {
        empty.getOptimalRoute("nil", "nul");
        fail("Optimal Route works on empty graph - when it shouldn't");
    }

    @Test
    public void shouldNotFindOptimalRouteWithOneNodeCycle() throws NotFoundException, ValidationException
    {
        Assert.assertNull(single_node.getOptimalRoute("A", "A"));
    }

    @Test
    public void shouldFindOptimalRouteWithReverseEntryDestination() throws ValidationException, NotFoundException
    {
        Assert.assertEquals(single_path.getOptimalRoute("B", "A").getTravelTime(), 1);
    }

    @Test
    public void distanceShouldNotMatterWithOptimalRoute() throws ValidationException, NotFoundException
    {
        Assert.assertEquals(single_path_length_zero.getOptimalRoute("A", "B").getTravelTime(), 0);
    }

    @Test
    public void shouldFindOptimalRouteWithMoreThanOneRoute() throws ValidationException, NotFoundException
    {
        Assert.assertEquals(straight_path.getOptimalRoute("A", "D").getTravelTime(), 6);
    }

    @Test
    public void cyclesShouldNotAffectOptimalRoute() throws ValidationException, NotFoundException
    {
        Assert.assertEquals(cyclic_path.getOptimalRoute("D", "B").getTravelTime(), 3);
    }

}