package com.barclaycardus.conveyor.utils;

import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.validation.RequirementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DepartureRecord
{
    private static final Logger logger = LoggerFactory.getLogger(DepartureRecord.class);
    private Terminal departureTerminal;
    private String departureDestination;
    private String departureTime;

    public DepartureRecord(String terminal, String _departureDestination, String _departureTime) throws ValidationException
    {
        setDepartureTerminal(terminal);
        setDepartureDestination(_departureDestination);
        setDepartureTime(_departureTime);
    }

    public Terminal getDepartureTerminal()
    {
        return departureTerminal;
    }

    public void setDepartureTerminal(String terminal) throws ValidationException
    {
        if (!RequirementValidator.isTerminalAcceptable(terminal))
        {
            logger.error("Terminal id failed requirements validation");
            throw new ValidationException("Invalid terminal given");
        }
        departureTerminal =  new Terminal(terminal);
    }

    public String getDepartureDestination()
    {
        return departureDestination;
    }

    public void setDepartureDestination(String _departureDest) throws ValidationException
    {
        if (!RequirementValidator.isDepartureDestinationAcceptable(_departureDest))
        {
            logger.error("Departure destination failed requirement validation");
            throw new ValidationException("Invalid departure destination entered");
        }
        departureDestination = _departureDest;
    }

    public String getDepartureTime()
    {
        return departureTime;
    }

    public void setDepartureTime(String _depTime) throws ValidationException
    {
        if (!RequirementValidator.isDepartureTimeAcceptable(_depTime))
        {
            logger.error("Departure time failed requirement validation");
            throw new ValidationException("Invalid departure time ");
        }
        departureTime = _depTime;
    }
}
