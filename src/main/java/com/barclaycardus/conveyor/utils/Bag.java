package com.barclaycardus.conveyor.utils;

import com.barclaycardus.conveyor.exception.ValidationException;
import com.barclaycardus.conveyor.validation.RequirementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bag
{
    private static final Logger logger = LoggerFactory.getLogger(Bag.class);

    private String bagId;
    private Terminal entry;
    private Terminal destination;

    public Bag(String _bagId, String _entry, String _destination) throws ValidationException
    {
        setBagId(_bagId);
        setEntry(_entry);
        setDestination(_destination);
    }

    private void setBagId(String _bagId) throws ValidationException
    {
        if (!RequirementValidator.isBagIdAcceptable(_bagId))
        {
            logger.error("bagId failed requirements validation");
            throw new ValidationException("Invalid input for bagid");
        }
        bagId = _bagId;
    }

    private void setEntry(String _entry) throws ValidationException
    {
        if (!RequirementValidator.isTerminalAcceptable(_entry))
        {
            logger.error("bag entry failed requirement validation");
            throw new ValidationException("Invalid input for bag entry point");
        }
        entry = new Terminal(_entry);
    }

    private void setDestination(String _destination) throws ValidationException
    {
        if (!RequirementValidator.isTerminalAcceptable(_destination))
        {
            logger.error("bag destination failed requirement validation");
            throw new ValidationException("Invalid input for bag destination point");
        }
        destination = new Terminal(_destination);
    }

    public String getBagId() {
        return bagId;
    }

    public Terminal getEntry() {
        return entry;
    }

    public Terminal getDestination() {
        return destination;
    }
}
