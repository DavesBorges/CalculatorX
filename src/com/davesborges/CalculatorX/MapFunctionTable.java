package com.davesborges.CalculatorX;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapFunctionTable implements FunctionTable {
    private Map<String, Function> functionMap;

    public MapFunctionTable(){
        functionMap = new HashMap<>();
    }
    public MapFunctionTable(FunctionTable functionTable) {
        functionMap = new HashMap<>();
        Arrays.stream(functionTable.getAllFunctions()).forEach(function -> functionMap.put(function.getName(), function));
    }

    @Override
    public double invoke(String functionName, double[] arguments) throws Exception {
        return functionMap.get(functionName).onCall(arguments);
    }

    @Override
    public void onDefineFunction(String name, Function function) {
        functionMap.put(name, function);
    }

    @Override
    public Function[] getAllFunctions() {
        return functionMap.values().toArray(new Function[functionMap.values().size()]);
    }

    @Override
    public boolean isDeclared(String functionName) {
        return functionMap.containsKey(functionName);
    }

    @Override
    public String[] getParameters(String functinoName) {
        return functionMap.get(functinoName).getParamters();
    }


}
