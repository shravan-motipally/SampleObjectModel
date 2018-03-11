package com.barclaycardus.conveyor.exception;

public class NotFoundException extends Exception
{
    private String message = "";

    public NotFoundException(String _message)
    {
        message = _message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
