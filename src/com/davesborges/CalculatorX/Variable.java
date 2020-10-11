package com.davesborges.CalculatorX;

public class Variable {
    private double value;
    private String name;
    private boolean defined;
    private final boolean isConstant;

    public Variable(String name){
        this.name = name;
        defined = false;
        isConstant = false;

    }

    public Variable(String name,double value) {
        this.value = value;
        this.name = name;
        defined = true;
        isConstant = false;
    }

    public Variable(String name, double value, boolean isConstant){
         this.value = value;
         this.name = name;
         defined = true;
         this.isConstant = isConstant;
    }
    public double getValue() throws Exception {
        if(!defined){
            throw new Exception("undefined variable ");
        }
        return value;
    }

    public void setValue(double value) throws Exception {
        if(isConstant)
            throw new Exception("Cannot setValue to Constant variable");
        defined = true;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConstant(){
        return isConstant;
    }


}
