package com.barclaycardus.conveyor.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;


public class RequirementValidator
{
    private static final Logger logger = LoggerFactory.getLogger(RequirementValidator.class);

    public static boolean isTerminalAcceptable(String str)
    {
        if (str == null || str.isEmpty())
            return false;
        return true;
    }

    public static boolean isTravelTimeAcceptable(int travelTime)
    {
        if (travelTime >= 0)
            return true;
        return false;
    }

    public static boolean isDepartureDestinationAcceptable(String str)
    {
        if (str == null || str.isEmpty())
            return false;
        return true;
    }

    public static boolean isDepartureTimeAcceptable(String depTime)
    {
        if (depTime == null || depTime.isEmpty())
            return false;
        Pattern militaryTimePattern = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d$");
        return militaryTimePattern.matcher(depTime).matches();
    }

    public static boolean isFlightIdAcceptable(String flightId)
    {
        if (flightId == null || flightId.isEmpty())
            return false;
        return true;
    }

    public static boolean isBagIdAcceptable(String bagId)
    {
        if (bagId == null || bagId.isEmpty())
            return false;
        Pattern allNums = Pattern.compile("^\\d*$");
        return allNums.matcher(bagId).matches();
    }

}
