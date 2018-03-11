package com.barclaycardus.conveyor.vo;

import com.barclaycardus.conveyor.exception.NotFoundException;
import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.utils.DepartureRecord;
import com.barclaycardus.conveyor.validation.RequirementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DepartureSchedule
{
    private static final Logger logger = LoggerFactory.getLogger(DepartureSchedule.class);
    private Map<String, ArrayList<DepartureRecord>> flightSchedule;
    private static final Object key = new Object();
    private static DepartureSchedule departureScheduleInstance = getInstance();
    private static int size = 0;
    private DepartureSchedule()
    {
        initializeFlightSchedule();
    }

    private void initializeFlightSchedule()
    {
        flightSchedule = new TreeMap<String, ArrayList<DepartureRecord>>(String::compareTo);
    }

    public static DepartureSchedule getInstance()
    {
        if (departureScheduleInstance == null){
            synchronized(key){
                if (departureScheduleInstance == null){
                    departureScheduleInstance = new DepartureSchedule();
                }
            }
        }
        return departureScheduleInstance;
    }

    public void addFlight(String flight, String terminal, String destination, String time) throws ValidationException
    {
        DepartureRecord record = new DepartureRecord(terminal, destination, time);


        if (flightSchedule == null)
        {
            initializeFlightSchedule();
        }
        else if (!RequirementValidator.isFlightIdAcceptable(flight.toLowerCase()))
        {
            logger.error("Flight id failed requirement validation");
            throw new ValidationException("Invalid input for adding flight");
        }

        if (flightSchedule.containsKey(flight.toLowerCase()))
        {
            flightSchedule.get(flight.toLowerCase()).add(record);
        }
        else
        {
            ArrayList<DepartureRecord> records = new ArrayList<DepartureRecord>();
            records.add(record);
            flightSchedule.put(flight.toLowerCase(), records);        }
    }

    private List<DepartureRecord> getDepartureRecords(String flight)
    {
        if (!flightSchedule.containsKey(flight.toLowerCase()))
            return null;
        else
            return flightSchedule.get(flight.toLowerCase());
    }

    /***
     *
     * @param flightId flight id needed for the terminal
     * @param departureTime if null, @returns first flight terminal found
     * @return
     */
    public String getFlightTerminal(String flightId, String departureTime)
    {
        if (flightId == null || flightId.isEmpty())
        {
            logger.info("Flight input empty");
            return null;
        }
        List<DepartureRecord> records = getDepartureRecords(flightId);
        if (departureTime == null || departureTime.isEmpty())
        {//use first
            if (records != null)
                return records.get(0).getDepartureTerminal().getId();
            return null;
        }
        if (records != null)
        {
            for (DepartureRecord record : records)
            {
                if (record.getDepartureTime().equals(departureTime))
                    return record.getDepartureTerminal().getId();
            }

        }
        return null;
    }

}
