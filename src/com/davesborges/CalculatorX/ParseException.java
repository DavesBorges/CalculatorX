package com.davesborges.CalculatorX;

public class ParseException extends Exception{
    private String position;
    private String message;


    public ParseException(String msg, String position){
        super(msg);
        this.message = msg;
        this.position = position;
    }

    @Override
    public String getMessage() {
        return message  + " at " + position;
    }
}
