package com.davesborges.CalculatorX;

public interface VariableTable {
    void onDeclareVariable(String name, Variable variable);
    void onAssignVariable(String name, double newValue);
    Variable[] getAllVariables();

    boolean isDeclared(String variableName);

    double getValue(String stringValue) throws Exception;
}
