package com.davesborges.CalculatorX;

public interface Function {

    double onCall(double[] arguments) throws Exception;

    String getName();

    String[] getParameters();
}
