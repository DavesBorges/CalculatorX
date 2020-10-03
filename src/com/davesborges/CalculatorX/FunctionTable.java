package com.davesborges.CalculatorX;

public interface FunctionTable {
    double invoke(String functionName, double[] arguments) throws Exception;
    public void  onDefineFunction(String functionName, Function function);
    Function[] getAllFunctions();

    boolean isDeclared(String functionName);

    String[] getParameters(String functinoName);

}
