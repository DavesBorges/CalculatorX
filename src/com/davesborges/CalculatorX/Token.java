package com.davesborges.CalculatorX;

public class Token {
    private char kind;
    private double value;
    private String raw;

    public Token(char kind, double value){
        this.kind = kind;
        this.value = value;
    }

    public Token(double value){
        this.kind = number;
        this.value = value;
    }

    public Token(char ch){
        this.kind = ch;
    }

    public Token(String raw){
        this.kind = name;
        this.raw = raw;
    }

    //Getters
    public char getKind() {
        return kind;
    }

    public double getValue() {
        return value;
    }

    public String getRaw() {
        if(raw != null)
            return raw;
        if(kind == number)
            return Double.toString(value);
        return Character.toString(kind);
    }

    @Override
    public String toString() {
        return "[kind: " + kind + "] [value: " + value + "] [raw: " + raw + "]";
    }

    //Constants for identifing the kind of a token
    static final char name = 'n';
    static final char number = '0';
    static final char statementEnd = ';';

    //Constants for identifing the value of a string token
    static final String nameKeyWord = "name";

}
