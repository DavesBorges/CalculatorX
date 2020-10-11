package com.davesborges.CalculatorX;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapVariableTable implements VariableTable {
    private Map<String, Variable> variablesMap;

    public MapVariableTable(){
        variablesMap = new HashMap<>();
    }

    public MapVariableTable(VariableTable variableTable) {
        variablesMap = new HashMap<>();
        Arrays.stream(variableTable.getAllVariables())
                .forEach(variable -> variablesMap.put(variable.getName(), variable));
    }

    @Override
    public void onDeclareVariable(String name, Variable variable) {
        variablesMap.put(name, variable);
    }

    @Override
    public void onDeclareConstant(String name, Variable constant) throws Exception {
        variablesMap.put(name, new Variable(name, constant.getValue(), constant.isConstant()));
    }

    @Override
    public void onAssignVariable(String name, double newValue) throws Exception {
        variablesMap.get(name).setValue(newValue);
    }

    @Override
    public Variable[] getAllVariables() {
        return variablesMap.values().toArray(new Variable[variablesMap.values().size()]);
    }

    @Override
    public boolean isDeclared(String variableName) {
        return variablesMap.containsKey(variableName);
    }

    @Override
    public double getValue(String stringValue) throws Exception {
        return variablesMap.get(stringValue).getValue();
    }

    @Override
    public boolean isConstant(String variableName){
        return variablesMap.get(variableName).isConstant();
    }
}
