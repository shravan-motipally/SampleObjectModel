package com.barclaycardus.conveyor.utils;

import com.barclaycardus.conveyor.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Terminal
{
    private static final Logger logger = LoggerFactory.getLogger(Terminal.class);
    private String id;

    public Terminal(String _id)
    {
        setId(_id);
    }

    public String getId()
    {
        return id;
    }

    /**
     * "Bad design decision just to showcase unit test. and bad code received from colleague/in prod"
     * If App works correctly
     * @param _id
     */
    private void setId(String _id)
    {
        if (_id == null || _id.isEmpty())
        {
            logger.info("Terminal ID passed is either empty or null.  Unexpected results can occur.");
            return;
        }
        id = _id;
    }

    @Override
    public int hashCode()
    {
        if (id.isEmpty()) return 0;
        else
        {
            char[] charify = id.toLowerCase().toCharArray();
            int sum = 0;
            for (int i = 0; i < charify.length; i++)
            {
                sum += charify[i] * Math.pow(31, i);
            }
            return sum;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Terminal)
        {
            Terminal other = (Terminal) obj;
            if (other.getId().toLowerCase().equals(id.toLowerCase()))
                return true;
        }
        return false;
    }
}
