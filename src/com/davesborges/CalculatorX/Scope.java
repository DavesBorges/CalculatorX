package com.davesborges.CalculatorX;

public class Scope {
    private String sourceName;
    private FunctionTable functionTable;
    private VariableTable variableTable;

    public Scope() throws Exception {
        functionTable = new MapFunctionTable();
        variableTable = new MapVariableTable();
        defineFunction("sqrt", new PredefinedFunction("sqrt", new String[]{"number"}, doubles -> Math.sqrt(doubles[0])));
        defineConstant("PI", 3.14159265358979323846264338327950288419716939937510582097494459230);
        defineConstant("e", 2.718281828459045235360287471352662497757247093);
    }
    public Scope(Scope scope){
        functionTable = new MapFunctionTable(scope.functionTable);
        variableTable = new MapVariableTable(scope.variableTable);

    }

    public boolean isVariableDeclared(String variableName){
        return variableTable.isDeclared(variableName);
    }

    public boolean isFunctionDeclared(String functionName){
        return functionTable.isDeclared(functionName);
    }
    public String getSourceName(){
        return sourceName;
    }

    public void assignVariable(String variableName, double newValue) throws Exception {
        variableTable.onAssignVariable(variableName, newValue);
    }

    public void defineFunction(String functionName, Function function){
        functionTable.onDefineFunction(functionName, function);
    }

    public void declareVariable(String name){
        variableTable.onDeclareVariable(name, new Variable(name));
    }

    public boolean isConstant(String name){
        return variableTable.isConstant(name);
    }
    public void defineVariable(String variableName, double value){
        variableTable.onDeclareVariable(variableName, new Variable(variableName, value));
    }
    public void defineConstant(String variableName, double value) throws Exception {
        variableTable.onDeclareConstant(variableName, new Variable(variableName, value, true));
    }
    public String[] getParameters(String functinoName){
        return functionTable.getParameters(functinoName);
    }

    public double getValue(String stringValue) throws Exception {
        return variableTable.getValue(stringValue);
    }

    public double callFunction(String functionName, double[] arguments) throws Exception {
        return functionTable.invoke(functionName, arguments);
    }
}
