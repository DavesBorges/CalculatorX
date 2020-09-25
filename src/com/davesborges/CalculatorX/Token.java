package com.davesborges.CalculatorX;

public class Token {
    private char kind;
    private double value;
    private String stringValue;
    private Position position;

    public Token(char kind, double value, Position position){
        this.kind = kind;
        this.value = value;
        this.position = position;
    }

    public Token(double value, Position position){
        this.kind = number;
        this.value = value;
        this.position = position;
    }

    public Token(char ch, Position position){
        this.kind = ch;
        this.position = position;
    }

    public Token(String raw, Position position){
        this.kind = name;
        this.stringValue = raw;
        this.position = position;
    }

    //Getters
    public char getKind() {
        return kind;
    }

    public double getValue() {
        return value;
    }

    public String getRaw() {
        if(stringValue != null)
            return stringValue;
        if(kind == number)
            return Double.toString(value);
        return Character.toString(kind);
    }

    @Override
    public String toString() {
        return "[kind: " + kind + "] [value: " + value + "] [stringValue: " + stringValue + "] " + position.getFile() +
                "[Line: "+ position.getLineNr() + "] [Column: "+ position.getColumnNr() + "]";
    }

    //Constants for identifing the kind of a token
    static final char name = 'n';
    static final char number = '0';
    static final char statementEnd = ';';

    //Constants for identifing the value of a string token
    static final String nameKeyWord = "name";

}
