package com.davesborges.CalculatorX;

public class Variable {
    private double value;
    private String name;
    boolean defined;

    public Variable(String name){
        this.name = name;
        defined = false;
    }

    public Variable(String name,double value) {
        this.value = value;
        this.name = name;
        defined = true;
    }

    public double getValue() throws Exception {
        if(!defined){
            throw new Exception("undefined variable ");
        }
        return value;
    }

    public void setValue(double value) {
        defined = true;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
