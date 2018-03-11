package com.barclaycardus.conveyor.vo;

import com.barclaycardus.conveyor.exception.NotFoundException;
import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.utils.Bag;
import com.barclaycardus.conveyor.utils.Route;
import com.barclaycardus.conveyor.validation.RequirementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.LinkedList;

public class Bagger
{
    private static final Logger logger = LoggerFactory.getLogger(Bagger.class);
    private static Bagger instance;
    private static final Object key = new Object();

    private static LinkedList<Bag> bags; //FIFO queue.

    public static Bagger getInstance()
    {
        if (instance == null){
            synchronized(key){
                if (instance == null){
                    instance = new Bagger();
                }
            }
        }
        return instance;
    }

    private Bagger()
    {
        initializeListOfBags();
    }

    private void initializeListOfBags()
    {
        bags = new LinkedList<Bag>();
    }

    public void addBag(String bagId, String entryPoint, String flightId) throws NotFoundException, ValidationException
    {
        if (!RequirementValidator.isBagIdAcceptable(bagId) ||
                !RequirementValidator.isTerminalAcceptable(entryPoint) ||
                !RequirementValidator.isFlightIdAcceptable(flightId))
        {
            logger.error("BagId | entryPoint | flightId failed requirement validation");
            throw new ValidationException("Bag creation failed due to bad input");
        }

        DepartureSchedule schedule = DepartureSchedule.getInstance();
        String destination = flightId.toLowerCase().equals("arrival") ?
                "baggageclaim" : schedule.getFlightTerminal(flightId, null);

        if (destination == null)
        {//Could not find the flight
            //routed to baggageclaim.
            throw new NotFoundException("FLIGHT: " + flightId + " not found.");
        }

        Bag bag = new Bag(bagId, entryPoint, destination);
        bags.add(bag);
    }

    public static void printOptimalRoutes(Conveyor conveyor)
    {
        Iterator<Bag> bagIterator = bags.iterator();
        while (bagIterator.hasNext())
        {
            Bag currentBag = bagIterator.next();
            try
            {
                Route minimumRoute = conveyor.getOptimalRoute(currentBag.getEntry().getId(), currentBag.getDestination().getId());
                System.out.println(currentBag.getBagId() + " " + minimumRoute.getFullRoute() + " : " + minimumRoute.getTravelTime());
            }
            catch (NotFoundException notFound)
            {
                logger.error("SEVERE: logic error.");
                throw new NotImplementedException();
            }
        }
    }
}
