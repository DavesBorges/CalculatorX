package com.davesborges.CalculatorX;

public interface VariableTable {
    void onDeclareVariable(String name, Variable variable);
    void onDeclareConstant(String name, Variable constant) throws Exception;
    void onAssignVariable(String name, double newValue) throws Exception;
    Variable[] getAllVariables();

    boolean isDeclared(String variableName);

    double getValue(String stringValue) throws Exception;

    boolean isConstant(String name);
}
