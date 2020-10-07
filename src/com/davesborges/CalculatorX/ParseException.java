package com.davesborges.CalculatorX;

public class ParseException extends Exception{
    private String position;
    private Position startingPosition;


    public ParseException(String msg, String position){
        super(msg);
        this.position = position;
    }

    @Override
    public String getMessage() {
        return super.getMessage()  + " in " + position;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }

    public Position getStartingPosition() {
        return startingPosition;
    }
}
