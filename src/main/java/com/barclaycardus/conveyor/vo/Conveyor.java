package com.barclaycardus.conveyor.vo;

import com.barclaycardus.conveyor.exception.NotFoundException;
import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.validation.RequirementValidator;
import com.barclaycardus.conveyor.utils.Route;
import com.barclaycardus.conveyor.utils.Terminal;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Conveyor
{
    private static final Logger logger = LoggerFactory.getLogger(Conveyor.class);
    private LinkedHashSet<Terminal> terminals;
    private LinkedHashSet<Route> routes;
    private int[][] currentTravelSchedule;

    public Conveyor()
    {
        terminals = new LinkedHashSet<Terminal>();
        routes = new LinkedHashSet<Route>();
    }

    public void addTerminal(String terminal) throws ValidationException
    {
        if (!RequirementValidator.isTerminalAcceptable(terminal))
        {
            logger.error("Terminal id failed requirement validation");
            throw new ValidationException("Invalid input for adding terminal");
        }
        Terminal tempTerminal = new Terminal(terminal.toLowerCase());
        if (terminals == null)
            terminals = new LinkedHashSet<Terminal>();

        if (!terminals.contains(tempTerminal))
            terminals.add(tempTerminal);
    }

    public void addRoute(String entry, String destination, int travelTime) throws ValidationException
    {
        if (!RequirementValidator.isTerminalAcceptable(entry) ||
                !RequirementValidator.isTerminalAcceptable(destination) ||
                !RequirementValidator.isTravelTimeAcceptable(travelTime))
        {
            logger.error("Route failed requirement validation");
            throw new ValidationException(" Invalid input for adding route");
        }
        Route tempRoute = new Route(entry.toLowerCase(), destination.toLowerCase(), travelTime);
        Route revRoute = new Route(destination.toLowerCase(), entry.toLowerCase(), travelTime);

        addTerminal(entry.toLowerCase());
        addTerminal(destination.toLowerCase());

        if (routes == null)
            routes = new LinkedHashSet<Route>();

        boolean routeExists = routes.contains(tempRoute);
        boolean revRouteExists = routes.contains(revRoute);
        if (!routeExists && !revRouteExists)
        {//Case #1: adding both routes with same travel time if route does not exist
            routes.add(tempRoute);
            routes.add(revRoute);
        }
        else if (routeExists && revRouteExists)
        {//Case #2: re-add first route only.
            routes.add(tempRoute);
        }
    }

    private boolean containsTerminal(String terminal)
    {
        return terminals.contains(new Terminal(terminal.toLowerCase()));
    }

    private boolean isConnected(String entry, String destination)
    {
        return routes.contains(new Route(entry, destination, -1)) ||
                routes.contains(new Route(destination, entry, -1));
    }

    private List<Route> getConnectedRoutes(String entry, List<Terminal> restrictedTerminals)
    {
        Iterator<Route> rIterator = routes.iterator();
        List<Route> attachedRoutes = new LinkedList<Route>();
        while (rIterator.hasNext())
        {
            Route route = rIterator.next();
            if (route.getEntry().getId().toLowerCase().equals(entry.toLowerCase()))
            {
                if (restrictedTerminals == null || !restrictedTerminals.contains(route.getDestination()))
                    attachedRoutes.add(route);
            }
        }
        //return attachedRoutes.isEmpty() ? null : attachedRoutes;
        return attachedRoutes;
    }

    public Route getOptimalRoute(String entry, String destination) throws NotFoundException
    {
        if (!containsTerminal(entry) || !containsTerminal(destination))
        {
            logger.error("Terminal does not exist. entry: " + entry + " destination: " + destination);
            throw new NotFoundException("Terminal not found to compute distance.");
        }
        List<Terminal> visitedTerminals = new LinkedList<Terminal>();
        visitedTerminals.add(new Terminal(entry));
        List<Route> connectingRoutes = getConnectedRoutes(entry, visitedTerminals);
        if (connectingRoutes == null || connectingRoutes.isEmpty()) //if entry point is disconnected
            return  null;

        Comparator<Route> routeComparator = Comparator.comparing(Route::getTravelTime);
        PriorityQueue<Route> queue = new PriorityQueue<Route> (routeComparator);
        PriorityQueue<Route> allPaths = new PriorityQueue<Route> (routeComparator);

        queue.addAll(connectingRoutes);
        while (!queue.isEmpty())
        {
            Route currentRoute = queue.poll();
            if (!currentRoute.getDestination().getId().toLowerCase().equals(destination.toLowerCase()))
            {
                visitedTerminals.add(currentRoute.getDestination());
                for (Route route : getConnectedRoutes(currentRoute.getDestination().getId(), visitedTerminals))
                {
                    Route temp = new Route(route.getEntry().getId(), route.getDestination().getId(), route.getTravelTime() + currentRoute.getTravelTime());
                    temp.setFullRoute(currentRoute.getFullRoute() + " " + route.getDestination().getId());
                    queue.add(temp);
                }
            }
            else
                allPaths.add(currentRoute);
        }

        if (allPaths.isEmpty()) //if destination is disconnected
            return null;

        Route minRoute = new Route(allPaths.peek().getEntry().getId(), allPaths.peek().getDestination().getId(), allPaths.peek().getTravelTime());
        minRoute.setFullRoute(allPaths.peek().getFullRoute());
        return minRoute;
        /**
         * Algorithm:
         * visitedTerminals.add(entry) [visit entry]
         * getConnectngRoutes(entry, visitedTerminals) [add connecting routes to priority queue]
         * putInQueue(connectingRoutes)
         *
         *
         * while (Queue is not empty) [while queue is not empty, dequeue, and visit each route]
         *      Route rt = dequeue();
         *
         *      visit(rt, Queue, rt.travelTime, visitedTerminals)
         *
         *
         * visit(rt, Queue, accumulatedTravelTime, visitedTerminals, finalDest)
         *
         *   if (rt.dest == finalDest)
         *      add to finalPathsList
         *   else
         *      visitedTerminals.add(rt.dest)
         *
         *   Queue.add(getConnectedRoutes(rt.dest), visitedTerminals)  -
         *      for each route r connecting (Queue.add(new Route(r.entry, r.dest, r.travelTime + accumulatedTravelTime).setFullRoute(rt.fullRoute + r.dest))
         *
         */
    }


}
