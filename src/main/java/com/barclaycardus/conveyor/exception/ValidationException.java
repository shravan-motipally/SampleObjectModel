package com.barclaycardus.conveyor.exception;

public class ValidationException extends Exception
{
    private String message = "";

    public ValidationException(String _message)
    {
        message = _message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
