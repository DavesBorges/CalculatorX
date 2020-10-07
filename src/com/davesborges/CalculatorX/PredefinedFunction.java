package com.davesborges.CalculatorX;

import java.util.Arrays;

public class PredefinedFunction implements Function{
    private String name;
    private String[] parameters;
    private java.util.function.Function<Double[], Double> function;

    public PredefinedFunction(String name, String[] parameters, java.util.function.Function<Double[], Double> function){
        this.name = name;
        this.parameters = parameters;
        this.function = function;
    }
    @Override
    public double onCall(double[] arguments) throws Exception {
        Double[] convertedArguments = new Double[arguments.length];
        for(int i = 0; i < arguments.length; ++i){
            convertedArguments[i] = arguments[i];
        }
        return function.apply(convertedArguments);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getParameters() {
        return parameters;
    }
}
