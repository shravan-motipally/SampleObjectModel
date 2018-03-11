package com.barclaycardus.conveyor.utils;

import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.validation.RequirementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Route
{
    private static final Logger logger = LoggerFactory.getLogger(Route.class);
    private Terminal entry;
    private Terminal destination;
    private String fullRoute;
    private int travelTime;

    public Route(String _entry, String _destination, int _travelTime)
    {
        entry = new Terminal(_entry);
        destination = new Terminal(_destination);
        setFullRoute(_entry.toLowerCase() + " " + _destination.toLowerCase());
        travelTime = _travelTime;
    }

    public Terminal getEntry() { return entry; }
    public Terminal getDestination() { return destination; }
    public String getFullRoute() { return fullRoute; }
    public int getTravelTime() { return travelTime; }

    public void setFullRoute(String _fullRoute)
    {
        if (_fullRoute != null && !_fullRoute.isEmpty())
        {
            fullRoute = _fullRoute;
        }
    }

    public int hashCode()
    {
        if (entry.getId().isEmpty() || destination.getId().isEmpty())
            return 0;
        else
        {
            char[] charify = (entry.getId().toLowerCase() + destination.getId().toLowerCase()).toCharArray();
            int sum = 0;
            for (int i = 0; i < charify.length; i++)
            {
                sum += charify[i] * Math.pow(31, i);
            }
            return sum;
        }
    }

    public boolean equals(Object obj)
    {
        if (obj instanceof Route)
        {
            Route other = (Route) obj;
            if (other.getEntry().equals(entry) && other.getDestination().equals(destination))
                return true;
        }
        return false;
    }
}
